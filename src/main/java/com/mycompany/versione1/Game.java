package com.mycompany.versione1;

import com.mycompany.versione1.costruttore.GameStatus;
import com.mycompany.versione1.grafica.CustomFrame;
import java.io.*;

public class Game {    
    public static void main(String[] args) throws IOException, ClassNotFoundException { 
        GameStatus g; 

        
        String filePath = "gamestatus.ser";
        File file = new File(filePath);
        if (file.exists()) {
            g= new GameStatus("gamestatus.ser");
        } else {
            g= new GameStatus();
        }       
        
        CustomFrame frame = new CustomFrame(g);
    }
}

      