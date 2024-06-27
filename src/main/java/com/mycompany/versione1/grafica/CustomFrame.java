package com.mycompany.versione1.grafica;

import com.mycompany.versione1.costruttore.GameStatus;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomFrame extends JFrame {

    public JPanel titleBar;
    public int mouseX, mouseY;
    public RoundedPanel contentPanel;
    public boolean isFullscreen = false;
    public Rectangle originalBounds;
    public CardLayout cardLayout;
    public RoundedPanel mainPanel;

    public void apri(String pannello){
        cardLayout.show(contentPanel, "ricarica");
        cardLayout.show(contentPanel, pannello);
    }
    
    
    public CustomFrame(GameStatus game) {
        setLayout(new BorderLayout());
        setSize(800, 600); // Set initial size of the frame
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        String backgroundImagePath = "/immagini/sfondi/sfondo.png";   
        Image backgroundImage = new ImageIcon(getClass().getResource(backgroundImagePath)).getImage();
        
        // Imposta il frame come non decorato
        setUndecorated(true);
        // Imposta il frame come trasparente
        setBackground(new Color(0, 0, 0, 0));
        // Imposta il layout del contenuto
        setLayout(new BorderLayout());
        // Colore personalizzato
        Color customColor = new Color(105, 105, 105); // RGB per DARK_GRAY

        // Crea un pannello personalizzato per gestire i bordi arrotondati e il bordo scuro
        mainPanel = new RoundedPanel(20, new Color(47, 38, 32));
        mainPanel.setLayout(new BorderLayout(0, 0)); // Nessuno spazio tra i pannelli
        mainPanel.setBackground(new Color(105, 105, 105, 255)); // Colore di sfondo senza trasparenza

        // Aggiungi il pannello della barra del titolo personalizzata
        titleBar = new RoundedPanel(20, new Color(47, 38, 32)); 
        titleBar.setOpaque(false);
        titleBar.setBackground(customColor);
        titleBar.setPreferredSize(new Dimension(800, 30));
        titleBar.setLayout(new BorderLayout());
        // Aggiungi un'etichetta per il titolo
        JLabel titleLabel = new JLabel("Origanum");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titleBar.add(titleLabel, BorderLayout.WEST);
        // Aggiungi i pulsanti di controllo
        JButton closeButton = new JButton("X");
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));
        
        JButton fullscreenButton = new JButton("⬜");
        fullscreenButton.setFocusPainted(false);
        fullscreenButton.setBorderPainted(false);
        fullscreenButton.setContentAreaFilled(false);
        fullscreenButton.setForeground(Color.WHITE);
        fullscreenButton.addActionListener(e -> toggleFullscreen());
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(fullscreenButton);
        buttonsPanel.add(closeButton);
        titleBar.add(buttonsPanel, BorderLayout.EAST);

        // Aggiungi la gestione del drag per spostare il frame
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });
        // Aggiungi la barra del titolo al frame
        mainPanel.add(titleBar, BorderLayout.NORTH);
    
        // Inizializza il CardLayout e il contentPanel
        cardLayout = new CardLayout();
        contentPanel = new RoundedPanel(cardLayout, 20);
        // Aggiungi vari RoundedPanel al contentPanel usando il CardLayout
        RoundedPanel ricarica = new RoundedPanel(20);  
            contentPanel.add(ricarica, "ricarica");
        RoundedPanel screen = new Screen(game, this, false);   
            contentPanel.add(screen, "Screen");
        RoundedPanel menu = new Menu(game, this);
            contentPanel.add(menu, "menu");
        RoundedPanel screenVecchio = new Screen(game, this, true);   
            contentPanel.add(screenVecchio, "ScreenVecchio");     
        RoundedPanel Credits = new Credits(this);   
            contentPanel.add(Credits, "credits");   
        RoundedPanel Update = new Update(this);   
            contentPanel.add(Update, "update");  
        // Aggiungi il contentPanel al mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Aggiungi il pannello principale al frame
        add(mainPanel, BorderLayout.CENTER);
        
        // Mostra il primo pannello

        // Imposta il ridimensionamento manuale
        setMinimumSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(800, 600));

        // Aggiungi il listener per il ridimensionamento
        ResizeListener resizeListener = new ResizeListener(this);
        addMouseListener(resizeListener);
        addMouseMotionListener(resizeListener);

        // Aggiungi il listener per il tasto ESC
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isFullscreen && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    toggleFullscreen();
                }
            }
        });

        // Imposta il frame visibile per ricevere i key event
        setFocusable(true);
        requestFocusInWindow();

        // Ridimensiona il frame per adattarsi ai suoi componenti
        pack();        
        apri("menu");        
        setVisible(true);

    }

    private void toggleFullscreen() {
        if (!isFullscreen) {
            originalBounds = getBounds();
            dispose();
            setUndecorated(true);
            setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
            contentPanel.setBounds(getBounds());
            contentPanel.setVisible(true);
            titleBar.setVisible(false);
            contentPanel.setCornerRadius(0);  // Rimuovi bordi arrotondati
            isFullscreen = true;
        } else {
            dispose();
            setUndecorated(true);
            setBounds(originalBounds);
            titleBar.setVisible(true);
            contentPanel.setCornerRadius(20);  // Ripristina bordi arrotondati
            isFullscreen = false;
        }
        setVisible(true);
    }

    // Listener per gestire il ridimensionamento del frame
    class ResizeListener extends MouseAdapter {
        private static final int BORDER_DRAG_THICKNESS = 5;
        private static final int MIN_WIDTH = 400;
        private static final int MIN_HEIGHT = 300;
        private final CustomFrame frame;
        private int cursor;
        private Point startPos;
        private Rectangle startBounds;

        public ResizeListener(CustomFrame frame) {
            this.frame = frame;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Insets insets = frame.getInsets();
            int x = e.getX();
            int y = e.getY();
            int width = frame.getWidth();
            int height = frame.getHeight();

            if (x < BORDER_DRAG_THICKNESS && y < BORDER_DRAG_THICKNESS) {
                cursor = Cursor.NW_RESIZE_CURSOR;
            } else if (x < BORDER_DRAG_THICKNESS && y > height - BORDER_DRAG_THICKNESS) {
                cursor = Cursor.SW_RESIZE_CURSOR;
            } else if (x > width - BORDER_DRAG_THICKNESS && y < BORDER_DRAG_THICKNESS) {
                cursor = Cursor.NE_RESIZE_CURSOR;
            } else if (x > width - BORDER_DRAG_THICKNESS && y > height - BORDER_DRAG_THICKNESS) {
                cursor = Cursor.SE_RESIZE_CURSOR;
            } else if (x < BORDER_DRAG_THICKNESS) {
                cursor = Cursor.W_RESIZE_CURSOR;
            } else if (x > width - BORDER_DRAG_THICKNESS) {
                cursor = Cursor.E_RESIZE_CURSOR;
            } else if (y < BORDER_DRAG_THICKNESS) {
                cursor = Cursor.N_RESIZE_CURSOR;
            } else if (y > height - BORDER_DRAG_THICKNESS) {
                cursor = Cursor.S_RESIZE_CURSOR;
            } else {
                cursor = Cursor.DEFAULT_CURSOR;
            }
            frame.setCursor(Cursor.getPredefinedCursor(cursor));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startPos = e.getPoint();
            startBounds = frame.getBounds();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (cursor != Cursor.DEFAULT_CURSOR) {
                int dx = e.getX() - startPos.x;
                int dy = e.getY() - startPos.y;
                Rectangle bounds = new Rectangle(startBounds);

                switch (cursor) {
                    case Cursor.NW_RESIZE_CURSOR -> {
                        bounds.x += dx;
                        bounds.y += dy;
                        bounds.width -= dx;
                        bounds.height -= dy;
                    }
                    case Cursor.N_RESIZE_CURSOR -> {
                        bounds.y += dy;
                        bounds.height -= dy;
                    }
                    case Cursor.NE_RESIZE_CURSOR -> {
                        bounds.y += dy;
                        bounds.width += dx;
                        bounds.height -= dy;
                    }
                    case Cursor.W_RESIZE_CURSOR -> {
                        bounds.x += dx;
                        bounds.width -= dx;
                    }
                    case Cursor.E_RESIZE_CURSOR -> bounds.width += dx;
                    case Cursor.SW_RESIZE_CURSOR -> {
                        bounds.x += dx;
                        bounds.width -= dx;
                        bounds.height += dy;
                    }
                    case Cursor.S_RESIZE_CURSOR -> bounds.height += dy;
                    case Cursor.SE_RESIZE_CURSOR -> {
                        bounds.width += dx;
                        bounds.height += dy;
                    }
                }

                // Assicurati che le dimensioni minime siano rispettate
                bounds.width = Math.max(bounds.width, MIN_WIDTH);
                bounds.height = Math.max(bounds.height, MIN_HEIGHT);
                remove(mainPanel);
                add(mainPanel, BorderLayout.CENTER);


                frame.setBounds(bounds);
                frame.revalidate();
                frame.repaint();
                 }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            frame.setCursor(Cursor.getDefaultCursor());
        }
    }
}

