package pong.Jogador;

// AQUI onde treina 30 genótipos
// to do: fazer receber jogadores, e não genótipos

import java.util.Arrays;
import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class Treinador implements IJogador {
    private static int MAIOR_FITNESS = 9999;
    
    private Genotipo populacao[];
    private double intervalo;
    private boolean bola_passou;
    private int contraatacou;
    private int atual;
    private int ultima_distancia;
    private int posicao_inicial = Integer.MAX_VALUE;
    
    public Treinador(){
        intervalo = Configuracao.INTERVALO_GENES;
        bola_passou = false;
        contraatacou = 0;
        atual = 0;
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
    public double calculaVelocidade(Raquete minha, Raquete oponente, Bola bola){
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
            fitness = MAIOR_FITNESS;
        
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
            salvaPopulacao();
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
    
    public void salvaPopulacao(){
        // to do salvar num(s) arquivo(s)
    }
    
    // to do rever
    public void repopularPopulacao(){
        // ordena a população para os melhores
        Arrays.sort(populacao);
        
        // substuituir uma porcentagem da população (padrão = 75%);
        int substituir = (int) (Configuracao.MAX_POPULACAO * Configuracao.PORCENTAGEM_SUBSTITUIR);
        
        // caso coloquem valores muito baixos na porcentagem
        if (substituir == 0) substituir = 1;
        
        int j = 0;
        int outro;
        
        // insere nos 3/4 da população os filhos gerados pelos melhores + individuo aleatório
        // to do: refazer
        for (int i = Configuracao.MAX_POPULACAO - substituir ; i < Configuracao.MAX_POPULACAO ; i++){
            // para que não escolha o mesmo indivíduo
            while (true){
                outro = Configuracao.R.nextInt(Configuracao.MAX_POPULACAO);
                if (j != outro) break;
            }
            populacao[i] = new Genotipo(Genotipo.crossover(populacao[j], populacao[outro]));
            // realiza a mutação
            populacao[i].mutacao();
            j++;
        }
    }
}