package it.aspix.scuola.test;

import java.util.function.Consumer;

import it.aspix.scuola.test.compito.CompitoDomanda;
import it.aspix.scuola.test.svolgimento.SvolgimentoDomanda;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class PannelloCorrezioneDomanda extends GridPane{
    
    Label ordine = new Label("P");
    Label origine = new Label("[o]");
    ToggleButton risposte[];
    ToggleGroup gruppo = new ToggleGroup();
    int posizione;
    
    private CompitoDomanda compitoDomanda;
    private SvolgimentoDomanda svolgimentoDomanda;
    
    Consumer<Integer> cambiataRisposta;

    public PannelloCorrezioneDomanda(int pos, Consumer<Integer> cambiataRisposta, CompitoDomanda compitoDomanda, SvolgimentoDomanda svolgimentoDomanda) {
        super();
        this.posizione = pos;
        this.cambiataRisposta = cambiataRisposta;
        this.compitoDomanda = compitoDomanda;
        this.svolgimentoDomanda = svolgimentoDomanda;
        
        ordine.setText( ""+pos );
        ordine.setPrefWidth(50);
        origine.setText( "["+compitoDomanda.posizioneOriginale+"]" );
        origine.setPrefWidth(40);
        
        risposte = new ToggleButton[compitoDomanda.risposte.size()];
        for(int i=0; i<risposte.length; i++) {
            risposte[i] = new ToggleButton( ""+(char)('a'+i) );
            risposte[i].setOnAction( e-> clickRisposta(e) );
            risposte[i].setToggleGroup(gruppo);
        }
        
        this.setMaxWidth( Integer.MAX_VALUE );
        ordine.getStyleClass().add("ordine");
        origine.getStyleClass().add("origine");
        
        this.add(ordine, 0, 0);
        this.add(origine, 1, 0);
        for(int i=0; i<risposte.length; i++) {
            this.add(risposte[i], 2+i, 0);
        }
        aggiornaVisualizzazione();
    }
    
    private void clickRisposta(ActionEvent e) {
        ToggleButton sorgente = (ToggleButton) e.getSource(); 
        
        if( sorgente.isSelected() ) {
            svolgimentoDomanda.valore = sorgente.getText().charAt(0)-'a';
        }
        
        aggiornaVisualizzazione();
        cambiataRisposta.accept(posizione);
    }
    
    public void aggiornaRisposta(int n) {
        // rimuovo tutte le selezioni
        for(ToggleButton tb : risposte) {
            tb.setSelected(false);
        }
        if(n!=-1) {
            risposte[n].setSelected(true);
        }
        svolgimentoDomanda.valore = n;
        aggiornaVisualizzazione();
        cambiataRisposta.accept(posizione);
    }
    
    private void aggiornaVisualizzazione() {
        // pulizia!
        for(int i=0; i<risposte.length; i++) {
            risposte[i].getStyleClass().remove("giusta");
            risposte[i].getStyleClass().remove("sbagliata");
            risposte[i].getStyleClass().remove("sarebbeStataGiusta");
        }
        if(svolgimentoDomanda.valore!=-1) {
            if( compitoDomanda.risposte.get(svolgimentoDomanda.valore).giusta ) {
                risposte[svolgimentoDomanda.valore].getStyleClass().add("giusta");
            } else {
                risposte[svolgimentoDomanda.valore].getStyleClass().add("sbagliata");
                // allora cerco quella giusta
                for(int i=0; i<compitoDomanda.risposte.size(); i++) {
                    if( compitoDomanda.risposte.get(i).giusta ) {
                        risposte[i].getStyleClass().add("sarebbeStataGiusta");
                    }
                }
            }
        }
    }
    
}
