package it.aspix.scuola.test;

public enum OrganizzazioneRisposte {
	
    BLOCK  ("risposte su righe diverse"),
    INLINE ("risposte su una riga");
	
	private final String descrizione;
	
	OrganizzazioneRisposte(String descrizione) {
        this.descrizione = descrizione;
    }
	
	public String toString(){
		return descrizione;
	}

	static OrganizzazioneRisposte cercaPerDescrizione(String d){
		if(d.equals(BLOCK.descrizione)){
			return BLOCK;
		}else if(d.equals(INLINE.descrizione)){
			return INLINE;
		}else{
			return null;
		}
	}
}
