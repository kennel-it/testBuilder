package it.aspix.scuola.test;

import it.aspix.scuola.test.compito.Compito;
import it.aspix.scuola.test.compito.CompitoDomanda;
import it.aspix.scuola.test.svolgimento.Svolgimento;
import it.aspix.scuola.test.svolgimento.SvolgimentoDomanda;

/************************************************************************************************
 * Valutazione di un compito, ha bisogno dia dello svolgimento che del compito stesso
 ***********************************************************************************************/
public class Valutatore {

    private double valoreBase;
    private double valoreGiusta;
    private double valoreSbagliata;

    private Compito compito;
    private Svolgimento svolgimento;
    
    public Valutatore() {
        super();
    }
    
    public Valutatore(Compito compito, Svolgimento svolgimento) {
        super();
        this.compito = compito;
        this.svolgimento = svolgimento;
    }
    
    public void setCompito(Compito compito) {
        this.compito = compito;
    }

    public void setSvolgimento(Svolgimento svolgimento) {
        this.svolgimento = svolgimento;
    }
    
    public void setValoreBase(double valoreBase) {
        this.valoreBase = valoreBase;
    }

    public void setValoreGiusta(double valoreGiusta) {
        this.valoreGiusta = valoreGiusta;
    }

    public void setValoreSbagliata(double valoreSbagliata) {
        this.valoreSbagliata = valoreSbagliata;
    }
    
    /********************************************************************************************
     * @return il punteggio ottenuto dallo svolgimento
     *******************************************************************************************/
    public double getPunteggio() {       
        double punteggio = valoreBase;
        
        for(int i=0; i<svolgimento.domande.size(); i++) {
            int rispostaScelta = svolgimento.domande.get(i).valore; 
            if( rispostaScelta != -1 ) {
                if(compito.domande.get(i).risposte.get(rispostaScelta).giusta) {
                    punteggio += valoreGiusta;
                } else {
                    punteggio += valoreSbagliata;
                }
            }
        }
        return punteggio;
    }
    
    /********************************************************************************************
     * @return un vettore che contiene le risposte ordinate come nel modello originale, il valore
     *         degli elementi del vettore è "-1" per risposta non data o l'inndice della 
     *         risposta sempre rispetto al modello originale
     *******************************************************************************************/
    public int[] getSequenzaInOriginale() {
        int risposte[] = new int[compito.domande.size()];
        
        for(int i=0; i<svolgimento.domande.size(); i++) {
            int rispostaScelta = svolgimento.domande.get(i).valore; 
            if( rispostaScelta == -1 ) {
                risposte[compito.domande.get(i).posizioneOriginale] = -1;
            } else {
                risposte[compito.domande.get(i).posizioneOriginale] = 
                        compito.domande.get(i).risposte.get(rispostaScelta).posizioneOriginale;
            }
        }
        return risposte;
    }
    
    public StatoRisposta getStatoRisposta(int n) {
        SvolgimentoDomanda svolgimentoDomanda = svolgimento.domande.get(n);
        CompitoDomanda compitoDomanda = compito.domande.get(n);
        
        if( svolgimentoDomanda.valore == -1 ) {
            return StatoRisposta.NON_DATA;
        } else if( compitoDomanda.risposte.get(svolgimentoDomanda.valore).giusta ) {
            return StatoRisposta.GIUSTA;
        } else {
            return StatoRisposta.SBAGLIATA;
        }
    } 
}
