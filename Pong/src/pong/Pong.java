package pong;

// FALTA implementar de fato as outras classes. ainda é só um pong normal
// o jogo em si

import pong.Outros.Renderizador;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;
import pong.Jogador.IJogador;
import pong.Outros.Configuracao;

public class Pong implements ActionListener, KeyListener {

    public static Pong pong;
    
    public final static Random R = new Random();

    public int largura = Configuracao.LARGURA_TELA, altura = Configuracao.ALTURA_TELA;

    public Renderizador renderizador;

    public Raquete raquete1, raquete2; // independentes
    
    public IJogador jogador1, jogador2;

    public Bola bola;

    public boolean ai = false, seleciona_dificuldade;

    public boolean w, s, cima, baixo;

    public int status_jogo = 0, limite_score = 7, jogador_venceu; //0 = Menu, 1 = Paused, 2 = Playing, 3 = Over

    public int dificuldade_ai, movimentos_ai, ai_cooldown = 0;

    public JFrame jframe;

    public Pong() {
        
        Timer timer = new Timer(20, this);

        jframe = new JFrame("Pong Genético");

        renderizador = new Renderizador();

        jframe.setSize(largura + 15, altura + 35);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderizador);
        jframe.addKeyListener(this);

        timer.start();
    }

    public void inicializarPong() {
        status_jogo = 2;
        raquete1 = new Raquete(this, 1);
        raquete2 = new Raquete(this, 2);
        bola = new Bola(this);
    }

    public void atualizarPong() {
        if (raquete1.getScore() >= limite_score) {
            jogador_venceu = 1;
            status_jogo = 3;
        }

        if (raquete2.getScore() >= limite_score) {
            status_jogo = 3;
            jogador_venceu = 2;
        }

        if (w) {
            raquete1.mover(true);
        }
        if (s) {
            raquete1.mover(false);
        }

        if (!ai) {
            if (cima) {
                raquete2.mover(true);
            }
            if (baixo) {
                raquete2.mover(false);
            }
        } else {
            if (ai_cooldown > 0) {
                ai_cooldown--;

                if (ai_cooldown == 0) {
                    movimentos_ai = 0;
                }
            }

            if (movimentos_ai < 10) {
                if (raquete2.getY() + raquete2.getAltura() / 2 < bola.getY()) {
                    raquete2.mover(false);
                    movimentos_ai++;
                }

                if (raquete2.getY() + raquete2.getAltura() / 2 > bola.getY()) {
                    raquete2.mover(true);
                    movimentos_ai++;
                }

                if (dificuldade_ai == 0) {
                    ai_cooldown = 20;
                }
                if (dificuldade_ai == 1) {
                    ai_cooldown = 15;
                }
                if (dificuldade_ai == 2) {
                    ai_cooldown = 10;
                }
            }
        }

        bola.atualizarBola(raquete1, raquete2);
    }
    
    // escreve o texto centralizado
    private void escreveTexto(Graphics2D g, String texto, int x, int y){
        int comprimento = (int)
            g.getFontMetrics().getStringBounds(texto, g).getWidth();
        int inicio = largura/2 - comprimento/2;
        g.drawString(texto, inicio + x, y);
 }

    public void renderizarPong(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, largura, altura);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (status_jogo == 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Configuracao.FONTE, 1, 50));
            escreveTexto(g, "PONG GENÉTICO", 0, 50);

            if (!seleciona_dificuldade) {
                g.setFont(new Font(Configuracao.FONTE, 1, 30));
                escreveTexto(g, "Pressione Espaço para jogar 2 pessoas", 0, altura / 2 - 25);
                escreveTexto(g, "Pressione shift para jogar com AI", 0, altura / 2 + 25);
                escreveTexto(g, "<< Limite de pontos: " + limite_score + " >>", 0, altura / 2 + 75);
            }
        }

        if (seleciona_dificuldade) {
            String string = dificuldade_ai == 0 ? "Fácil" : (dificuldade_ai == 1 ? "Médio" : "Difícil");

            g.setFont(new Font(Configuracao.FONTE, 1, 30));
            escreveTexto(g, "<< Dificuldade AI: " + string + " >>" , 0, altura / 2 - 25);
            escreveTexto(g, "Pressione Espaço para jogar" , 0, altura / 2 + 25);
        }

        if (status_jogo == 1) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Configuracao.FONTE, 1, 50));
            escreveTexto(g, "PAUSA", 0, altura / 2);
        }

        if (status_jogo == 1 || status_jogo == 2) {
            g.setColor(Color.WHITE);

            g.setStroke(new BasicStroke(5f));

            g.drawLine(largura / 2, 0, largura / 2, altura);

            g.setStroke(new BasicStroke(5f));

            g.drawOval(largura / 2 - 150, altura / 2 - 150, 300, 300);

            g.setFont(new Font(Configuracao.FONTE, 1, 50));

            g.drawString(String.valueOf(raquete1.getScore()), largura / 2 - 90, 50);
            g.drawString(String.valueOf(raquete2.getScore()), largura / 2 + 65, 50);

            raquete1.renderizarRaquete(g);
            raquete2.renderizarRaquete(g);
            bola.renderizarBola(g);
        }

        if (status_jogo == 3) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(Configuracao.FONTE, 1, 50));
            
            escreveTexto(g, "PONG", 0, 50);

            if (ai && jogador_venceu == 2) {
                escreveTexto(g, "O AI ganhou!", 0, altura / 2);
            } else {
                escreveTexto(g, "Jogador " + jogador_venceu + " ganhou!", 0, altura / 2); // ou 200
            }

            g.setFont(new Font(Configuracao.FONTE, 1, 30));
            escreveTexto(g, "Pressione Espaço para jogar de novo", 0, altura - 75);
            escreveTexto(g, "Pressione ESC para retornar ao Menu", 0, altura - 25);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (status_jogo == 2) {
            atualizarPong();
        }

        renderizador.repaint();
    }

    public static void main(String[] args) {
        pong = new Pong();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) {
            cima = true;
        } else if (id == KeyEvent.VK_DOWN) {
            baixo = true;
        } else if (id == KeyEvent.VK_RIGHT) {
            if (seleciona_dificuldade) {
                if (dificuldade_ai < 2) {
                    dificuldade_ai++;
                } else {
                    dificuldade_ai = 0;
                }
            } else if (status_jogo == 0) {
                limite_score++;
            }
        } else if (id == KeyEvent.VK_LEFT) {
            if (seleciona_dificuldade) {
                if (dificuldade_ai > 0) {
                    dificuldade_ai--;
                } else {
                    dificuldade_ai = 2;
                }
            } else if (status_jogo == 0 && limite_score > 1) {
                limite_score--;
            }
        } else if (id == KeyEvent.VK_ESCAPE && (status_jogo == 2 || status_jogo == 3)) {
            status_jogo = 0;
        } else if (id == KeyEvent.VK_SHIFT && status_jogo == 0) {
            ai = true;
            seleciona_dificuldade = true;
        } else if (id == KeyEvent.VK_SPACE) {
            if (status_jogo == 0 || status_jogo == 3) {
                if (!seleciona_dificuldade) {
                    ai = false;
                } else {
                    seleciona_dificuldade = false;
                }

                inicializarPong();
            } else if (status_jogo == 1) {
                status_jogo = 2;
            } else if (status_jogo == 2) {
                status_jogo = 1;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = false;
        } else if (id == KeyEvent.VK_S) {
            s = false;
        } else if (id == KeyEvent.VK_UP) {
            cima = false;
        } else if (id == KeyEvent.VK_DOWN) {
            baixo = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
