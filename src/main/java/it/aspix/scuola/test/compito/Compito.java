package it.aspix.scuola.test.compito;

import it.aspix.scuola.test.OrganizzazioneRisposte;

import java.util.ArrayList;

public class Compito {

    public int id;
    public boolean ridotto = false;
    public OrganizzazioneRisposte organizzazioneRisposte;
    public ArrayList<CompitoDomanda> domande = new ArrayList<>();

	public void addDomanda(CompitoDomanda cd) {
	    cd.ridotto = ridotto;
	    domande.add(cd);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<domande.size(); i++){
			sb.append(domande.get(i).toString());
		}
		return sb.toString();
	}
	
    public String toODT(OrganizzazioneRisposte modello){
    	StringBuilder sb=new StringBuilder();
    	for(int i=0 ; i<domande.size(); i++){
    		sb.append(domande.get(i).toODT(i, modello));
    	} 
    	return sb.toString();
    }

}
