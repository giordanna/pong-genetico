package pong.Jogador;

import pong.Outros.Configuracao;
import java.util.Arrays;
import pong.Bola;
import pong.Raquete;

public class Genotipo implements Comparable<Genotipo>{
    
    private double gene[];
    private double fitness;
    
    public Genotipo(){
        gene = new double[Configuracao.TAMANHO_CROMOSSOMO];
        fitness = 0;
    }
    
    public Genotipo( double [] genes ){
        
        gene = new double[genes.length];
        System.arraycopy(genes, 0, gene, 0, genes.length);  
        fitness = 0;
    }
    
    public Genotipo( Genotipo copia ){
        
        gene = new double[copia.gene.length];
        System.arraycopy(copia.gene, 0, gene, 0, copia.gene.length);
        this.fitness = copia.fitness;
    }
    
    public double getFitness() { return fitness; }
    
    public void setFitness( double fitness ){
        this.fitness = fitness;
    }
    
    public double [] getGenes() { return gene; }
    
    // valida velocidade y da raquete
    // retorna velocidade da raquete
    public double validaVelocidade(Raquete jogador, Raquete oponente, Bola bola){
        return gene[0] * bola.getMovimentoY() + gene[1] * bola.getY() + gene[2] * jogador.getY();
    }
    
    // retorna um genótipo aleatório
    public static Genotipo genotipoAleatorio(double menor, double maior){
        Genotipo novo = new Genotipo();
        for (int i = 0 ; i < Configuracao.TAMANHO_CROMOSSOMO ; i++){
            novo.gene[i] = menor + (maior - menor) * Configuracao.R.nextDouble(true,true);
        }
        
        return novo;
    }
    
    // retorna filho produzido por dois pais através de crossover por média aritmética
    // todo mudar pra cruzamento aritmético mesmo
    public static Genotipo crossover(Genotipo a, Genotipo b){
        Genotipo novo = new Genotipo();
        for (int i = 0 ; i < Configuracao.TAMANHO_CROMOSSOMO ; i++){
            novo.gene[i] = (a.gene[i] + b.gene[i]) / 2.0;
        }
        
        return novo;
    }
    
    // mutação do genótipo. para tentar escapar da solução subótima local
    public void mutacao(){
        double probabilidade;
        for (int i = 0 ; i < Configuracao.TAMANHO_CROMOSSOMO ; i++){
            probabilidade = Configuracao.R.nextDouble(true,true);
            if (probabilidade < Configuracao.MUTACAO){
                gene[i] += Configuracao.DESVIO_MUTACAO * Configuracao.R.nextGaussian();
            }
        }
    }
    
    @Override
    public String toString(){
        return Arrays.toString(gene) + ":" + fitness;
    }

    @Override
    public int compareTo(Genotipo outro) {
        if (this.fitness == outro.fitness)
            return 0;
        return this.fitness < outro.fitness ? 1 : -1;
    }
}