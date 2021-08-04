package it.aspix.scuola.test.compito;

public class CompitoRisposta {

    public int posizioneOriginale;
    public String testo;
    public boolean giusta;

    public CompitoRisposta() {
        super();
    }
    
    public CompitoRisposta(String testo, int posizioneOriginale, boolean giusta) {
        super();
        this.giusta = giusta;
        this.testo = testo;
        this.posizioneOriginale = posizioneOriginale;
    }
	
}
