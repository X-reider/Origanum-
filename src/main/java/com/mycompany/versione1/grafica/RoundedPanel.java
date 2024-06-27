package com.mycompany.versione1.grafica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color borderColor;
    private Image backgroundImage;
  

    public RoundedPanel(int radius, Image sfondo) {
        super();
        this.cornerRadius = radius;
        
        backgroundImage = sfondo;
        setOpaque(false);
    }
    
       public RoundedPanel(int radius, Color colore) {
        super();
        this.cornerRadius = radius;
        this.borderColor = colore;
        backgroundImage = null;
        setOpaque(false);
    }

    // Costruttore che accetta solo il layout manager
    public RoundedPanel(LayoutManager layout, int radius) {
        super(layout);
        this.cornerRadius = radius;
        this.borderColor = Color.BLACK;
        setOpaque(false);
    }

    // Costruttore che accetta il layout manager, il raggio e il colore del bordo
    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
    }
    
      // Costruttore che accetta il layout manager, il raggio e il colore del bordo
    public RoundedPanel(LayoutManager layout, int radius, Color borderColor) {
        super(layout);
        this.cornerRadius = radius;
        this.borderColor = borderColor;
        setOpaque(false);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

       @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Migliora la qualità del rendering
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Crea un'area di clip arrotondata
        Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setClip(clip);

        // Disegna l'immagine di sfondo o il colore di sfondo
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(borderColor);
            g2.fill(clip);
        }

        // Ripristina il clip per evitare che influenzi il disegno successivo
        g2.setClip(null);

        // Disegna il bordo arrotondato
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // Override per gestire la pittura del bordo
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius); // Disegna il bordo scuro
    }
}
