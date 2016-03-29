package pong;

import java.awt.Color;
import java.awt.Graphics;
import pong.Outros.Configuracao;

public class Raquete {

    private final int numero_raquete, largura = Configuracao.RAQUETE_LARGURA, altura = Configuracao.RAQUETE_ALTURA;
    private int x, y;

    private int score;

    public Raquete(Pong pong, int numero_raquete) {
        this.numero_raquete = numero_raquete;

        if (numero_raquete == 1) {
            this.x = 0;
        }

        if (numero_raquete == 2) {
            this.x = pong.largura - largura;
        }

        this.y = pong.altura / 2 - this.altura / 2;
    }
    
    public int getLargura() { return largura; }
    
    public int getAltura() { return altura; }
    
    public int getNumeroRaquete() { return numero_raquete; }
    
    public int getX() { return x; }
    
    public int getY() { return y; }
    
    public int getScore() { return score; }
    
    public void atualizaScore(){
        score++;
    }

    public void resetScore(){
        score = 0;
    }
    
    public void renderizarRaquete(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, largura, altura);
    }

    // implementação antiga, mais voltada pro jogador humano
    public void mover(boolean cima) {
        int velocidade = Configuracao.MAX_VELOCIDADE_RAQUETE;

        if (cima) {
            if (y - velocidade > 0) {
                y -= velocidade;
            } else {
                y = 0;
            }
        } else if (y + altura + velocidade < Pong.pong.altura) {
            y += velocidade;
        } else {
            y = Pong.pong.altura - altura;
        }
    }
    
    public int determinaLimite(int valor, int min, int max) {
        return Math.min(Math.max(valor, min), max);
    }
    
    // TESTAR
    public void mover(int velocidade) {
        this.y = determinaLimite(y, 0, Configuracao.ALTURA_TELA - this.altura);
        velocidade = determinaLimite(velocidade, -Configuracao.MAX_VELOCIDADE_RAQUETE, Configuracao.MAX_VELOCIDADE_RAQUETE);


        if (velocidade < 0) {
            if (y + velocidade > 0) {
                y += velocidade;
            } else {
                y = 0;
            }
        } else if (y + altura + velocidade < Pong.pong.altura) {
            y += velocidade;
        } else {
            y = Pong.pong.altura - altura;
        }
    }

}
