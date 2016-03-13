package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import pong.Outros.Configuracao;

public class Bola {

    private int x, y, largura = Configuracao.BOLA_RAIO, altura = largura;

    private int movimentoX, movimentoY;

    private Pong pong;
    
    public final static Random R = new Random();

    private int quantidade_colisoes;

    public Bola(Pong pong) {
        this.pong = pong;

        criar();
    }
    
    // gets
    
    public int getY() { return y; }
    
    public int getX() { return x; }
    
    public int getAltura() { return altura; }
    
    public int getLargura() { return largura; }
    
    public int getMovimentoX() { return movimentoX; }
    
    public int getMovimentoY() { return movimentoY; }

    public void atualizarBola(Raquete raquete1, Raquete raquete2) {
        int velocidade = Configuracao.MAX_VELOCIDADE_BOLA;

        this.x += movimentoX * velocidade;
        this.y += movimentoY * velocidade;

        if (this.y + altura - movimentoY > pong.altura || this.y + movimentoY < 0) {
            if (this.movimentoY < 0) {
                this.y = 0;
                this.movimentoY = R.nextInt(4);

                if (movimentoY == 0) {
                    movimentoY = 1;
                }
            } else {
                this.movimentoY = -R.nextInt(4);
                this.y = pong.altura - altura;

                if (movimentoY == 0) {
                    movimentoY = -1;
                }
            }
        }

        if (verificaColisao(raquete1) == 1) {
            this.movimentoX = 1 + (quantidade_colisoes / 5);
            this.movimentoY = -2 + R.nextInt(4);

            if (movimentoY == 0) {
                movimentoY = 1;
            }
            
            //verificaGrau(raquete1);

            quantidade_colisoes++;
        } else if (verificaColisao(raquete2) == 1) {
            this.movimentoX = -1 - (quantidade_colisoes / 5);
            this.movimentoY = -2 + R.nextInt(4);
            

            if (movimentoY == 0) {
                movimentoY = 1;
            }
            
            //verificaGrau(raquete2);

            quantidade_colisoes++;
        }

        if (verificaColisao(raquete1) == 2) {
            raquete2.atualizaScore();
            criar();
        } else if (verificaColisao(raquete2) == 2) {
            raquete1.atualizaScore();
            criar();
        }
    }

    public void criar() {
        this.quantidade_colisoes = 0;
        this.x = pong.largura / 2 - this.largura / 2;
        this.y = pong.altura / 2 - this.altura / 2;

        this.movimentoY = -2 + R.nextInt(4);

        if (movimentoY == 0) {
            movimentoY = 1;
        }

        if (R.nextBoolean()) {
            movimentoX = 1;
        } else {
            movimentoX = -1;
        }
    }

    public int verificaColisao(Raquete raquete) {
        if (this.x < raquete.getX() + raquete.getLargura() && this.x + largura > raquete.getX() && this.y < raquete.getY() + raquete.getAltura() && this.y + altura > raquete.getY()) {
            return 1; //bounce
        } else if ((raquete.getX() > x && raquete.getNumeroRaquete() == 1) || (raquete.getX() < x - largura && raquete.getNumeroRaquete() == 2)) {
            return 2; //score
        }

        return 0; //nothing
    }
    
    // ao acertar a raquete, verificar a velocidade do movimento Y da bola
    public void verificaGrau(Raquete raquete){
        // quanto mais longe do centro da raquete, maior a velocidade Y
        int velocidade = this.y - raquete.getY() - raquete.getAltura() / 2;
        
        this.movimentoY += Configuracao.RAQUETE_INCLINACAO * velocidade;
    }

    public void renderizarBola(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, largura, altura);
    }

}
