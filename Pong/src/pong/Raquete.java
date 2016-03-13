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

    public void renderizarRaquete(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, largura, altura);
    }

    public void mover(boolean cima) {
        int velocidade = 15;

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

}
