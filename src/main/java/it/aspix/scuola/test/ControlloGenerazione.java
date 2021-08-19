package it.aspix.scuola.test;

import java.io.File;
import java.util.ArrayList;

import it.aspix.scuola.test.compito.Compito;
import it.aspix.scuola.test.io.WriterODT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControlloGenerazione {
	
    @FXML
    private Label info1;
    @FXML
    private TextField numeroDiCompitiCompleti;
    @FXML
    private TextField numeroDiCompitiRidotti;
    @FXML
    private ComboBox<String> layout;
    @FXML
    private TextField risultato;
    
    @FXML
    void initialize() {
        layout.getItems().addAll(OrganizzazioneRisposte.BLOCK.toString(),OrganizzazioneRisposte.INLINE.toString());
        layout.setValue(OrganizzazioneRisposte.BLOCK.toString());
        numeroDiCompitiCompleti.requestFocus();
        
        LavoroAttuale.addAscoltatoreCompitoCambiato( x -> {
            if( x == ParteCompito.MODELLO ) {
                info1.setText("Il modello in uso contiene "+LavoroAttuale.getModello().sizeDomande()+" domande");
            }
        });
    }
    
    private final int getNumero(TextField tf) {
        try {
            return Integer.parseInt( tf.getText() );
        }catch(Exception ex) {
            return 0;
        }
    }
    
    @FXML
    private void generaTest(ActionEvent event){
        File fileCompiti = new File( LavoroAttuale.getNomeFileStampa() );
        int id = 0;
        try {
            Mixer mescolatoreDiCompiti = new Mixer( LavoroAttuale.getModello() );
            ArrayList<Compito> cc = new ArrayList<>();
            for(int j=0 ; j<getNumero(numeroDiCompitiRidotti) ; j++){
                Compito c = mescolatoreDiCompiti.getAnother(true);
                c.id = id++;
                cc.add(c);
            }
            for(int j=0 ; j<getNumero(numeroDiCompitiCompleti) ; j++){
                Compito c = mescolatoreDiCompiti.getAnother(false); 
                c.id = id++;
                cc.add(c);
            }
            WriterODT.scrivi(fileCompiti, cc, OrganizzazioneRisposte.cercaPerDescrizione(layout.getValue()));
            LavoroAttuale.setCompiti(cc);
            LavoroAttuale.salvaCompiti( LavoroAttuale.getNomeFileCompiti() );
        } catch (Exception e) {
            e.printStackTrace();
            // FIXME: webEngine.loadContent("<pre>"+e.getMessage()+"</pre>");
        }
    }
    
}
