package pong.Jogador;

// verifica apenas a posição Y da bola

import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class AIBasico implements IJogador {

    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        return Configuracao.MAX_VELOCIDADE_RAQUETE * 
                ((bola.getY() - minha.getY() - minha.getAltura() / 2) < 0 ? -1 : 1);
    }

    @Override
    public void resultado(int ponto) {}
    
}

