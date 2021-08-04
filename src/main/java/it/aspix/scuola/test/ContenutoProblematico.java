package it.aspix.scuola.test;

public class ContenutoProblematico extends Exception {

    private static final long serialVersionUID = 1L;
    
    private String dettaglioProblema;
    
    public ContenutoProblematico(String s) {
        dettaglioProblema = s;
    }
    
    public String getDettagliProblema() {
        return dettaglioProblema;
    }

}
