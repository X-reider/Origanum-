package com.mycompany.versione1.costruttore;

import java.util.Collections;
import java.util.HashMap;
import java.util.regex.*;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

public class Frase {
    public String testo;
    public HashMap<String, Integer> destinazioni = new HashMap<>();
    public boolean corsivo= false;
    public Set<String> caratteristiche = new HashSet<>();      
    Set<String> requisiti = new HashSet<>();
    Set<String> requisitiNegativi = new HashSet<>();
        
    public boolean controlla (GameStatus game){
        Set<String> tag = new HashSet<>();
        tag.addAll(game.tag);
        //aggiunge un tag che corrisponde al paragrafo precedente
        tag.add("P" + String.valueOf(game.paragrafoEffettivo));
        return tag.containsAll(requisiti) && Collections.disjoint(tag, requisitiNegativi); //per ora va bene ma poi va aggiunto controllo paragrafo precedente e paragrafo già visitato
    }   
    
    //è usata nel costruttore
        public static boolean matchesRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    
    public Frase(String contenuto, int Npar) {
    // Espressione regolare per catturare le parole alfanumeriche separate da trattini e precedute da &, e la sequenza opzionale tra parentesi graffe
    String patternString = "^(?:\\{([0-9a-zA-Z\\s!,>]*)\\})?\\s*(.+)";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(contenuto);
        
        if (matcher.find()) {  
            if (matcher.group(1) != null && !matcher.group(1).isEmpty()) {
                String[] parte= new String[2];
                String[] proprità = matcher.group(1).toLowerCase().split(",");
                for (String p : proprità){
                    p= p.trim();
                    if (p.equals("n"))
                        caratteristiche.add("invio");
                    else if (p.equals("nn"))
                        caratteristiche.add("doppio invio");
                    else if(matchesRegex(p, "\\w+")){
                        requisiti.add(p); }
                    else if(matchesRegex(p, "!\\w+"))
                        requisitiNegativi.add(p);
                    else if(p.contains(">")){
                        parte = p.trim().split(">");
                        parte[1]=parte[1].trim();
                        parte[0]=parte[0].trim();
                        if (parte[1].equals("indietro"))
                            destinazioni.put(parte[0], -1);
                        else
                            destinazioni.put(parte[0], Integer.valueOf(parte[1]));
                        if (parte[0].equals(""))
                           caratteristiche.add("corsivo");
                    }
                }
            }
            testo = matcher.group(2);
        }  
    }    
}
    
