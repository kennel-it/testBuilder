package it.aspix.scuola.test.compito;

import it.aspix.scuola.test.OrganizzazioneRisposte;

import java.util.ArrayList;

public class CompitoDomanda {

    public String testo;
    public boolean ridotto;
    public int posizioneOriginale; // zero based
    public ArrayList<CompitoRisposta> risposte;

    public CompitoDomanda(){
        super();
    }

    public CompitoDomanda(String domanda){
        super();
        this.testo = domanda;
        risposte = new ArrayList<>();
    }

    public void addRisposta(CompitoRisposta cr) {
        risposte.add(cr);
    }

    // un "-" poi la posizione originale della domanda poi "["
    // poi una sequenza di lettere che rappresentano la posizione originale
    // della risposta (a è zero, b uno...), la lettera è maiuscola per la
    // risposta esatta, in fine un "]"
    public String toString(){
        CompitoRisposta cr;
        StringBuilder sb = new StringBuilder();
        sb.append("-"+posizioneOriginale);
        sb.append('[');
        for(int i=0; i<risposte.size(); i++){
            cr = risposte.get(i);
            if(cr.giusta){
                sb.append((char)('A'+cr.posizioneOriginale));
            }else{
                sb.append((char)('a'+cr.posizioneOriginale));
            }
        }
        sb.append(']');
        return sb.toString();
    }

    private static final String formatta(String x){
    	String out = x.replace("\\n", "<text:line-break/>").replace("  ", "<text:s text:c=\"2\"/>");
    	String daConfronto;
    	do{
    		daConfronto=out;
    		int rimpiazzi = (out.length() - out.replace("##", "").length())/2;
    		if( rimpiazzi>=2 ) {
    		    out=out.replaceFirst("##","<text:span text:style-name=\"codice\">");
    		    out=out.replaceFirst("##","</text:span>");
    		}
    	}while(!daConfronto.equals(out));
    	return out; 
    }
    
    public String toODT(int numero, OrganizzazioneRisposte modello){
        StringBuilder sb=new StringBuilder();

        sb.append("<text:p text:style-name=\"domanda\">");
        sb.append(numero+": "+formatta(replaceXML(testo)));
        sb.append("</text:p>\n");
        if(modello==OrganizzazioneRisposte.BLOCK || 
           ( modello==OrganizzazioneRisposte.AUTO && ridotto ) ){
            sb.append("<text:p text:style-name=\"risposta\">");
            for(int i=0 ; i<risposte.size(); i++){
                sb.append((char)('a'+i)+") ");
                sb.append(formatta(replaceXML(risposte.get(i).testo)));
                sb.append("<text:line-break/>");
            }
            sb.append("</text:p>\n");
        }else{
            sb.append("<text:p text:style-name=\"risposta\">");
            sb.append(" ");
            for(int i=0 ; i<risposte.size(); i++){
                sb.append("<text:span text:style-name=\"rispostainlinea\">");
                sb.append(" ["+((char)('a'+i))+"]\u00a0");  // \u00a0 = no-break space
                sb.append(formatta(replaceXML(risposte.get(i).testo).replace(" ","\u00a0")));
                sb.append("</text:span> ");
            }
            sb.append("</text:p>\n");
        }
        return sb.toString();
    }

    private String replaceXML(String x){
        return x.replace("&", "&amp;").replace(">", "&gt;").replace("<", "&lt;");
    }
}
