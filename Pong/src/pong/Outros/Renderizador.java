package pong.Outros;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import pong.Pong;

public class Renderizador extends JPanel {

    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Pong.pong.renderizarPong((Graphics2D) g);
    }

}
