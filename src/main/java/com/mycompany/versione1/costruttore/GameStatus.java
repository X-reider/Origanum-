package com.mycompany.versione1.costruttore;

import com.mycompany.versione1.grafica.CustomFrame;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;

public class GameStatus implements Serializable {
    //risorse

    public transient Random random;
    public transient HashMap<Integer, Paragrafo> paragrafi = new HashMap<>();
    public transient HashMap<String, BufferedImage> oggetti = new HashMap<>();
    public transient HashMap<String, BufferedImage> immagini = new HashMap<>();
    public transient HashMap<String, String> traccie;
    public transient MP3Player mp3Player;

    //gestione indietro
    public transient Stato visita;
    public LinkedList<Stato> list = new LinkedList<>();
    public transient ListIterator<Stato> cronologia = list.listIterator();

    //informazioni principali di gioc
    public String[] inventario;
    public Set<String> tag = new HashSet<>();
    public int selezionato = 7;
    public int paragrafoCorrente = 1;
    public int paragrafoEffettivo = 1;

    // impostazioni
    public float volume = 0.99f; // in futuro verrà implementato un modo per impostare il volume
    public int font = 16;
    public String scena; // non ho idea di che roba sia

    public void save() {
        try (FileOutputStream fileOut = new FileOutputStream("gameStatus.ser"); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameStatus(String filePath) {
        GameStatus loadedStatus = null;
        try (FileInputStream fileIn = new FileInputStream(filePath); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            loadedStatus = (GameStatus) in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Copia i dati dall'oggetto caricato a questo oggetto
        this.list = loadedStatus.list;
        this.cronologia = list.listIterator();
        this.inventario = loadedStatus.inventario;
        this.tag = loadedStatus.tag;
        this.selezionato = loadedStatus.selezionato;
        this.paragrafoCorrente = loadedStatus.paragrafoCorrente;
        this.paragrafoEffettivo = loadedStatus.paragrafoEffettivo;
        this.volume = loadedStatus.volume;
        this.font = loadedStatus.font;
        this.scena = loadedStatus.scena;
        mp3Player = new MP3Player();

        //lettura tracce audio
        traccie = leggiAudioDaCartella("audio/musiche");
        // lettura immagini oggetti
        oggetti = leggiImmaginiDaCartella("immagini/oggetti");
        //leggi immagini paragrafo
        immagini = leggiImmaginiDaCartella("immagini/illustrazioni");
        // lettura file di testo
        StringBuilder contenuto = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(GameStatus.class.getResourceAsStream("/testi/testo.txt")))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenuto.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // caricamento dei paragrafi
        String[] paragrafiArray = contenuto.toString().trim().substring(1).split("#");
        Paragrafo appoggio;
        for (String p : paragrafiArray) {
            appoggio = new Paragrafo(p.trim());
            paragrafi.put(appoggio.pagina, appoggio);
        }
    }

    public GameStatus() {
        mp3Player = new MP3Player();
        //lettura tracce audio
        traccie = leggiAudioDaCartella("audio/musiche");
        // lettura immagini oggetti
        oggetti = leggiImmaginiDaCartella("immagini/oggetti");
        //leggi immagini paragrafo
        immagini = leggiImmaginiDaCartella("immagini/illustrazioni");

        //popola l'inventario
        inventario = new String[9];
        for (int i = 0; i < 7; i++) {
            inventario[i] = "vuoto";
        }
        inventario[7] = "vuoto";
        inventario[8] = "vuoto";

        // Leggi il file di testo
        StringBuilder contenuto = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(GameStatus.class.getResourceAsStream("/testi/testo.txt")))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenuto.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // carica i paragrafi
        String[] paragrafiArray = contenuto.toString().trim().substring(1).split("#");
        Paragrafo appoggio;
        for (String p : paragrafiArray) {
            appoggio = new Paragrafo(p.trim());
            paragrafi.put(appoggio.pagina, appoggio);
        }
    }

    //gestisce il cambio di paragrafo
    public void vai(int destinazione, CustomFrame f) {
        //traduzione destinazione in caso di -1
        if (destinazione == -1) {
            destinazione = paragrafoEffettivo;
        }
        // tiene al passo la cronologia
        cronologia.add(new Stato(this));

        //aggiungi nuovo oggetto DA FARE LA RIMOZIONE
        if (inventario[selezionato].equals("vuoto") && paragrafi.get(destinazione).oggettoAggiunto != null) {
            inventario[selezionato] = paragrafi.get(destinazione).oggettoAggiunto;
        }

        //mantiene aggiornato il paragrafo effettivo
        if (!paragrafi.get(paragrafoCorrente).temporaneo) {
            paragrafoEffettivo = paragrafoCorrente;
        }

        //applica tag (vecchio paragrafo)
        tag.addAll(paragrafi.get(paragrafoCorrente).tagAggiunti);
        tag.removeAll(paragrafi.get(paragrafoCorrente).tagRimossi);
        tag.remove("f");

        paragrafoCorrente = destinazione;
        if (estrai()) {
            tag.add("f");
        }

        f.apri(paragrafi.get(destinazione).tipo);
    }

    boolean estrai() {
        int fortuna = 1;
        if (tag.contains("fortuna1")) {
            fortuna = 2;
        }
        if (tag.contains("fortuna2")) {
            fortuna = 3;
        }
        if (tag.contains("benedizione")) {
            fortuna += 5;
        }

        for (int i = 0; i < fortuna; i++) {
            random = new Random();
            if (random.nextInt(100) < paragrafi.get(paragrafoCorrente).percentuale) {
                return true;
            }
        }
        return false;
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(GameStatus.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, BufferedImage> leggiImmaginiDaCartella(String directoryPath) {
        HashMap<String, BufferedImage> oggetti = new HashMap<>();
        File cartella = new File(GameStatus.class.getResource("/" + directoryPath).getFile());
        String jarOrExePath = GameStatus.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        //Controlla con che estensione viene eseguito il file (Utilizzato principalmente per l'esecuione del file in jar ed exe)
        if (!(jarOrExePath.toLowerCase().endsWith(".jar") || jarOrExePath.toLowerCase().endsWith(".exe"))) {
            // Controlla se la directory esiste e se è una directory
            if (cartella.exists() && cartella.isDirectory()) {

                File[] files = cartella.listFiles((dir, name) -> {
                    String lowerName = name.toLowerCase();
                    return lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".gif");
                });

                if (files != null) {
                    for (File file : files) {
                        try {
                            BufferedImage image = ImageIO.read(file);
                            if (image != null) {
                                oggetti.put(rimuoviEstensione(file.getName()), image);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Nessun file trovato nella directory specificata.");
                }
            } else {
                System.out.println("La directory specificata non esiste o non è una directory.");
            }
        } else { //Parte utilizzata per navigare nelle risorse del file jar/exe
            try (JarFile jarFile = new JarFile(jarOrExePath)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    // Controlla se l'entry è un file e se è nella cartella specificata
                    if (!entry.isDirectory() && entry.getName().startsWith(directoryPath)) {
                        // Verifica se il file ha l'estensione desiderata
                        if (entry.getName().toLowerCase().endsWith(".png") || entry.getName().toLowerCase().endsWith(".jpg") || entry.getName().toLowerCase().endsWith(".jpeg") || entry.getName().toLowerCase().endsWith(".gif")) {
                            try (InputStream is = jarFile.getInputStream(entry)) {
                                BufferedImage image = ImageIO.read(is);
                                if (image != null) {
                                    oggetti.put(rimuoviEstensione(new File(entry.getName()).getName()), image);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return oggetti;

    }

    public static HashMap<String, String> leggiAudioDaCartella(String directoryPath) {
        HashMap<String, String> traccie = new HashMap<>();
        File cartella = new File(GameStatus.class.getResource("/" + directoryPath).getFile());
        String jarOrExePath = GameStatus.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        //Controlla con che estensione viene eseguito il file (Utilizzato principalmente per l'esecuione del file in jar ed exe)
        if (!(jarOrExePath.toLowerCase().endsWith(".jar") || jarOrExePath.toLowerCase().endsWith(".exe"))) {
            // Controlla se la directory esiste e se è una directory
            if (cartella.exists() && cartella.isDirectory()) {

                File[] files = cartella.listFiles((dir, name) -> {
                    String lowerName = name.toLowerCase();
                    return lowerName.endsWith(".mp3");
                });

                if (files != null) {
                    for (File file : files) {
                        traccie.put(rimuoviEstensione(file.getName()), "/" + directoryPath + "/" + file.getName());
                    }
                } else {
                    System.out.println("Nessun file trovato nella directory specificata.");
                }
            } else {
                System.out.println("La directory specificata non esiste o non è una directory.");
            }
        } else { //Parte utilizzata per navigare nelle risorse del file jar/exe
            try (JarFile jarFile = new JarFile(jarOrExePath)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    // Controlla se l'entry è un file e se è nella cartella specificata
                    if (!entry.isDirectory() && entry.getName().startsWith(directoryPath)) {
                        // Verifica se il file ha l'estensione desiderata
                        if (entry.getName().toLowerCase().endsWith(".mp3")) {
                            traccie.put(rimuoviEstensione(new File(entry.getName()).getName()), "/" + entry.getName());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return traccie;
    }

    public static String rimuoviEstensione(String nomeFile) {
        if (nomeFile == null || nomeFile.isEmpty()) {
            return nomeFile;
        }

        // Trova l'indice dell'ultimo punto
        int ultimoPunto = nomeFile.lastIndexOf('.');

        // Se non c'è punto, restituisce il nome del file intero
        if (ultimoPunto == -1) {
            return nomeFile;
        }

        // Restituisce la sottostringa dal primo carattere fino all'ultimo punto
        return nomeFile.substring(0, ultimoPunto);
    }

}
