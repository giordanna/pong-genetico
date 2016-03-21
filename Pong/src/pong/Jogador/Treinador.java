package pong.Jogador;

// AQUI onde treina 30 genótipos
// to do: fazer receber jogadores, e não genótipos

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class Treinador implements IJogador {
    private static int MAIOR_FITNESS = 9999;
    private static int qtd_treinador = 0;
    private int treinador;
    private Genotipo populacao[];
    private double intervalo;
    private boolean bola_passou;
    private int contraatacou;
    private int atual;
    private int ultima_distancia;
    private int posicao_inicial = Integer.MAX_VALUE;
    private Writer output_fitness, output_genotipo;
    
    public Treinador() throws IOException{
        qtd_treinador++;
        treinador = qtd_treinador;
        
        intervalo = Configuracao.INTERVALO_GENES;
        bola_passou = false;
        contraatacou = 0;
        atual = 0;
        
        output_fitness = new BufferedWriter(new FileWriter("fitness" + treinador + ".csv"));
        output_fitness.write("");
        output_fitness.close();
        
        output_genotipo = new BufferedWriter(new FileWriter("melhorgenotipo" + treinador + ".csv"));
        output_genotipo.write("");
        output_genotipo.close();
        
        inicializaPopulacao();
    }
    
    public void inicializaPopulacao(){
        populacao = new Genotipo[Configuracao.MAX_POPULACAO];
        
        for (int i = 0 ; i < Configuracao.MAX_POPULACAO ; i++){
            populacao[i] = Genotipo.genotipoAleatorio(-intervalo, intervalo);
        }
    }
    
    public int retornaInicio(Raquete minha){
        if (posicao_inicial == Integer.MAX_VALUE)
            posicao_inicial = minha.getY();
        return posicao_inicial - minha.getY();
    }
    
    public int distanciaPercorrida(Raquete minha, Bola bola){
        int distancia = 0;
        if ( (minha.getX() >= (Configuracao.ALTURA_TELA / 2) && bola.getX() > minha.getX() + 1 ) ||
                (minha.getX() < Configuracao.ALTURA_TELA / 2 && bola.getX() < minha.getX() - 1) ){
            bola_passou = true;
            if (bola.getY() > minha.getY())
                distancia = bola.getY() - (minha.getY() + minha.getAltura());
            else
                distancia = minha.getY() - (bola.getY() + bola.getAltura());
        }
        return distancia;
    }
    
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola){
        if (bola.getMovimentoX() == 0 && bola.getMovimentoY() == 0)
            return retornaInicio(minha);
        
        contraatacou++;
        if (!bola_passou)
            ultima_distancia = distanciaPercorrida(minha, bola);
        
        return populacao[atual].validaVelocidade(minha, oponente, bola);
    }
    
    // cálculo do fitness
    @Override
    public void resultado(int ponto){
        int fitness = 0;
        
        if (ponto > 0)
            fitness = MAIOR_FITNESS; // se marcou um ponto então ele é bom mesmo
        
        fitness += contraatacou;
        fitness += 480 - 3 * ultima_distancia;
        bola_passou = false;
        
        if (populacao[atual].getFitness() != 0){
            populacao[atual].setFitness(populacao[atual].getFitness() + fitness);
            populacao[atual].setFitness(populacao[atual].getFitness() / 2);
        }
        else{
            populacao[atual].setFitness(fitness);
        }
        
        contraatacou = 0;
        atual++;
        
        if (atual >= Configuracao.MAX_POPULACAO){
            Arrays.sort(populacao);
            try {
                salvaPopulacao();
            } catch (IOException ex) {
                Logger.getLogger(Treinador.class.getName()).log(Level.SEVERE, null, ex);
            }
            repopularPopulacao();
            atual = 0;
        }
    }
    
    public Genotipo melhorGenotipo(){
        Genotipo melhor = new Genotipo(Genotipo.genotipoAleatorio(0, 0));
        
        for (int i = 0 ; i < Configuracao.MAX_POPULACAO ; i++){
            if ( populacao[i].getFitness() > melhor.getFitness() ){
                melhor = new Genotipo(populacao[i]);
            }
        }
        
        return melhor;
    }
    
    public void salvaPopulacao() throws IOException{
        // salva num(s) arquivo(s)
        output_fitness = new BufferedWriter(new FileWriter("fitness" + treinador + ".csv", true));
        
        for (Genotipo x: populacao){
            output_fitness.append( (x.getFitness() + "; ").replace(".",",") );   
        }
        output_fitness.append("\n");
        output_fitness.close();
        
        output_genotipo = new BufferedWriter(new FileWriter("melhorgenotipo" + treinador + ".csv", true));
        output_genotipo.append((Arrays.toString(melhorGenotipo().getGenes())).replace(".",","));
        output_genotipo.append("\n");
        output_genotipo.close();
        
    }
    
    public void repopularPopulacao(){
        
        //torna os piores 3/4 nulos
        for (int i = populacao.length/4 ; i < populacao.length ; i++){
            populacao[i] = null;
        }
        
        int j = 0;
        int outro;
        // preenche metade com os melhores + um genótipo aleatório da mesma geração
        for (int i = populacao.length/4 ; i < 3*populacao.length/4 ; i++){
            while (true){
                outro = Configuracao.R.nextInt(populacao.length/4);
                if (j != outro) break;
            }
            populacao[i] = new Genotipo(Genotipo.crossover(populacao[j], populacao[outro]));
            // realiza a mutação
            j++;
        }
        
        // adiciona alguns poucos genótipos com mutação
        for (int i = 3*populacao.length/4 ; i < 7*populacao.length/8 ; i++){
            outro = Configuracao.R.nextInt(populacao.length/2-1);
            populacao[i] = new Genotipo(Genotipo.mutacao(populacao[outro]));
        }
        
        // preenche o resto com novos genótipos aleatórios
        for (int i = 7*populacao.length/8 ; i < populacao.length ; i++){
            populacao[i] = Genotipo.genotipoAleatorio(-intervalo, intervalo);
        }
    }
}