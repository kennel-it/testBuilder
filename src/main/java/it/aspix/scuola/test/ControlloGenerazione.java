package it.aspix.scuola.test;

import java.io.File;
import java.util.ArrayList;

import it.aspix.scuola.test.compito.Compito;
import it.aspix.scuola.test.io.WriterODT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControlloGenerazione {

    @FXML
    Label info1;

    @FXML
    private TextField numeroDiCompiti_0;
    @FXML
    private TextField numeroDiCompiti_1;
    @FXML
    private TextField numeroDiCompiti_2;
    private TextField numeroDiCompiti[] = new TextField[3];

    @FXML
    private CheckBox ridotto_0;
    @FXML
    private CheckBox ridotto_1;
    @FXML
    private CheckBox ridotto_2;
    private CheckBox ridotto[] = new CheckBox[3];

    @FXML
    private ComboBox<String> layout_0;
    @FXML
    private ComboBox<String> layout_1;
    @FXML
    private ComboBox<String> layout_2;
    private ComboBox<String>[] layout = (ComboBox<String>[]) new ComboBox[3];

    @FXML
    private TextField risultato;

    @FXML
    void initialize() {
        numeroDiCompiti[0] = numeroDiCompiti_0;
        numeroDiCompiti[1] = numeroDiCompiti_1;
        numeroDiCompiti[2] = numeroDiCompiti_2;
        ridotto[0] = ridotto_0;
        ridotto[1] = ridotto_1;
        ridotto[2] = ridotto_2;
        layout[0] = layout_0;
        layout[1] = layout_1;
        layout[2] = layout_2;
        for(ComboBox<String> l: layout) {
            l.getItems().addAll(
                    OrganizzazioneRisposte.BLOCK.toString(),
                    OrganizzazioneRisposte.INLINE.toString());
            l.setValue(OrganizzazioneRisposte.BLOCK.toString());
        }
        numeroDiCompiti[0].requestFocus();

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
        Mixer mescolatoreDiCompiti = new Mixer( LavoroAttuale.getModello() );
        int id = 0;
        ArrayList<Compito> cc = new ArrayList<>();
        for(int gruppo=0; gruppo <numeroDiCompiti.length; gruppo++) {
            boolean rid = ridotto[gruppo].isSelected();
            OrganizzazioneRisposte or = OrganizzazioneRisposte.cercaPerDescrizione(layout[gruppo].getValue());
            int nc = getNumero(numeroDiCompiti[gruppo]);
            for(int j=0 ; j< nc ; j++){
                Compito c = mescolatoreDiCompiti.getAnother(rid);
                c.id = id++;
                c.organizzazioneRisposte = or;
                cc.add(c);
            }
        }
        try {
            WriterODT.scrivi(fileCompiti, cc);
            LavoroAttuale.setCompiti(cc);
            LavoroAttuale.salvaCompiti( LavoroAttuale.getNomeFileCompiti() );
        } catch (Exception e) {
            e.printStackTrace();
            // FIXME: webEngine.loadContent("<pre>"+e.getMessage()+"</pre>");
        }
    }

}
