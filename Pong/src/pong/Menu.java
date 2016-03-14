package pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import pong.Jogador.IJogador;
import static pong.Menu.Status.*;
import pong.Outros.Configuracao;
import pong.Outros.Renderizador;

public class Menu implements ActionListener, KeyListener {

    public JFrame jframe;
    public Renderizador renderizador;
    
    public static Pong pong;
    
    // dimensões da tela
    public int largura = Configuracao.LARGURA_TELA, altura = Configuracao.ALTURA_TELA;
    
    public final static Random R = new Random();

    public Raquete raquete_esquerda, raquete_direita; // independentes
    
    public IJogador jogador_esquerda, jogador_direita; // os que vão ser usados de fato
    
    public IJogador instancias_esquerda[]; // humano, ai basico, ai perfeito, ai genético, treinador
    public IJogador instancias_direita[]; // humano, ai basico, ai perfeito, ai genético, treinador

    public Bola bola;

    // to do: retirar
    public boolean seleciona_jogador_esquerda;
    
    // botões pros humanos usarem
    public boolean w, s, cima, baixo;

    // enum com Menu, Pausado, Jogando
    public Status status_jogo = Menu;
    
    // humano = 0, ai basico = 1, ai perfeito = 2, ai genético = 3, treinador = 4
    public int opcao_jogador_esquerda = 0, opcao_jogador_direita = 0;

    public Menu() {
        
        Timer timer = new Timer(20, this);

        jframe = new JFrame("Pong Genético");

        renderizador = new Renderizador();

        jframe.setSize(largura + 15, altura + 35);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderizador);
        jframe.addKeyListener(this);

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
            g.setFont(new Font(Configuracao.FONTE, 1, 50));
            escreveTexto(g, "PONG GENÉTICO", 0, 50);

            if (!seleciona_jogador_esquerda) {
                g.setFont(new Font(Configuracao.FONTE, 1, 30));
                escreveTexto(g, "<< Jogador 1: " + string[opcao_jogador_esquerda] + " >>" , 0, altura / 2 - 25);
                escreveTexto(g, "Pressione Espaço para jogar" , 0, altura / 2 + 25);
            }
        }

        if (seleciona_jogador_esquerda) {

            g.setFont(new Font(Configuracao.FONTE, 1, 30));
            escreveTexto(g, "<< Jogador 2: " + string[opcao_jogador_direita] + " >>" , 0, altura / 2 - 25);
            escreveTexto(g, "Pressione Espaço para jogar" , 0, altura / 2 + 25);
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
            
            // aqui que acontece a mágica das raquetes e da bola
            raquete_esquerda.renderizarRaquete(g);
            raquete_direita.renderizarRaquete(g);
            bola.renderizarBola(g);
        }
    }

    public void renderizarPong(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, largura, altura);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        modoMenu(g);
        modoPausado(g);
        modoMenu(g);
    }
    
    // to do: mexer nisso aqui
    public void atualizarPong() {

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

        bola.atualizarBola(raquete_esquerda, raquete_direita);
    }

    // onde realiza o update dos frames
    @Override
    public void actionPerformed(ActionEvent e) {
        if (status_jogo == Jogando) {
            atualizarPong();
        }

        renderizador.repaint();
    }
    /*

    public static void main(String[] args) {
        pong = new Pong();
    }
    */

    // o que fazer quando pressionar teclas
    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) {
            cima = true;
        } else if (id == KeyEvent.VK_DOWN) {
            baixo = true;
        } else if (id == KeyEvent.VK_RIGHT) { // quando pressionar seta pra direita
            if (seleciona_jogador_esquerda) {
                if (opcao_jogador_esquerda < 5) {
                    opcao_jogador_esquerda++;
                } else {
                    opcao_jogador_esquerda = 0;
                }
            }
            else{
                if (opcao_jogador_direita < 5) {
                    opcao_jogador_direita++;
                } else {
                    opcao_jogador_direita = 0;
                }
            }
        } else if (id == KeyEvent.VK_LEFT) { // quando pressionar seta pra esquerda
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
        } else if (id == KeyEvent.VK_ESCAPE && (status_jogo == Jogando)) {
            status_jogo = Menu;
        } else if (id == KeyEvent.VK_SHIFT && status_jogo == Menu) {
            seleciona_jogador_esquerda = true;
        } else if (id == KeyEvent.VK_SPACE) {
            if (status_jogo == Menu) seleciona_jogador_esquerda = false;

            inicializarPong();
            
            } else if (status_jogo == Pausado) {
                status_jogo = Jogando;
            } else if (status_jogo == Jogando) {
                status_jogo = Pausado;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = false;
        } else if (id == KeyEvent.VK_S) {
            s = false;
        } else if (id == KeyEvent.VK_UP) {
            cima = false;
        } else if (id == KeyEvent.VK_DOWN) {
            baixo = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}