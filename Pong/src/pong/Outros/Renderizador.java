package pong.Outros;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import pong.Pong;

public class Renderizador extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dimension dimensao;
    
    public Renderizador(){
        dimensao = new Dimension(Configuracao.LARGURA_TELA, Configuracao.ALTURA_TELA);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Pong.pong.renderizarPong((Graphics2D) g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.dimensao;
    }

}
