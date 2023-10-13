package it.aspix.scuola.test;

import java.util.Random;

import it.aspix.scuola.test.compito.Compito;
import it.aspix.scuola.test.compito.CompitoDomanda;
import it.aspix.scuola.test.compito.CompitoRisposta;
import it.aspix.scuola.test.modello.Modello;
import it.aspix.scuola.test.modello.ModelloDomanda;
import it.aspix.scuola.test.modello.ModelloRisposta;

public class Mixer {
	private Modello sorgente;

	public Mixer(Modello sorgente) {
		super();
		this.sorgente = sorgente;
	}
	
	public Compito getAnother(boolean elimaEliminabile){
		ModelloDomanda sDomanda;
		ModelloRisposta sRisposta;
		Compito cCompito;
		CompitoDomanda cDomanda;
		CompitoRisposta cRisposta;
		Random caso = new Random();
		int estratto;
		
		// copio
		cCompito = new Compito();
		cCompito.ridotto = elimaEliminabile;
		for(int iDomanda = 0; iDomanda<sorgente.sizeDomande(); iDomanda++){
			sDomanda = sorgente.getDomanda(iDomanda);
			cDomanda = new CompitoDomanda(sDomanda.getTesto() );
			cDomanda.ridotto = elimaEliminabile;
			cDomanda.posizioneOriginale = iDomanda;
			for(int iRisposta = 0; iRisposta<sDomanda.size(); iRisposta++){
				sRisposta = sDomanda.getRisposta(iRisposta);
				cRisposta = new CompitoRisposta(
				        sRisposta.testo(),
				        iRisposta,
				        sRisposta.giusta() 
				);
				if(!elimaEliminabile || !sRisposta.eliminabile() ) {
				    cDomanda.risposte.add( cRisposta );
				}
			}
			cCompito.domande.add(cDomanda);
		}
		// mixo le risposte
		for(CompitoDomanda x: cCompito.domande){
			for(int iRisposta = 0; iRisposta<x.risposte.size(); iRisposta++){
				estratto = caso.nextInt( x.risposte.size() );
				// ne tolgo una a caso
				cRisposta = x.risposte.get(estratto);
				x.risposte.remove(estratto);
				// e la inserisco in testa
				x.risposte.add(0, cRisposta);
			}
		}
		// mixo le domande
		for(int iDomanda = 0; iDomanda<cCompito.domande.size(); iDomanda++){
			estratto = caso.nextInt( cCompito.domande.size() );
			cDomanda = cCompito.domande.get(estratto);
			cCompito.domande.remove(estratto);
			cCompito.domande.add(0, cDomanda);
		}
		return cCompito;
	}
}
