package it.aspix.scuola.test.io;

/************************************************************************************************
 * I nomi dei file vengono calcolati aggiungendo dei suffissi in base ad un nome di partenza
 ***********************************************************************************************/
public class NomiFileCorrelati {
    
    public final String modello;
	public final String stampa;
	public final String correzione;
	public final String risposte;
	public final String statistiche;
	
	private static final String SUFFISSO_STAMPA = "-stampa.odt";
	private static final String SUFFISSO_COMPITI = "-compiti.json";
	private static final String SUFFISSO_SVOLGIMENTI = "-svolgimenti.json";
	private static final String SUFFISSO_MODELLO_TXT = ".txt";
	private static final String SUFFISSO_MODELLO_ODS = ".ods";
	private static final String SUFFISSO_STATISTICHE = "-statistiche.png";
	
	public NomiFileCorrelati(String nome) throws Exception{
		String prefisso;
		if(nome.endsWith(SUFFISSO_STAMPA)){
            prefisso = nome.substring(0,nome.length()-SUFFISSO_STAMPA.length());
            modello = "";
        }else if(nome.endsWith(SUFFISSO_COMPITI)){
            prefisso = nome.substring(0,nome.length()-SUFFISSO_COMPITI.length());
            modello = "";
        }else if(nome.endsWith(SUFFISSO_SVOLGIMENTI)){
            prefisso = nome.substring(0,nome.length()-SUFFISSO_SVOLGIMENTI.length());
            modello = "";
        }else if(nome.endsWith(SUFFISSO_STATISTICHE)){
            prefisso = nome.substring(0,nome.length()-SUFFISSO_STATISTICHE.length());
            modello = "";
        }else if(nome.endsWith(SUFFISSO_MODELLO_TXT)){
            prefisso = nome.substring(0,nome.length()-SUFFISSO_MODELLO_TXT.length());
            modello = nome;
        }else if(nome.endsWith(SUFFISSO_MODELLO_ODS)){
            prefisso = nome.substring(0,nome.length()-SUFFISSO_MODELLO_ODS.length());
            modello = nome;
        }else{
            throw new Exception("Non so come utilizare il file il file \""+nome+"\"");
        }
		stampa = prefisso + SUFFISSO_STAMPA;
		correzione = prefisso + SUFFISSO_COMPITI;
		risposte = prefisso + SUFFISSO_SVOLGIMENTI;
		statistiche = prefisso + SUFFISSO_STATISTICHE;
	}
	
}
