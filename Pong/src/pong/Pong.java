package pong;

// copia do pong.java onde está sendo executado testes
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.Timer;
import pong.Jogador.*;
import pong.Jogador.IJogador;
import static pong.Pong.Status.*;
import pong.Outros.Configuracao;
import pong.Outros.MersenneTwisterFast;
import pong.Outros.Renderizador;

public class Pong implements ActionListener, KeyListener {
    
    Configuracao conf = new Configuracao();
    
    public static Timer timer;

    public JFrame jframe;
    public Renderizador renderizador;
    
    public static Pong pong;
    
    // dimensões da tela
    public int largura = Configuracao.LARGURA_TELA, altura = Configuracao.ALTURA_TELA;
    
    public final static  MersenneTwisterFast R = new  MersenneTwisterFast();

    public Raquete raquete_esquerda, raquete_direita; // independentes
    
    public IJogador jogador_esquerda, jogador_direita; // os que vão ser usados de fato
    
    public IJogador instancias_esquerda[]; // humano, ai basico, ai perfeito, ai genético, treinador
    public IJogador instancias_direita[]; // humano, ai basico, ai perfeito, ai genético, treinador

    public Bola bola;

    public boolean seleciona_jogador_esquerda = true;
    
    // botões pros humanos usarem
    public boolean w, s, cima, baixo;

    // enum com Menu, Pausado, Jogando (não existe limite)
    public Status status_jogo = Menu;
    
    // humano = 0, ai basico = 1, ai perfeito = 2, ai genético = 3, treinador = 4
    public int opcao_jogador_esquerda = 0, opcao_jogador_direita = 0;
    
    public int partida = 0;
    
    public static int velocidade = 3;

    public Pong() throws IOException {
        instancias_esquerda = new IJogador[5];
        instancias_esquerda[0] = new Humano(KeyEvent.VK_W, KeyEvent.VK_S);
        instancias_esquerda[1] = new AIBasico();
        instancias_esquerda[2] = new AIPerfeito();
        instancias_esquerda[3] = new AIGenetico(Genotipo.genotipoAleatorio(0, 0));
        instancias_esquerda[4] = null;
        
        instancias_direita = new IJogador[5];
        instancias_direita[0] = new Humano(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        instancias_direita[1] = new AIBasico();
        instancias_direita[2] = new AIPerfeito();
        instancias_direita[3] = new AIGenetico(Genotipo.genotipoAleatorio(0, 0));
        instancias_direita[4] = null;
        
        // tempo de atualização dos frames. mexer nisso aqui
        timer = new Timer(20, this);

        jframe = new JFrame("Pong Genético");

        renderizador = new Renderizador();
        
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderizador);
        jframe.addKeyListener(this);
        jframe.setContentPane(renderizador);
        jframe.pack();
        jframe.setVisible(true);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jframe.setLocation(dim.width/2-jframe.getSize().width/2, dim.height/2-jframe.getSize().height/2);

        timer.start();
    }
    
    public enum Status {
        Menu, Pausado, Jogando;
    }

    public void inicializarPong() {
        status_jogo = Jogando;
        raquete_esquerda = new Raquete(pong, 1);
        raquete_direita = new Raquete(pong, 2);
        
        bola = new Bola(pong);
    }
    
    // escreve o texto centralizado
    private void escreveTexto(Graphics2D g, String texto, int x, int y){
        int comprimento = (int)
            g.getFontMetrics().getStringBounds(texto, g).getWidth();
        int inicio = largura/2 - comprimento/2;
        g.drawString(texto, inicio + x, y);
 }
    
