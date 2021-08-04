package it.aspix.scuola.test.modello;

import java.util.ArrayList;

/****************************************************************************
 * 
 * ModelloDomanda sta per Domanda facente parte del modello,
 * ha un testo e delle possibili risposte
 *
 ***************************************************************************/
public class ModelloDomanda {

    private int id; // usualmente la posizione
    private String testo;
	private ArrayList<ModelloRisposta> risposte;
	
    public ModelloDomanda(int id, String domanda){
        super();
        this.id = id;
        this.testo = domanda;
        this.risposte = new ArrayList<>();
    }
	
    public int getId() {
        return id;
    }

    public String getTesto() {
        return testo;
    }
    
    public void addRisposta(ModelloRisposta r) {
        risposte.add(r);
    }
    
    public ModelloRisposta getRisposta(int i) {
        return risposte.get(i);
    }
    
    public int size() {
        return risposte.size();
    }
    
    /************************************************************************
     * @return una stringa che descrive i problemi di questa domanda, 
     * la stringa sarà "" se non ci sono problemi
     ***********************************************************************/
    public String check(){
    	int numeroRisposteGiuste = 0;
    	for(ModelloRisposta r: risposte){
    		if( r.giusta() ){
    			numeroRisposteGiuste++;
    		}
    	}
    	if(numeroRisposteGiuste!=1){
    		return "La domanda \""+testo+"\" ha "+numeroRisposteGiuste+" risposte esatte\n";
    	}else{
    		return "";
    	}
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(id+":"+testo);
        for(ModelloRisposta mr: risposte) {
            sb.append("\n  "+mr);
        }
        return sb.toString();
    }
    
}
