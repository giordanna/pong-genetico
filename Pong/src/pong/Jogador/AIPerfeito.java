package pong.Jogador;

// faz previsão da posição da bola

import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class AIPerfeito implements IJogador {
    

    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        
        int bola_y = bola.getY();
        int bola_vel_x = Math.abs(bola.getMovimentoX());
        
        if (minha.getX() < Configuracao.LARGURA_TELA / 2)
            bola_vel_x *= -1;
        
        // a física em si
        if (bola.getMovimentoX() != 0)
            bola_y = (bola.getMovimentoY() / bola_vel_x) * (minha.getX() - bola.getX()) + bola.getY();
        
        // acha o verdadeiro y da bola pelo fato que acerta as paredes
        while (bola_y < 0 || bola_y > Configuracao.ALTURA_TELA){
            if (bola_y < 0)
                bola_y = Math.abs(bola_y);
            else
                bola_y = Configuracao.ALTURA_TELA - (bola_y % Configuracao.ALTURA_TELA);
        }

        return Configuracao.MAX_VELOCIDADE_RAQUETE * 
                ((bola_y - minha.getY() - minha.getAltura() / 2) < 0 ? -1 : 1);
    }

    @Override
    public void resultado(int ponto) {}
   
}
