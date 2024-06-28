# Documentazione

Questo documento fornisce una breve documentazione riguardante l'applicazione "Origanum".
L'applicazione è stata realizzata dal gruppo di lavoro composto da:
- Sathyaram Pontillo
- Francesco Soricaro
- Mirko Mauro Mezzina
  
## Indice

1. [Introduzione](#1-Introduzione)
2. [Utilizzo dei file](#2-utilizzo-dei-file)
3. [Struttura algebrica](#3-struttura-algebrica)
4. [SWING](#4-Swing)
5. [Diagramma delle classi](#5-diagramma-delle-classi)
6. [Thread](#6-thread)
7. [Lambda expressions](#7-lambda-expressions)
8. [Informazioni aggiuntive](#8-informazioni-aggiuntive)

## 1. Introduzione
Origanum è un'avventura grafico/testuale, giocabile tramite intefaccia grafica basata su Java mediante l'utilizzo del framework Swing. 
La suddetta avventura è suddivisa in paragrafi. L’input utente viene effettuato tramite pulsanti e le risposte del gioco vengono mostrate come output testuale.
Il caso di studio è stato sviluppato come progetto Maven mediante l'utilizzo dell'IDE NetBeans.

## 2. Utilizzo dei File
In questa sezione viene spiegato quali file sono stati utilizzati ed in che modo.
Avremo un file denominato "testo.txt" al cui interno è presente la trama del gioco suddivisa in paragrafi. All'interno di questo ovviamente è possibile anche aggiungere altri paragrafi per arricchire la trama.
E' possibile caricare da file "gamestatus.ser" una partita salvata precedentemente.

## 3. Struttura algebrica
Le strutture algebriche utilizzate sono : Hashmap e Hashset. 


    SPECIFICA SINTATTICA HASHMAP

    Tipi:

    hashmap, key, value, boolean, int

    Operatori:

    new() -> hashmap                            crea nuove hashmap
    put(key, value, hashmap) -> hashmap         aggiunge una coppia <key, value>
    remove(key, hashmap) -> hashmap             rimuove una coppia <key, value>
    get(key, hashmap) -> value                  restituisce il value associato alla key
    size(hashmap) -> int                        restituisce il numero di coppie <key, value>
    containsKey(key, hashmap) -> boolean        stabilisce se la key è presente
    containsValue(value, hashmap) -> boolean    stabilisce se contiene almeno una key con questo value
    isEmpty(hashmap) -> boolean                 stabilisce se è vuoto
    equals(hashmap, hashmap) -> boolean         stabilisce se sono equivalenti


    SPECIFICA SEMANTICA

    Tipi:

    hashmap = insieme di coppie di tipo (k, v) ∈ H dove ∅ denota l'insieme vuoto
    boolean = insieme dei valori di verità {vero, falso}
    int     = insieme dei numeri naturali

    Operatori:

    new() = H'
    POST: H' = ∅

    put(k, v, H) = H'
    POST: H' = H ∪ {(k, v)}

    remove(put(k, v, H)) = if isEmpty(H) then new() else put(remove(H), k, v)
    PRE: (k, v) ∈ H
    POST: H' = H - {(k, v)}

    get(put(k, v, H)) = if containsKey(k, H) then v else error
    PRE: ∃(k', v') ∈ H t.c. k' = k
    POST: v = v'

    size(H) = i
    POST: i = |H|

    containsKey(k, H) = b
    POST: b = vero se k ∈ H, falso altrimenti

    containsValue(v, H) = b
    POST: b = vero se ∃(k', v') ∈ H t.c. v' = v

    isEmpty(new()) = true

    isEmpty(put(k, v, H)) = false

    equals(H, H') = b
    POST: b = vero se ∀(k, v) ∈ H, ∃(k', v') ∈ H' t.c. k = k' e v = v', falso altrimenti

    SPECIFICA DI RESTRIZIONE

    remove(key, hashmap) = error if !(containsKey(k, H))

    get(key, hashmap) = error if !(containsKey(k, H)) or isEmpty(new())

Nello specifco la struttura algebrica HashMap è stata utilizzata per gestire i paragrafi, gli oggetti, le immagini e le tracce audio.

## 4. Swing

Per la creazione della Gui è stato fondamentale l'utilizzo del framework Swing per Java.
Sono state utilizzate tutte le classi principali, come Jframe, JTextPane dove sarà presente la trama del gioco, JScrollPane, JLabel, Jpanel e Jbutton dove l'utente potrà selezionare i vari oggetti presenti nell'inventario.

## 5. Diagramma delle classi

![class diagram](Img/Origanum class diagram.jpg)

## 6. Thread

Il Thread utilizzato viene applicato alla musica. E' denominato playbackThread e permette di riavviare la musica.

## 7. Lambda expressions

La lambda expression è stata utilizzata per ottenere determinati file all'interno di una cartella specificata. Per esempio ci è servita per ottenere file che terminano con l'estensioni .jpeg, jpg, img, gif, mp3.

## 8. Informazioni aggiuntive

L'avventura è stata caricata sul sito itch.io, per scaricare la versione più aggiornata del gioco nel momento in cui verranno apportate modifiche e con una trama più arricchita.
