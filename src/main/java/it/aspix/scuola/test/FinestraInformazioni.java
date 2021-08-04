package it.aspix.scuola.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FinestraInformazioni extends Stage {
    
    @FXML
    TextArea informazioni;
    @FXML
    Label stato;
    
    private Exception eccezione;
    
    @FXML 
    public void clickSuStato() {
        StringWriter x = new StringWriter();
        PrintWriter pw = new PrintWriter(x);
        eccezione.printStackTrace(pw);
        informazioni.setText(x.getBuffer().toString());
    }
 
    public FinestraInformazioni(String descrizione, Exception eccezione){
        FXMLLoader fxmlLoader = new FXMLLoader();
        this.eccezione = eccezione;
        try {
            fxmlLoader.setController(this);
            Scene scena = new Scene(fxmlLoader.load(getClass().getResource("finestraInformazioni.fxml").openStream()));
            setScene(scena);
            informazioni.setText(descrizione);
            stato.setText( eccezione.getClass().getCanonicalName() );
            setTitle("ci sono dei problemi!");
        } catch (Exception e) {
            Label testo = new Label(e.getLocalizedMessage());
            Scene scene = new Scene(new BorderPane(testo),200,100);
            setTitle("orca miseria!"); 
            setScene(scene); 
        }          
        if(descrizione==null) {
            // se non c'è una descrizione stampo lo stack trace
            clickSuStato();
        }
        this.show();
    }
}
