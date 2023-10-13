package it.aspix.scuola.test;

public enum OrganizzazioneRisposte {

    BLOCK  ("risposte su righe diverse"),
    INLINE ("risposte su una riga"),
    AUTO   ("risposte su una riga, ridotti su righe diverse");

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
        }else if(d.equals(AUTO.descrizione)){
            return AUTO;
        }else{
            return null;
        }
    }
}
