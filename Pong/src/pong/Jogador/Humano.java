package pong.Jogador;

import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class Humano implements IJogador {
    
    // verificar se vai funcionar
    private int cima, baixo;
    
    public Humano(int cima, int baixo){
        this.cima = cima;
        this.baixo = baixo;
    }
    
    // não será usado de fato
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        return Configuracao.MAX_VELOCIDADE_RAQUETE;
    }

    @Override
    public void resultado(int ponto) {}
  
}
