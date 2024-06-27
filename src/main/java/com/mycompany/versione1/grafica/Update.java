package com.mycompany.versione1.grafica;

import static com.mycompany.versione1.costruttore.GameStatus.loadImage;
import static com.mycompany.versione1.costruttore.UpdateCheck.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;

public class Update extends RoundedPanel {

    private JEditorPane testo;
    private JButton button;
    private BufferedImage glowI;

    public BufferedImage indietroI;
    public boolean evidenziato = false;

    public Update(CustomFrame f) {
        super(20, new Color(224, 196, 150));
        setLayout(null); // Usa null layout per posizionamento assoluto

        indietroI = loadImage("/immagini/icone/indietro.png");
        glowI = loadImage("/immagini/icone/glow.png");

        button = new JButton();
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0)); // Rimuovi i margini interni per l'icona
        button.setContentAreaFilled(false); // Rendi il bottone trasparente
        button.setBorderPainted(false); // Rendi il bordo trasparente
        add(button);

        testo = new JEditorPane();
        testo.setOpaque(false); // Rendi trasparente
        testo.setEditable(false); // Rendi non modificabile
        testo.setContentType("text/html");
        testo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(testo);

        button.addMouseListener(new MouseListener() {
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
                        f.apri("menu");
                    }
                    pressedInside = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                evidenziato = true;
                ridimensiona(f);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                evidenziato = false;
                ridimensiona(f);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isShowing()) {
                    ridimensiona(f);
                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
                // Ensure initial positioning after panel is shown
                ridimensiona(f);
            }
        });
        
        testo.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    // Apri il link nel browser predefinito
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        setVisible(true);

        ridimensiona(f);
    }

    public void ridimensiona(CustomFrame f) {
        testo.setBounds(0, this.getHeight()/2, this.getWidth(), this.getHeight());

        if (evidenziato) {
            S.posiziona(button, glowI, indietroI, 13, 6, 8, this);
        } else {
            S.posiziona(button, indietroI, 13, 6, 8, this);
        }

        testo.setContentType("text/html");
        testo.setEditable(false);

        // Costruisci il contenuto HTML
        StringBuilder htmlContent = new StringBuilder("<html><body style='text-align: center; font-family: serif; user-select: none; -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none;'>");

        htmlContent.append("<div style='font-weight: bold; font-size: 24px;'>Versione:" + getActualVersion() + "</div>");
        String response = "";
        if (!Update()) {
            response = "La versione attuale è quella più aggiornata";
        } else {
            response = "<a href=\"https://sanrio1.itch.io/test\"> E' disponibile una nuova versione, clicca qui per aggiornare!</a>";
        }

        htmlContent.append("<div style='font-size: 18px;'>" + response + "</div>");

        htmlContent.append("<p></p>");

        htmlContent.append("</body></html>");
        testo.setText(htmlContent.toString());

        // Impedisci la selezione del testo
        testo.setHighlighter(null);
        DefaultCaret noCaret = new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
                // Non dipinge il caret
            }
        };
        noCaret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        testo.setCaret(noCaret);

    }
}
