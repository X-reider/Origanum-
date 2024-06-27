package com.mycompany.versione1.costruttore;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class ObliqueBotton extends JButton {
    
    public ObliqueBotton(String text) {
        super(text);
        setContentAreaFilled(false); // Evita la pittura di default del contenuto del bottone
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Imposta anti-aliasing per una grafica più liscia
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Definisci la forma del bottone
        Polygon p = new Polygon();
        p.addPoint(0, 10);
        p.addPoint(10, 0);
        p.addPoint(getWidth() - 1, 0);
        p.addPoint(getWidth() - 1, getHeight() - 10);
        p.addPoint(getWidth() - 10, getHeight() - 1);
        p.addPoint(0, getHeight() - 1);

        // Disegna la forma del bottone
        g2.setColor(getBackground());
        g2.fill(p);
        
        // Disegna il bordo del bottone
        g2.setColor(getForeground());
        g2.draw(p);
        
        // Disegna il testo del bottone
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(getText(), g2);
        int x = (getWidth() - (int) r.getWidth()) / 2;
        int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);
        
        g2.dispose();
        
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150, 50);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Oblique Button Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            
            JPanel panel = new JPanel();
            panel.add(new ObliqueBotton("Click Me"));

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
