package com.mycompany.versione1.grafica;

import java.awt.*;
import javax.swing.*;

public class CombinedIcon implements Icon {
    private final Icon mainIcon;
    private final Icon overlayIcon;
    private final Icon evidenziatoIcon;

    public CombinedIcon(Icon mainIcon, Icon overlayIcon) {
        this.mainIcon = mainIcon;
        this.overlayIcon = overlayIcon;
        this.evidenziatoIcon = null;
    }
    
        public CombinedIcon(Icon evidenziato, Icon mainIcon, Icon overlayIcon ) {
        this.mainIcon = mainIcon;
        this.overlayIcon = overlayIcon;
        this.evidenziatoIcon = evidenziato;
    }

    @Override
    public int getIconWidth() {
        return mainIcon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return mainIcon.getIconHeight();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if(evidenziatoIcon !=null)
            evidenziatoIcon.paintIcon(c, g, x , y);
        
        mainIcon.paintIcon(c, g, x, y);
        overlayIcon.paintIcon(c, g, x , y);
    }
}
