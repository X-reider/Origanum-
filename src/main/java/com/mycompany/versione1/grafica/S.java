package com.mycompany.versione1.grafica;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class S {

    private static final Map<String, BufferedImage> imageCache = new HashMap<>();

    public static void posiziona(JButton bottone, BufferedImage icona1, BufferedImage icona2, BufferedImage icona3, double dim, double sinistra, double alto, RoundedPanel backgroundPanel) {
        int larghezza = backgroundPanel.getWidth();
        int altezza = backgroundPanel.getHeight();
        int iconSize = (int) (altezza * dim / 100);

        Icon scaled1 = new ImageIcon(getScaledImage(icona1, iconSize, iconSize));
        Icon scaled2 = new ImageIcon(getScaledImage(icona2, iconSize, iconSize));
        Icon scaled3 = new ImageIcon(getScaledImage(icona3, iconSize, iconSize));
        bottone.setIcon(new CombinedIcon(scaled1, scaled2, scaled3));

        double X = sinistra / 100;
        double Y = alto / 100;
        bottone.setBounds((int) (X * larghezza - dim / 200 * altezza), (int) (Y * altezza - dim / 200 * altezza), iconSize, iconSize);
    }

    public static void posiziona(JButton bottone, BufferedImage icona1, BufferedImage icona2, double dim, double sinistra, double alto, RoundedPanel backgroundPanel) {
        int larghezza = backgroundPanel.getWidth();
        int altezza = backgroundPanel.getHeight();
        int iconSize = (int) (altezza * dim / 100);

        Icon scaled1 = new ImageIcon(getScaledImage(icona1, iconSize, iconSize));
        Icon scaled2 = new ImageIcon(getScaledImage(icona2, iconSize, iconSize));
        bottone.setIcon(new CombinedIcon(scaled1, scaled2));

        double X = sinistra / 100;
        double Y = alto / 100;
        bottone.setBounds((int) (X * larghezza - dim / 200 * altezza), (int) (Y * altezza - dim / 200 * altezza), iconSize, iconSize);
    }

    public static void posiziona(JButton bottone, BufferedImage icona1, double dim, double sinistra, double alto, RoundedPanel backgroundPanel) {
        int larghezza = backgroundPanel.getWidth();
        int altezza = backgroundPanel.getHeight();
        int iconSize = (int) (altezza * dim / 100);

        Icon scaled1 = new ImageIcon(getScaledImage(icona1, iconSize, iconSize));
        bottone.setIcon(scaled1);

        double X = sinistra / 100;
        double Y = alto / 100;
        bottone.setBounds((int) (X * larghezza - dim / 200 * altezza), (int) (Y * altezza - dim / 200 * altezza), iconSize, iconSize);
    }

    public static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h) {
        String key = srcImg.hashCode() + "_" + w + "_" + h;
        if (imageCache.containsKey(key)) {
            return imageCache.get(key);
        }

        if (w <= 0 || h <= 0) {
            return srcImg;
        }

        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // Usa RenderingHints meno costosi se la qualità non è critica
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        imageCache.put(key, resizedImg);

        return resizedImg;
    }
}
