package it.aspix.scuola.test.modello;

import java.util.ArrayList;

/****************************************************************************
 * 
 * un ModelloCompito è un semplice contenitore di domande
 *
 ***************************************************************************/
public class Modello{

	private ArrayList<ModelloDomanda> domande;
	
	private ArrayList<String> problemi;
	
	public Modello() {
	    domande = new ArrayList<ModelloDomanda>();
	    problemi = new ArrayList<String>();
	}
	
	public void addDomanda(ModelloDomanda d) {
	    domande.add(d);
	}
	
	public ModelloDomanda getDomanda(int i) {
	    return domande.get(i);
	}
	
	public int sizeDomande() {
	    return domande.size();
	}
	
    public void addProblema(String s) {
        problemi.add(s);
    }
    
    public String getProblema(int i) {
        return problemi.get(i);
    }
    
    public int sizeProblemi() {
        return problemi.size();
    }

    /************************************************************************
     * @return una stringa che descrive i problemi di questo compito, 
     * la stringa sarà "" se non ci sono problemi
     ***********************************************************************/
	public String check(){
		StringBuilder risposta = new StringBuilder();
		for(String p: problemi) {
		    risposta.append(p+"\n");
		}
		for(ModelloDomanda d: domande){
			risposta.append(d.check());
		}
		return risposta.toString();
	}
}
