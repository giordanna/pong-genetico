package pong.Jogador;

import java.awt.event.KeyEvent;
import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class Humano implements IJogador {
    
    // verificar se vai funcionar
    private int cima, baixo;
    private KeyEvent tecla;
    
    public Humano(KeyEvent tecla, int cima, int baixo){
        this.cima = cima;
        this.baixo = baixo;
        this.tecla = tecla;
    }
    
    @Override
    public int calculaVelocidade(Raquete minha, Raquete oponente, Bola bola) {
        int movimento = 0;
        int id = tecla.getKeyCode();
        
        if (id == cima){
            movimento = -Configuracao.MAX_VELOCIDADE_RAQUETE;
        }
        if (id == baixo){
            movimento = Configuracao.MAX_VELOCIDADE_RAQUETE;
        }
        
        return movimento;
    }

    @Override
    public void resultado(int ponto) {}
  
}
