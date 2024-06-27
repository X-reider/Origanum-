package com.mycompany.versione1.costruttore;

import java.util.regex.*;
import java.util.*;


public class Paragrafo {
   public String tipo= "Screen"; 
   public int pagina;
   public boolean temporaneo=false;
   public String immagine;
   public int percentuale=100;
   public Frase[] frasi;
   public Set<String> tagAggiunti = new HashSet<>();      //serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   public Set<String> tagRimossi = new HashSet<>();       //serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   public String oggettoAggiunto;//serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   public Set<String> oggettiRimossi = new HashSet<>();   //serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   public String[] ricetta;
   public String traccia= "non cambiare";
   public String effettiSonori;
   public int inizio;

    public Paragrafo(String contenuto) {
        // Espressione regolare spezzata in parti leggibili(?:\\s\\d)*
        String spazzi = "\\s*";
        String numero = "(?s)^(\\d+)";              // Cattura uno o più numeri all'inizio       
        String comandi = "\\[([0-9a-zA-Z\\s:,-]*)\\]";                
        String testo = "(.+)";              // Cattura il resto del testo
        String patternString = numero+ spazzi +comandi +spazzi +testo;
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(contenuto);
        ricetta= "NULLA+NULLA".split("\\+");
        if (matcher.find()) { 
            //pagina
            this.pagina = Integer.parseInt(matcher.group(1));
            
           if (matcher.group(2) != null && !matcher.group(2).isEmpty()) {
                String[] parte= new String[2];
                String[] proprità = matcher.group(2).toLowerCase().split(",");
                for (String p : proprità){
                    p= p.trim();
                    if(p.equals("temporaneo"))
                        temporaneo = true;
                    else if(p.contains("-")){
                        parte = p.split("-");
                        ricetta[1] = parte[1].trim();
                        ricetta[0] = parte[0].trim();
                    } 
                    else if (p.contains(":")){                        
                        parte = p.trim().split(":");
                        parte[1]=parte[1].trim();
                        parte[0]=parte[0].trim();
                        if (parte[0].equals("tipo"))
                            tipo=parte[1];
                        if (parte[0].equals("traccia"))
                            traccia=parte[1];
                        if (parte[0].equals("effetti"))
                            effettiSonori=parte[1];
                        if (parte[0].equals("inizio"))
                            inizio=Integer.parseInt(parte[1]);
                        if (parte[0].equals("tag"))
                            tagAggiunti.add(parte[1]);
                        if (parte[0].equals("rtag"))
                            tagRimossi.add(parte[1]);
                        if (parte[0].equals("oggetto"))
                            oggettoAggiunto= parte[1];
                        if (parte[0].equals("immagine"))
                            immagine= parte[1];
                        if (parte[0].equals("estrai"))
                            percentuale=Integer.parseInt(parte[1]);                  
                        }
                    }
                }   
            }                      
            if(matcher.group(3) != null){
                String[] frasiarray = matcher.group(3).replaceAll("([\\.!?])", "$1§").split("§");
            frasi = new Frase[frasiarray.length];
            // costruisci frasi
            for (int i = 0; i < frasiarray.length; i++) {
                frasi[i]= new Frase(frasiarray[i].trim(), pagina);
            }
            } 
        }
   
    }
