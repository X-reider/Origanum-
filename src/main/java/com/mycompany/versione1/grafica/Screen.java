package com.mycompany.versione1.grafica;

import com.mycompany.versione1.costruttore.Frase;
import com.mycompany.versione1.costruttore.GameStatus;
import static com.mycompany.versione1.costruttore.GameStatus.loadImage;
import com.mycompany.versione1.costruttore.Paragrafo;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.DefaultCaret;

public class Screen extends RoundedPanel {

    public BufferedImage avantiI;
    public BufferedImage slotI;
    public BufferedImage craftI;
    public BufferedImage slotIB;
    public BufferedImage craftIB;
    public BufferedImage analizzaIB;
    public BufferedImage maniI;
    public BufferedImage analizzaI;
    public BufferedImage menuI;
    public BufferedImage indietroI;
    public int evidenziato = -1;
    public BufferedImage evidenziatoI;
    public boolean vecchio;

    private JTextPane paragrafo;
    private JScrollPane scrollPane;
    private JButton[] buttons;
    private JLabel imageLabel; // Nuovo JLabel per l'immagine
    private JPanel contentPanel; // Pannello per contenere immagine e testo

    public Screen(GameStatus game, CustomFrame f, boolean v) {
        super(20, new ImageIcon(Screen.class.getResource("/immagini/sfondi/sfondo.png")).getImage());
        setLayout(null); // Usa null layout per posizionamento assoluto

        vecchio = v;
        // lettura immagini bottoni
        analizzaIB = loadImage("/immagini/icone/analizzaB.png");
        slotIB = loadImage("/immagini/icone/icon1B.png");
        craftIB = loadImage("/immagini/icone/craftB.png");
        avantiI = loadImage("/immagini/icone/avanti.png");

        analizzaI = loadImage("/immagini/icone/analizza.png");
        slotI = loadImage("/immagini/icone/icon1.png");
        craftI = loadImage("/immagini/icone/craft.png");
        menuI = loadImage("/immagini/icone/menu.png");
        maniI = loadImage("/immagini/icone/mani.png");
        indietroI = loadImage("/immagini/icone/indietro.png");
        evidenziatoI = loadImage("/immagini/icone/glow.png");

        // Imposta JLabel per l'immagine
        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Imposta paragrafo
        paragrafo = new JTextPane();
        paragrafo.setOpaque(false); // Rendi trasparente
        paragrafo.setEditable(false); // Rendi non modificabile
        paragrafo.setContentType("text/html");
        paragrafo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Imposta contentPanel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio tra immagine e testo
        contentPanel.add(paragrafo);

        // Avvolgi il contentPanel in un JScrollPane
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null); // Rendi il bordo trasparente
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // Abilita la barra di scorrimento verticale

