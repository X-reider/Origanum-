package com.mycompany.versione1.grafica;

import com.mycompany.versione1.costruttore.GameStatus;
import static com.mycompany.versione1.costruttore.GameStatus.loadImage;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Menu extends RoundedPanel{
    private final BufferedImage gioca;
    public BufferedImage giocaI;
    public BufferedImage giocaIC;
    public BufferedImage esciI;
    public BufferedImage esciIC;
    public BufferedImage creditsI;
    public BufferedImage creditsIC;
    public BufferedImage aggiornaI;
    public BufferedImage aggiornaIC;
    private JButton[] buttons;
    public int evidenziato = -1;

    
    public Menu(GameStatus game, CustomFrame f){
        super(20, new ImageIcon(Menu.class.getResource("/immagini/sfondi/menuSondo.png")).getImage());  
        setLayout(null); // Usa null layout per posizionamento assoluto
        gioca = loadImage("/immagini/icone/menu.png");
        
        giocaI = loadImage("/immagini/icone/gioca.png");
        esciI = loadImage("/immagini/icone/esci.png");
        creditsI = loadImage("/immagini/icone/credits.png");
        aggiornaI = loadImage("/immagini/icone/aggiorna.png");

        giocaIC = loadImage("/immagini/icone/gioca_click.png");
        esciIC = loadImage("/immagini/icone/esci_click.png");
        creditsIC = loadImage("/immagini/icone/credits_click.png");
        aggiornaIC = loadImage("/immagini/icone/aggiorna_click.png");
        
       // Crea e aggiungi 9 bottoni 
        buttons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            buttons[i] = new JButton();
            buttons[i].setFocusPainted(false);
            buttons[i].setMargin(new Insets(0, 0, 0, 0)); // Rimuovi i margini interni per l'icona
            buttons[i].setContentAreaFilled(false); // Rendi il bottone trasparente
            buttons[i].setBorderPainted(false); // Rendi il bordo trasparente
            add(buttons[i]);
        }
        
        
 // Aggiungi ActionListener ai bottoni 6=analizza
        for (int i = 0; i < 4; i++) {
            int index = i;
            buttons[i].addMouseListener(new MouseListener() {
                private boolean pressedInside = false;
                @Override
                public void mouseClicked(MouseEvent e) {}
                @Override
                public void mousePressed(MouseEvent e) {
                    pressedInside = true;
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (pressedInside) {
                        if (e.getButton() == MouseEvent.BUTTON1) { //TASTO SINISTR
                            if (index == 0) {
                                f.apri("Screen");
                            }
                            if (index == 1)
                                System.exit(0);
                            if (index == 2)
                                f.apri("credits");
                            if (index ==3)
                                f.apri("update");
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
            addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(isShowing())
                 ridimensiona(game, f);
            }
            @Override
            public void componentShown(ComponentEvent e) {
                // Ensure initial positioning after panel is shown
                 ridimensiona(game, f);
            }
        });
        setVisible(true);
    }
    
    public void ridimensiona (GameStatus game, CustomFrame f) {
        //cifre in percentuale
        
        double DIM=14;
        double MARGINE_SINSTRO = 50;
        double MARGINE_ALTO = 50;

        if(evidenziato==0)
            S.posiziona(buttons[0], giocaIC, 19 ,22, 51, this); // gioca 
        else
            S.posiziona(buttons[0], giocaI, 19 ,22, 52.9, this); // gioca 
        if (evidenziato==1)            
            S.posiziona(buttons[1], esciIC, 14 ,86, 20, this);
        else
            S.posiziona(buttons[1], esciI, 14 ,86, 19.9, this);
        if(evidenziato==2)
            S.posiziona(buttons[2], creditsIC, 19 ,57, 53.4, this);
        else
            S.posiziona(buttons[2], creditsI, 19 ,57, 53, this);
        if(evidenziato==3)
            S.posiziona(buttons[3], aggiornaIC, 19 ,57, 77, this);
        else
            S.posiziona(buttons[3], aggiornaI, 19 ,57, 77.4, this);
                
    }
}
