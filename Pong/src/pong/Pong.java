package pong;

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

public class Pong implements ActionListener, KeyListener {

    public static Pong pong;
    
    public final static Random R = new Random();

    public int largura = 700, altura = 700;

    public Renderizador renderizador;

    public Raquete jogador1, jogador2;

    public Bola bola;

    public boolean ai = false, seleciona_dificuldade;

    public boolean w, s, cima, baixo;

    public int status_jogo = 0, limite_score = 7, jogador_venceu; //0 = Menu, 1 = Paused, 2 = Playing, 3 = Over

    public int dificuldade_ai, movimentos_ai, ai_cooldown = 0;

    public JFrame jframe;

    public Pong() {
        
        Timer timer = new Timer(20, this);

        jframe = new JFrame("Pong");

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
        jogador1 = new Raquete(this, 1);
        jogador2 = new Raquete(this, 2);
        bola = new Bola(this);
    }

    public void atualizarPong() {
        if (jogador1.getScore() >= limite_score) {
            jogador_venceu = 1;
            status_jogo = 3;
        }

        if (jogador2.getScore() >= limite_score) {
            status_jogo = 3;
            jogador_venceu = 2;
        }

        if (w) {
            jogador1.mover(true);
        }
        if (s) {
            jogador1.mover(false);
        }

        if (!ai) {
            if (cima) {
                jogador2.mover(true);
            }
            if (baixo) {
                jogador2.mover(false);
            }
        } else {
            if (ai_cooldown > 0) {
                ai_cooldown--;

                if (ai_cooldown == 0) {
                    movimentos_ai = 0;
                }
            }

            if (movimentos_ai < 10) {
                if (jogador2.getY() + jogador2.getAltura() / 2 < bola.getY()) {
                    jogador2.mover(false);
                    movimentos_ai++;
                }

                if (jogador2.getY() + jogador2.getAltura() / 2 > bola.getY()) {
                    jogador2.mover(true);
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

        bola.atualizarBola(jogador1, jogador2);
    }

    public void renderizarPong(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, largura, altura);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (status_jogo == 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));

            g.drawString("PONG", largura / 2 - 75, 50);

            if (!seleciona_dificuldade) {
                g.setFont(new Font("Arial", 1, 30));

                g.drawString("Press Space to Play", largura / 2 - 150, altura / 2 - 25);
                g.drawString("Press Shift to Play with Bot", largura / 2 - 200, altura / 2 + 25);
                g.drawString("<< Score Limit: " + limite_score + " >>", largura / 2 - 150, altura / 2 + 75);
            }
        }

        if (seleciona_dificuldade) {
            String string = dificuldade_ai == 0 ? "Easy" : (dificuldade_ai == 1 ? "Medium" : "Hard");

            g.setFont(new Font("Arial", 1, 30));

            g.drawString("<< Bot Difficulty: " + string + " >>", largura / 2 - 180, altura / 2 - 25);
            g.drawString("Press Space to Play", largura / 2 - 150, altura / 2 + 25);
        }

        if (status_jogo == 1) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PAUSED", largura / 2 - 103, altura / 2 - 25);
        }

        if (status_jogo == 1 || status_jogo == 2) {
            g.setColor(Color.WHITE);

            g.setStroke(new BasicStroke(5f));

            g.drawLine(largura / 2, 0, largura / 2, altura);

            g.setStroke(new BasicStroke(2f));

            g.drawOval(largura / 2 - 150, altura / 2 - 150, 300, 300);

            g.setFont(new Font("Arial", 1, 50));

            g.drawString(String.valueOf(jogador1.getScore()), largura / 2 - 90, 50);
            g.drawString(String.valueOf(jogador2.getScore()), largura / 2 + 65, 50);

            jogador1.renderizarRaquete(g);
            jogador2.renderizarRaquete(g);
            bola.renderizarBola(g);
        }

        if (status_jogo == 3) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));

            g.drawString("PONG", largura / 2 - 75, 50);

            if (ai && jogador_venceu == 2) {
                g.drawString("The Bot Wins!", largura / 2 - 170, 200);
            } else {
                g.drawString("Player " + jogador_venceu + " Wins!", largura / 2 - 165, 200);
            }

            g.setFont(new Font("Arial", 1, 30));

            g.drawString("Press Space to Play Again", largura / 2 - 185, altura / 2 - 25);
            g.drawString("Press ESC for Menu", largura / 2 - 140, altura / 2 + 25);
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
    public void keyTyped(KeyEvent e) {

    }
}
