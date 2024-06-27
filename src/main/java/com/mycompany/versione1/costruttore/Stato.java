
package com.mycompany.versione1.costruttore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Stato implements Serializable{
    public String[] inventario;
    public Set<String> tag = new HashSet<>();
    public int paragrafoCorrente = 1;
        
    public Stato (GameStatus game){
    this.inventario= game.inventario.clone();
    this.tag= game.tag;
    this.paragrafoCorrente=game.paragrafoCorrente;
    }
        
}
