package it.aspix.scuola.test.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import it.aspix.scuola.test.modello.Modello;
import it.aspix.scuola.test.modello.ModelloDomanda;
import it.aspix.scuola.test.modello.ModelloRisposta;

/************************************************************************************************
 * Le domande iniziano con "*" e le risposte con:
 * "+" per la risposta giusta
 * "-" per le risposte sbagliate
 * "=" per la domanda eliminabile
 * 
 * le righe che iniziano con "#" sono commenti e vengono ignorate, tra una domanda e l'altra 
 * deve esserci una riga vuota
 ***********************************************************************************************/
public class ReaderModelloTxt {
	
    static final char INIZIO_COMMENTO = '#';
	static final char INIZIO_DOMANDA = '*';
	static final char INIZIO_RISPOSTA_ESATTA = '+';
	static final char INIZIO_RISPOSTA_SBAGLIATA = '-';
	static final char INIZIO_RISPOSTA_ELIMINABILE = '=';
	
	public static Modello leggi(File nomeFileDomande, String encoding) throws IOException{
		String riga;
		int spazio;
		Modello compito = new Modello();
		ModelloDomanda inCostruzione = null;
		FileInputStream fis = new FileInputStream(nomeFileDomande);
        InputStreamReader isr = new InputStreamReader(fis, encoding);
        BufferedReader input = new BufferedReader(isr);
        char iniziale;
        int numeroDomanda = 1;
        while( (riga=input.readLine()) != null){
            riga = riga.trim();
            if(riga.startsWith(""+INIZIO_COMMENTO)){
            	continue;
            }
            if(riga.length()==0){
                // potrebbe essere la fine di una domanda
                if(inCostruzione!=null){
                    compito.addDomanda(inCostruzione);
                    inCostruzione = null;
                }
                continue;
            }
            if(riga.startsWith(""+INIZIO_DOMANDA)){
                spazio = riga.indexOf(' ');
                riga = riga.substring(spazio+1);
                riga = riga.trim(); // metti caso abbia messo più spazi
                while(riga.endsWith("\\")){
                	// qui non si fa trim perché se va a capo probabilmente c'è formattazione con spazi
                	riga += "n"+input.readLine();
                }
                inCostruzione = new ModelloDomanda(numeroDomanda, riga);
                numeroDomanda++;
            }else{
                if(riga.startsWith(""+INIZIO_RISPOSTA_ESATTA) || riga.startsWith(""+INIZIO_RISPOSTA_SBAGLIATA) || riga.startsWith(""+INIZIO_RISPOSTA_ELIMINABILE)){
                	iniziale = riga.charAt(0);
                    spazio = riga.indexOf(' ');
                    riga = riga.substring(spazio+1);
                    riga = riga.trim(); // metti caso abbia messo più spazi
                    ModelloRisposta attuale = new ModelloRisposta (
                            riga,
                            iniziale==INIZIO_RISPOSTA_ESATTA, 
                            iniziale==INIZIO_RISPOSTA_ELIMINABILE
                    );
                    if(inCostruzione!=null) {
                        inCostruzione.addRisposta(attuale);
                    } else {
                        compito.addProblema("risposta senza domanda: \""+riga+'"');
                    }
                }else{
                    compito.addProblema("non capisco \""+riga+'"');
                }
            }
        }
        // vedi se c'è una domanda non ancora registrata
        if(inCostruzione!=null){
            compito.addDomanda(inCostruzione);
        }        
        input.close();
        isr.close();
        fis.close();
        return compito;
	}
}
