package com.mycompany.versione1.costruttore;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.*;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Player {
    private Thread playbackThread;
    private AdvancedPlayer player;
    private boolean isPlaying;
    private int trascorsi;
    public String attuale="nulla";     
    public float volAttuale;

    public void playSong(GameStatus game, String nuova, int inizio) {        
        if(!(attuale.equals(nuova) || nuova.equals("non cambiare")) ){
            stopSong();
            attuale = nuova;
            try {
                InputStream fileInputStream = MP3Player.class.getResourceAsStream(game.traccie.get(attuale));
                AudioDevice audioDevice = new CustomAudioDevice();
                player = new AdvancedPlayer(fileInputStream, audioDevice);
                isPlaying = true;

                playbackThread = new Thread(() -> {
                    try {
                        player.setPlayBackListener(new PlaybackListener() {
                            @Override
                            public void playbackFinished(PlaybackEvent evt) {
                               stopSong();
                               playSong(game, attuale, 0); // Riavvia la traccia dall'inizioo
                            }
                        });

                        player.play(inizio, Integer.MAX_VALUE);

                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                });
                playbackThread.start();
                
              if (fileInputStream == null) {
                throw new FileNotFoundException("Risorsa non trovata");
            }
              
            } catch (FileNotFoundException | JavaLayerException e) {
                e.printStackTrace();
            }   
        }
        //se è cambiato il volume del sistema aggiorna
        if( volAttuale != game.volume){
            volAttuale = game.volume;
            setVolume(game.volume);
        }
    }

    public void stopSong() {
        if (isPlaying && player != null) {
            player.close();
            playbackThread.interrupt();
            isPlaying = false;
        }
    }

    private void setVolume(float volume) {
        try {
            // Ottieni il mixer di sistema
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            for (Mixer.Info mixerInfo : mixers) {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                    port.open();
                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                        FloatControl volumeControl = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
                        volumeControl.setValue(volume);
                    }
                    port.close();
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private class CustomAudioDevice extends javazoom.jl.player.JavaSoundAudioDevice {
        @Override
        public void write(short[] samples, int offs, int len) throws JavaLayerException {
            super.write(samples, offs, len);
            trascorsi += len / 1152; // 1152 samples per frame for MPEG 1 Layer III
        }
    }
}