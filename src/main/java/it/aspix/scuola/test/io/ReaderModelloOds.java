package it.aspix.scuola.test.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;

import it.aspix.scuola.test.modello.Modello;
import it.aspix.scuola.test.modello.ModelloDomanda;
import it.aspix.scuola.test.modello.ModelloRisposta;

/************************************************************************************************
 * Prima colonna: domanda
 * Seconda colonna: risposta giusta
 * Seguono poi le altre risposte e l'ultima è quella eliminabile
 ***********************************************************************************************/
public class ReaderModelloOds {
	
	public static Modello leggi(File nomeFileDomande, String encoding) throws IOException{
		System.out.println(encoding);
		Modello compito = new Modello();
		ModelloDomanda inCostruzione = null;
        
        SpreadSheet spread = new SpreadSheet(nomeFileDomande);
        System.out.println("Number of sheets: " + spread.getNumSheets());
        System.out.println();
        Sheet sheet = spread.getSheets().get(0);
        
        System.out.println("In sheet " + sheet.getName());
        Range range = sheet.getDataRange();
        System.out.println( "colonne:"+range.getNumColumns());
        System.out.println( "righe:"+range.getNumRows());
        for(int iRiga=0; iRiga<range.getNumRows(); iRiga++) {
            Range rDomanda = range.getCell(iRiga, 0);
            if(rDomanda==null || rDomanda.getValue()==null) {
                System.out.println(iRiga+" vuota "+rDomanda.getValue());
                continue;
            }
            String domanda = rDomanda.getValue().toString();
            ArrayList<String> risposte = new ArrayList<>(); 
            for(int iColonna=1; iColonna<range.getNumColumns(); iColonna++) {
                Range rRisposta = range.getCell(iRiga, iColonna);
                if(rRisposta!=null && rRisposta.getValue()!=null) {
                    risposte.add(rRisposta.getValue().toString().trim());
                }
            }
            if(risposte.size() >= 2) {
                System.out.println("domanda: "+domanda);
                inCostruzione = new ModelloDomanda(iRiga+1, domanda.replace("\n", "\\n"));
                // la prima risposta è quella giusta
                inCostruzione.addRisposta( new ModelloRisposta (risposte.get(0), true, false) );
                risposte.remove(0);
                for(int i=0; i<risposte.size(); i++) {
                    inCostruzione.addRisposta( new ModelloRisposta (
                            risposte.get(i), 
                            false, 
                            i==risposte.size()-1
                    ));
                }
                compito.addDomanda(inCostruzione);
            }
        }
        return compito;
	}
}
