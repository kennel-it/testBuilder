package it.aspix.scuola.test;

import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SupportoAudio {
    
    public static boolean acceso = true;
    
    public static final Media LETTERA[] = new Media[4];
    static{
        LETTERA[0] = new Media(SupportoAudio.class.getResource("audio/a.mp3").toString());
        LETTERA[1] = new Media(SupportoAudio.class.getResource("audio/b.mp3").toString());
        LETTERA[2] = new Media(SupportoAudio.class.getResource("audio/c.mp3").toString());
        LETTERA[3] = new Media(SupportoAudio.class.getResource("audio/d.mp3").toString());
    }
    
    public static final Media NUMERO[] = new Media[10];
    static{
        for(int i=0; i<NUMERO.length ; i++) {
            NUMERO[i] = new Media(SupportoAudio.class.getResource("audio/"+i+".mp3").toString());
        }
    }
    
    public static final Media FINE = new Media(SupportoAudio.class.getResource("audio/toccatoFondo.mp3").toString());
    public static final Media SI = new Media(SupportoAudio.class.getResource("audio/si.mp3").toString());
    public static final Media NO = new Media(SupportoAudio.class.getResource("audio/no.mp3").toString());
    public static final Media BIANCA = new Media(SupportoAudio.class.getResource("audio/bianca.mp3").toString());
    
    
    private static ArrayList<Media> mediaInSospeso = new ArrayList<>();
    private static MediaPlayer mp = null;
    
    private static Runnable xxx = new Runnable() {
        @Override
        public void run() {
            if(mediaInSospeso.size()>0) {
                mp = new MediaPlayer(mediaInSospeso.get(0));
                mediaInSospeso.remove(0);
                mp.setVolume(0.5);
                mp.play();
                mp.setOnEndOfMedia( xxx ); 
            } else {
                mp = null;
            }
        }
    };
    
    public static void play(Media media) {
        if(acceso) {
            if(mp == null){
                // mp.seek( new Duration(0) ); 
                mp = new MediaPlayer(media);
                mp.setVolume(0.5);
                mp.play();
                mp.setOnEndOfMedia( xxx ); 
            } else {
                mediaInSospeso.add( media );
            }
        }
    }
}