    public void modoMenu(Graphics2D g){
        
        String[] string = {"Humano", "AI Básico", "AI Perfeito", "AI Genético", "AI Treinador"};
        if (status_jogo == Menu) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Configuracao.FONTE, Font.BOLD, 50));
            escreveTexto(g, "PONG GENÉTICO", 0, 50);

            if (seleciona_jogador_esquerda) {
                
                g.setFont(new Font(Configuracao.FONTE, Font.BOLD, 30));
                escreveTexto(g, "← Jogador 1: " + string[opcao_jogador_esquerda] + " →" , 0, altura / 2 - 25);
                                
                g.setFont(new Font(Configuracao.FONTE, Font.PLAIN, 30));
                escreveTexto(g, "Jogador 2: " + string[opcao_jogador_direita] , 0, altura / 2 + 25);
                escreveTexto(g, "Pressione Espaço para jogar" , 0, altura / 2 + 75);

                g.setFont(new Font(Configuracao.FONTE, Font.BOLD, 50));
                g.drawString("↑↓" , 10, altura / 2);
            }
            
            if (!seleciona_jogador_esquerda) {

                g.setFont(new Font(Configuracao.FONTE, Font.PLAIN, 30));
                escreveTexto(g, "Jogador 1: " + string[opcao_jogador_esquerda], 0, altura / 2 - 25);
                escreveTexto(g, "Pressione Espaço para jogar" , 0, altura / 2 + 75);

                g.setFont(new Font(Configuracao.FONTE, Font.BOLD, 30));
                escreveTexto(g, "← Jogador 2: " + string[opcao_jogador_direita] + " →" , 0, altura / 2 + 25);

                g.setFont(new Font(Configuracao.FONTE, Font.BOLD, 50));
                g.drawString("↑↓" , 10, altura / 2);
            }
        }
    }
    
    public void modoPausado(Graphics2D g){
        if (status_jogo == Pausado) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Configuracao.FONTE, 1, 50));
            escreveTexto(g, "PAUSA", 0, altura / 2);
        }
    }
    
    public void modoJogando(Graphics2D g){
        // porque também tem que mostrar o fundo quando o jogo tá pausado
        if (status_jogo == Pausado || status_jogo == Jogando) {
            g.setColor(Color.WHITE);

            g.setStroke(new BasicStroke(5f));

            // faz a linha do meio
            g.drawLine(largura / 2, 0, largura / 2, altura);

            g.setStroke(new BasicStroke(5f));

            // faz o círculo do meio
            g.drawOval(largura / 2 - 150, altura / 2 - 150, 300, 300);

            g.setFont(new Font(Configuracao.FONTE, 1, 50));
            
            // faz a pontuação
            g.drawString(String.valueOf(raquete_esquerda.getScore()), largura / 2 - 90, 50);
            g.drawString(String.valueOf(raquete_direita.getScore()), largura / 2 + 65, 50);
            
            // informa qual o indivíduo atual e a geração caso o jogador da esquerda seja treinador
            if (opcao_jogador_esquerda == 4){
                g.setFont(new Font(Configuracao.FONTE, 1, 30));
                g.drawString("Atual: " + ((Treinador) jogador_esquerda).getAtual(), 50, 30);
                
                g.setFont(new Font(Configuracao.FONTE, 1, 25));
                g.drawString("Geração: " + ((Treinador) jogador_esquerda).getGeracao(), 50, altura - 20);
            }
            
            // informa qual o indivíduo atual e a geração caso o jogador da direita seja treinador
            if (opcao_jogador_direita == 4){
                g.setFont(new Font(Configuracao.FONTE, 1, 30));
                g.drawString("Atual: " + ((Treinador) jogador_direita).getAtual(), 3*largura/ 4, 30);
                
                g.setFont(new Font(Configuracao.FONTE, 1, 25));
                g.drawString("Geração: " + ((Treinador) jogador_direita).getGeracao(), 3*largura/ 4, altura - 20);
            }
            
            g.setFont(new Font(Configuracao.FONTE, 1, 20));
            escreveTexto(g, "Velocidade: " + getVelocidade(), 0, altura - 30);
            
            // aqui que acontece a mágica das raquetes e da bola
            raquete_esquerda.renderizarRaquete(g);
            raquete_direita.renderizarRaquete(g);
            bola.renderizarBola(g);
        }
    }

    public void renderizarPong(Graphics2D g) {
        g.setColor(new Color(49,41,96));
        g.fillRect(0, 0, largura, altura);
        
        // formas e letras não ficarão serrilhadas
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        modoMenu(g);
        modoJogando(g);
        modoPausado(g);
    }
    
    public void atualizaInstancias() throws IOException{
        
        if (opcao_jogador_esquerda == 4) // AI Treinador
            if (instancias_esquerda[opcao_jogador_esquerda] == null){
                instancias_esquerda[opcao_jogador_esquerda] = new Treinador();
            }
        
        if (opcao_jogador_direita == 4) // AI Treinador
            if (instancias_direita[opcao_jogador_direita] == null){
                instancias_direita[opcao_jogador_direita] = new Treinador();
            }
        
        if (opcao_jogador_esquerda == 3){ // AI genético
            if (instancias_esquerda[4] != null)
                instancias_esquerda[opcao_jogador_esquerda] = new AIGenetico(((Treinador) instancias_esquerda[4]).melhorGenotipo());
        }
        
        if (opcao_jogador_direita == 3){ // AI genético
            if (instancias_direita[4] != null)
                instancias_direita[opcao_jogador_direita] = new AIGenetico(((Treinador) instancias_direita[4]).melhorGenotipo());
        }
        
        jogador_esquerda = instancias_esquerda[opcao_jogador_esquerda];
        jogador_direita = instancias_direita[opcao_jogador_direita];
    }
    
    // to do: mexer nisso aqui
    public void atualizarPong() {
        
        int ponto;

        // humano esquerda
        if (opcao_jogador_esquerda == 0){
            if (w) {
                raquete_esquerda.mover(true);
            }
            if (s) {
                raquete_esquerda.mover(false);
            }
        }
        else{ // então é AI
            raquete_esquerda.mover(jogador_esquerda.verificaDirecao(
                    raquete_esquerda, raquete_direita, bola));
        }
        
        // humano direita
        if (opcao_jogador_direita == 0){
            if (cima) {
                raquete_direita.mover(true);
            }
            if (baixo) {
                raquete_direita.mover(false);
            }
        }
        else{ // então é AI
            raquete_direita.mover(jogador_direita.verificaDirecao(
                    raquete_direita, raquete_esquerda, bola));
        }

        ponto = bola.atualizarBola(raquete_esquerda, raquete_direita);
        if (ponto != 0){
            // se ponto = 1 (ou seja, esquerda marcou ponto), jogador esquerda recebe 1. se ponto = -1, jogador esquerda recebe -1
            jogador_esquerda.resultado(ponto); // só tem efeito mesmo se for treinador
            // se ponto = -1 (ou seja, direita marcou ponto), jogador direita recebe 1. se ponto = 1, jogador direita recebe -1
            jogador_direita.resultado(-ponto); // só tem efeito mesmo se for treinador
            partida++;
            if (jogador_esquerda instanceof Treinador || jogador_direita instanceof Treinador){
                if (partida == 3){
                    raquete_esquerda.resetScore();
                    raquete_direita.resetScore();
                    partida = 0;
                }
            }
        }
    }

    // onde realiza o update dos frames
    @Override
    public void actionPerformed(ActionEvent e) {
        if (status_jogo == Jogando) {
            atualizarPong();
        }

        renderizador.repaint();
    }
 
    public static void main(String[] args) throws IOException {
        pong = new Pong();
    }
    
    public static void mudarVelocidade(){
        switch (velocidade){
            case 1:{
                timer.stop();
                timer = new Timer(0,pong);
                timer.start();
                break;
            }
            case 2:{
                timer.stop();
                timer = new Timer(10,pong);
                timer.start();
                break;
            }
            case 3:{
                timer.stop();
                timer = new Timer(20,pong);
                timer.start();
                break;
            }
        }
    }
    
    public String getVelocidade(){
        switch (velocidade){
            case 1:{
                return "Super Rápido";
            }
            case 2:{
                return "Rápido";
            }
            case 3:{
                return "Normal";
            }
        }
        return "Normal";
    }

    // o que fazer quando pressionar teclas
    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) { // quando pressionar seta pra cima
            cima = true;
            if (status_jogo == Menu) {
                if (!seleciona_jogador_esquerda) seleciona_jogador_esquerda = true;
            }
        } else if (id == KeyEvent.VK_DOWN) { // quando pressionar seta pra baixo
            baixo = true;
            if (status_jogo == Menu) {
                if (seleciona_jogador_esquerda) seleciona_jogador_esquerda = false;
            }
        } else if (id == KeyEvent.VK_RIGHT) { // quando pressionar seta pra direita
            if (status_jogo == Menu){
                if (seleciona_jogador_esquerda) {
                    if (opcao_jogador_esquerda < 4) {
                        opcao_jogador_esquerda++;
                    } else {
                        opcao_jogador_esquerda = 0;
                    }
                }
                else{
                    if (opcao_jogador_direita < 4) {
                        opcao_jogador_direita++;
                    } else {
                        opcao_jogador_direita = 0;
                    }
                }
            }
        } else if (id == KeyEvent.VK_LEFT) { // quando pressionar seta pra esquerda
            if (status_jogo == Menu){
                if (seleciona_jogador_esquerda) {
                    if (opcao_jogador_esquerda > 0) {
                        opcao_jogador_esquerda--;
                    } else {
                        opcao_jogador_esquerda = 4;
                    }
                }
                else{
                    if (opcao_jogador_direita > 0) {
                        opcao_jogador_direita--;
                    } else {
                        opcao_jogador_direita = 4;
                    }
                }
            }
            // se apertou Esc no jogo ou quando tava pausado, volta pro menu
        } else if (id == KeyEvent.VK_ESCAPE && ((status_jogo == Jogando) || (status_jogo == Pausado))) {
            status_jogo = Menu;
        } else if (id == KeyEvent.VK_SPACE) {
            if (status_jogo == Menu){
                
                try {
                    // instâncias escolhidas
                    atualizaInstancias();
                } catch (IOException ex) {
                    Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
                }
                velocidade = 3;
                mudarVelocidade();
                status_jogo = Jogando;
                partida = 0;
                inicializarPong();
            } else if (status_jogo == Pausado) {
                status_jogo = Jogando;
            } else if (status_jogo == Jogando) {
                status_jogo = Pausado;
            }
        } else if (id >= 49 && id <= 51){ // 49 = tecla 1 e 51 = tecla 3
            if (status_jogo == Jogando){
                velocidade = 52 - id;
                mudarVelocidade();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        switch (id) {
            case KeyEvent.VK_W:
                w = false;
                break;
            case KeyEvent.VK_S:
                s = false;
                break;
            case KeyEvent.VK_UP:
                cima = false;
                break;
            case KeyEvent.VK_DOWN:
                baixo = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}