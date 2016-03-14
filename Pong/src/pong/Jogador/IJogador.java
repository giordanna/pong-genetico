package pong.Jogador;

import pong.Bola;
import pong.Raquete;

public interface IJogador {

    // verifica a direção que a raquete deve se mover
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola);

    // método chamado quando alguém faz uma pontuação
    // ponto = 1 - jogador fez o ponto. ponto = -1 - oponente fez o ponto
    public void resultado(int ponto);

}
