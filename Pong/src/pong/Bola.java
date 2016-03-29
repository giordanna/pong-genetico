package pong;

import java.awt.Color;
import java.awt.Graphics;
import pong.Outros.Configuracao;

public class Bola {

    private int x, y, largura = Configuracao.BOLA_RAIO, altura = largura;

    private int movimentoX, movimentoY;

    private Pong pong;

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

    public int atualizarBola(Raquete raquete1, Raquete raquete2) {
        int velocidade = Configuracao.MAX_VELOCIDADE_BOLA;

        this.x += movimentoX * velocidade;
        this.y += movimentoY * velocidade;

        // colisão com as paredes
        if (this.y + altura - movimentoY > pong.altura || this.y + movimentoY < 0) {
            // parede de cima
            if (this.movimentoY < 0) {
                this.y = 0;
                //this.movimentoY = R.nextInt(4);
                this.movimentoY = - this.movimentoY;

                if (movimentoY == 0) {
                    movimentoY = 1;
                }
            // parede de baixo
            } else {
                //this.movimentoY = -R.nextInt(4);
                this.y = pong.altura - altura;
                this.movimentoY = - this.movimentoY;
                if (movimentoY == 0) {
                    movimentoY = -1;
                }
            }
        }
        // colisão com raquete da esquerda
        if (verificaColisao(raquete1) == 1) {
            this.movimentoX = 1 + (quantidade_colisoes / 5);
            //this.movimentoY = -2 + R.nextInt(4);
            
            verificaGrau(raquete1);

            quantidade_colisoes++;
        // colisão com raquete da direita
        } else if (verificaColisao(raquete2) == 1) {
            this.movimentoX = -1 - (quantidade_colisoes / 5);
            //this.movimentoY = -2 + R.nextInt(4);
            
            verificaGrau(raquete2);

            quantidade_colisoes++;
        }

        if (verificaColisao(raquete1) == 2) {
            raquete2.atualizaScore();
            criar();
            return -1; // jogador 2 marcou ponto
        } else if (verificaColisao(raquete2) == 2) {
            raquete1.atualizaScore();
            criar();
            return 1; // jogador 1 marcou ponto
        }
        
        return 0;
    }

    public void criar() {
        this.quantidade_colisoes = 0;
        this.x = pong.largura / 2 - this.largura / 2;
        this.y = pong.altura / 2 - this.altura / 2;

        this.movimentoY = -2 + Configuracao.R.nextInt(4);

        if (movimentoY == 0) {
            movimentoY = 1;
        }

        if (Configuracao.R.nextBoolean()) {
            movimentoX = 1;
        } else {
            movimentoX = -1;
        }
    }

    public int verificaColisao(Raquete raquete) {
        if (this.x < raquete.getX() + raquete.getLargura() && this.x + largura > raquete.getX() && this.y < raquete.getY() + raquete.getAltura() && this.y + altura > raquete.getY()) {
            return 1; //rebate
        } else if ((raquete.getX() > x && raquete.getNumeroRaquete() == 1) || (raquete.getX() < x - largura && raquete.getNumeroRaquete() == 2)) {
            return 2; //ponto
        }

        return 0; //nada
    }
    
    // ao acertar a raquete, verificar a velocidade do movimento Y da bola
    public void verificaGrau(Raquete raquete){
        // quanto mais longe do centro da raquete, maior a velocidade Y
        int velocidade = this.y - raquete.getY() - raquete.getAltura() / 2;
        velocidade = converterRange(-75,75,-Configuracao.RAQUETE_INCLINACAO,Configuracao.RAQUETE_INCLINACAO,velocidade);
        if (velocidade == 0) {
                velocidade = Configuracao.R.nextInt(2);
                if (velocidade == 0) velocidade = -1;
            }
        if (movimentoY < 0 ){
            if (velocidade < 0) movimentoY = velocidade;
            else movimentoY = -velocidade;
        }
        else{
            if (velocidade > 0) movimentoY = velocidade;
            else movimentoY = -velocidade;
        }
    }

    public void renderizarBola(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, largura, altura);
    }
    
    

    public static int converterRange( int inicio_original, int final_original,
            int inicio_novo, int final_novo,int valor) {
        double escala = (double)(final_novo - inicio_novo) / (final_original - inicio_original);
        return (int)(inicio_novo + ((valor - inicio_original) * escala));
    }



}