        // Imposta la velocità dello scorrimento
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Incrementa la velocità dello scorrimento
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    JScrollBar bar = scrollPane.getVerticalScrollBar();
                    int delta = e.getUnitsToScroll() * bar.getUnitIncrement();
                    bar.setValue(bar.getValue() + delta);
                }
            }
        });

        add(scrollPane);

        // Crea e aggiungi 9 bottoni 
        buttons = new JButton[11];
        for (int i = 0; i < 11; i++) {
            buttons[i] = new JButton();
            buttons[i].setFocusPainted(false);
            buttons[i].setMargin(new Insets(0, 0, 0, 0)); // Rimuovi i margini interni per l'icona
            buttons[i].setContentAreaFilled(false); // Rendi il bottone trasparente
            buttons[i].setBorderPainted(false); // Rendi il bordo trasparente
            add(buttons[i]);
        }

        // Aggiungi ActionListener ai bottoni 6=analizza
        for (int i = 0; i < 11; i++) {
            int index = i;
            buttons[i].addMouseListener(new MouseListener() {
                private boolean pressedInside = false;

                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressedInside = true;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (pressedInside) {
                        // tasto sinistro
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (index == 6 && game.selezionato < 6 && !vecchio) {
                                for (Paragrafo p : game.paragrafi.values()) {
                                    if ((game.inventario[6].equals(p.ricetta[0]) && (game.inventario[game.selezionato].equals(p.ricetta[1])))
                                            || (game.inventario[6].equals(p.ricetta[1]) && (game.inventario[game.selezionato].equals(p.ricetta[0]))))//potrebbe esserci un errore
                                    {
                                        game.vai(p.pagina, f);
                                    }
                                }
                                game.selezionato = 8; // Deseleziona craft
                            }
                            if (index == 8) {
                                indietro(game, f);
                            }
                            if (index == 9) {
                                avanti(game, f);
                            }
                            if (index == 10) {
                                game.save();
                                f.apri("menu");
                            } else if (index < 6 && !vecchio) {
                                game.selezionato = index;
                            }
                            ridimensiona(game, f);
                        } // tasto destro
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (index < 7 && game.selezionato < 7 && !vecchio) {
                                String temporaneo = game.inventario[index];
                                game.inventario[index] = game.inventario[game.selezionato];
                                game.inventario[game.selezionato] = temporaneo;
                            }
                        }
                        ridimensiona(game, f);
                        pressedInside = false;
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    evidenziato = index;
                    ridimensiona(game, f);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    pressedInside = false;
                    evidenziato = -1;
                    ridimensiona(game, f);
                }
            });
        }

        // Listener per ridimensionare i bottoni quando la finestra viene ridimensionata
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isShowing()) {
                    ridimensiona(game, f);
                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
                // Ensure initial positioning after panel is shown
                ridimensiona(game, f);
            }
        });
        setVisible(true);

        // Aggiungi un listener per aggiornare il paragrafo quando lo scrollPane viene ridimensionato
        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Aggiorna(game, f);
            }
        });
    }

    public void ridimensiona(GameStatus game, CustomFrame f) {
        int frameWidth = this.getWidth();
        int frameHeight = this.getHeight();
        double N = (double) frameHeight / frameWidth; // serve per normalizzare alcune misure

        double MARGINE_PARAGRAFO_ALTO = 0.07;
        double MARGINE_PARAGRAFO_BASSO = 0.08;
        double MARGINE_PARAGRAFO_SINISTRA = 0.18;
        double MARGINE_PARAGRAFO_DESTRA = 0.24;

        // cifre in percentuale
        double DIM_ANALIZZA = 10;
        double MARGINE_SINSTRO_INVENTARIO = 90;
        double MARGINE_ALTO_INVENTARIO = 10;
        double MARGINE_BASSO_INVENTARIO = 8;
        double DIMENSIONE_CRAFT = 15;
        double SPAZIATURA = 1;
        double MARGINE_SINISTRO_INDIETRO = 5;

        double dimSlot = (100 - MARGINE_BASSO_INVENTARIO - MARGINE_ALTO_INVENTARIO - DIMENSIONE_CRAFT - SPAZIATURA * 6) / (6);

        // RIDIMENSIONA IL CRAFT
        if (vecchio) {
            S.posiziona(buttons[6], craftIB, game.oggetti.get(game.visita.inventario[6]), DIMENSIONE_CRAFT, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO, this);
        } else if (evidenziato == 6) {
            S.posiziona(buttons[6], evidenziatoI, craftI, game.oggetti.get(game.inventario[6]), DIMENSIONE_CRAFT, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO, this);
        } else {
            S.posiziona(buttons[6], craftI, game.oggetti.get(game.inventario[6]), DIMENSIONE_CRAFT, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO, this);
        }

        // RIDIMENSIONA GLI SLOT
        for (int i = 0; i < 6; i++) {
            if (vecchio) {
                S.posiziona(buttons[i], slotIB, game.oggetti.get(game.visita.inventario[i]), dimSlot, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO + DIMENSIONE_CRAFT / 2 + (i + 1) * SPAZIATURA + (i + 0.5) * dimSlot, this);
            } else if (game.selezionato == i) {
                if (evidenziato == i) {
                    S.posiziona(buttons[i], evidenziatoI, maniI, game.oggetti.get(game.inventario[i]), dimSlot * 1.3, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO + DIMENSIONE_CRAFT / 2 + (i + 1) * SPAZIATURA + (i + 0.5) * dimSlot, this);
                } else {
                    S.posiziona(buttons[i], maniI, game.oggetti.get(game.inventario[i]), dimSlot * 1.3, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO + DIMENSIONE_CRAFT / 2 + (i + 1) * SPAZIATURA + (i + 0.5) * dimSlot, this);
                }
            } else {
                if (evidenziato == i) {
                    S.posiziona(buttons[i], evidenziatoI, slotI, game.oggetti.get(game.inventario[i]), dimSlot, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO + DIMENSIONE_CRAFT / 2 + (i + 1) * SPAZIATURA + (i + 0.5) * dimSlot, this);
                } else {
                    S.posiziona(buttons[i], slotI, game.oggetti.get(game.inventario[i]), dimSlot, MARGINE_SINSTRO_INVENTARIO, MARGINE_ALTO_INVENTARIO + DIMENSIONE_CRAFT / 2 + (i + 1) * SPAZIATURA + (i + 0.5) * dimSlot, this);
                }
            }
        }

        // RIDIMENSIONA BOTTONE ANALIZZA
        if (vecchio) {
            S.posiziona(buttons[7], analizzaIB, DIM_ANALIZZA, MARGINE_SINSTRO_INVENTARIO - N * (DIMENSIONE_CRAFT / 2 + DIM_ANALIZZA / 2) - SPAZIATURA, MARGINE_ALTO_INVENTARIO, this);
        } else if (evidenziato == 7) {
            S.posiziona(buttons[7], evidenziatoI, analizzaI, DIM_ANALIZZA, MARGINE_SINSTRO_INVENTARIO - N * (DIMENSIONE_CRAFT / 2 + DIM_ANALIZZA / 2) - SPAZIATURA, MARGINE_ALTO_INVENTARIO, this);
        } else {
            S.posiziona(buttons[7], analizzaI, DIM_ANALIZZA, MARGINE_SINSTRO_INVENTARIO - N * (DIMENSIONE_CRAFT / 2 + DIM_ANALIZZA / 2) - SPAZIATURA, MARGINE_ALTO_INVENTARIO, this);
        }

        // gestione indietro
        if (evidenziato == 8) {
            S.posiziona(buttons[8], evidenziatoI, indietroI, DIM_ANALIZZA, 5, 85, this);
        } else {
            S.posiziona(buttons[8], indietroI, DIM_ANALIZZA, 5, 85, this);
        }

        if (evidenziato == 9) {
            S.posiziona(buttons[9], evidenziatoI, avantiI, DIM_ANALIZZA, MARGINE_SINISTRO_INDIETRO + DIM_ANALIZZA + SPAZIATURA, 85, this);
        } else {
            S.posiziona(buttons[9], avantiI, DIM_ANALIZZA, MARGINE_SINISTRO_INDIETRO + DIM_ANALIZZA + SPAZIATURA, 85, this);
        }

        //regola quando viene mostrato il tasto indietro
        if (game.cronologia.hasPrevious()) {
            buttons[8].setVisible(true);
        } else {
            buttons[8].setVisible(false);
        }
        //regola quando viene mostrato il tasto avanti
        if (game.cronologia.hasNext()) {
            buttons[9].setVisible(true);
        } else {
            buttons[9].setVisible(false);
        }

        // gestione indietro
        if (evidenziato == 10) {
            S.posiziona(buttons[10], evidenziatoI, menuI, DIM_ANALIZZA, 5, 5, this);
        } else {
            S.posiziona(buttons[10], menuI, DIM_ANALIZZA, 5, 5, this);
        }

        // gestione paragrafi
        scrollPane.setBounds(
                (int) (frameWidth * MARGINE_PARAGRAFO_SINISTRA),
                (int) (frameHeight * MARGINE_PARAGRAFO_ALTO),
                (int) (frameWidth * (1 - MARGINE_PARAGRAFO_SINISTRA - MARGINE_PARAGRAFO_DESTRA)),
                (int) (frameHeight * (1 - MARGINE_PARAGRAFO_ALTO - MARGINE_PARAGRAFO_BASSO))
        );

        Aggiorna(game, f);
    }

    public void Aggiorna(GameStatus game, CustomFrame frame) {
        // Salva la posizione di scorrimento
        int scrollValue = scrollPane.getVerticalScrollBar().getValue();

        // gestisci la musica
        Paragrafo par;
        if (vecchio) {
            par = game.paragrafi.get(game.visita.paragrafoCorrente);
        } else {
            par = game.paragrafi.get(game.paragrafoCorrente);
        }

        game.mp3Player.playSong(game, par.traccia, par.inizio);

        // Rimuovi tutti gli hyperlink listener dal JTextPane
        for (HyperlinkListener listener : paragrafo.getHyperlinkListeners()) {
            paragrafo.removeHyperlinkListener(listener);
        }

        paragrafo.setContentType("text/html");
        paragrafo.setEditable(false);

        // Costruisci il contenuto HTML
        StringBuilder htmlContent = new StringBuilder("<html><body style='text-align: left; font-family: serif; user-select: none; -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none;'>");

        int i = 0;
        for (Frase f : par.frasi) {
            if (f.controlla(game)) {
                if (f.caratteristiche.contains("invio")) {
                    htmlContent.append("<br>");
                }
                if (f.caratteristiche.contains("doppio invio")) {
                    htmlContent.append("<br> <br>");
                }

                String linkId = Integer.toString(i);
                String testo = f.testo;
                if (f.caratteristiche.contains("corsivo")) {
                    testo = String.format("<i>%s</i>", testo);
                }
                String linkText = String.format("<a href='%s' style='text-decoration: none; font-size: 25px; color: #e0c496;'>%s</a>", linkId, testo);
                htmlContent.append(linkText);
            }
            i++;
        }
        htmlContent.append("</body></html>");
        paragrafo.setText(htmlContent.toString());

        // Aggiungi un HyperlinkListener per gestire i click sui link
        if (!vecchio) {
            paragrafo.addHyperlinkListener(e -> {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    int fraseCliccata = Integer.parseInt(e.getDescription());
                    if (par.frasi[fraseCliccata].destinazioni.get(game.inventario[game.selezionato]) != null) {
                        game.vai(par.frasi[fraseCliccata].destinazioni.get(game.inventario[game.selezionato]), frame);
                    } else if (par.frasi[fraseCliccata].destinazioni.get("") != null) {
                        game.vai(par.frasi[fraseCliccata].destinazioni.get(""), frame);
                    }

                }
            });
        }

        paragrafo.setPreferredSize(new Dimension(scrollPane.getWidth(), 2200));

        // Aggiorna l'immagine
        if (par.immagine != null && !par.immagine.isEmpty()) {
            BufferedImage image = game.immagini.get(par.immagine);
            ImageIcon imageIcon;
            if (image != null) {
                imageIcon = new ImageIcon(getScaledImage(image, scrollPane.getWidth()));
            } else {
                imageIcon = null;
            }
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setIcon(null); // Rimuovi l'immagine se non presente
        }

        // Impedisci la selezione del testo
        paragrafo.setHighlighter(null);
        DefaultCaret noCaret = new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
                // Non dipinge il caret
            }
        };
        noCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        paragrafo.setCaret(noCaret);

        // Ripristina la posizione di scorrimento
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollValue));
    }

    public static void indietro(GameStatus game, CustomFrame f) {
        game.visita = game.cronologia.previous();
        f.apri("ScreenVecchio");
    }

    public static void avanti(GameStatus game, CustomFrame f) {
        game.visita = game.cronologia.next();
        if (game.cronologia.hasNext()) {
            f.apri("ScreenVecchio");
        } else {
            f.apri("Screen");
        }
    }

    private Image getScaledImage(BufferedImage src, int width) {
        int originalWidth = src.getWidth();
        int originalHeight = src.getHeight();
        int newWidth = width;
        int newHeight = (originalHeight * width) / originalWidth;

        Image scaledImage = src.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return scaledImage;
    }
}
