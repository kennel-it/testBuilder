package it.aspix.scuola.test;

import java.awt.Image;
import java.awt.Taskbar;
import java.io.IOException;

import javax.imageio.ImageIO;

import it.aspix.scuola.test.io.NomiFileCorrelati;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

public class Main extends Application {
	
    @FXML
    TextField nomeFileModello;
    @FXML
    TextField nomeFileStampa;
    @FXML
    TextField nomeFileCompiti;
    @FXML
    TextField nomeFileSvolgimenti;
    @FXML
    TextField nomeFileStatistiche;
    @FXML
    ComboBox<String> encoding;

    @FXML
    private void initialize() {
        // imposto i possibili encoding
        encoding.getItems().addAll("UTF-8","ISO8859-1");
        encoding.setValue("UTF-8");

        LavoroAttuale.addAscoltatoreCompitoCambiato( e -> {
            nomeFileModello.getStyleClass().remove("errore");
            nomeFileCompiti.getStyleClass().remove("errore");
            nomeFileSvolgimenti.getStyleClass().remove("errore");

            nomeFileModello.getStyleClass().add( LavoroAttuale.getModello()!=null ? "buono" : "errore");
            nomeFileCompiti.getStyleClass().add( LavoroAttuale.getCompiti()!=null ? "buono" : "errore");
            nomeFileSvolgimenti.getStyleClass().add( LavoroAttuale.getSvolgimenti()!=null ? "buono" : "errore");
        });

        try {
            Taskbar tb = Taskbar.getTaskbar();
            Image i = ImageIO.read(getClass().getResourceAsStream("icona.png"));
            tb.setIconImage(i);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    /********************************************************************************************
     * Accetto tutti i eventi drag
     *******************************************************************************************/
    @FXML
    private void dragOverFileDomande(DragEvent event) {
        if(event.getGestureSource()!=nomeFileModello && event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }
    
    /********************************************************************************************
     * Accetto l'evento drop e prendo il primo file
     *******************************************************************************************/
    @FXML
    private void dropFileDomande(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            try{
                // calcolo e stampo tutti i nomi dei diversi file
                NomiFileCorrelati nfc = new NomiFileCorrelati(db.getFiles().get(0).toString());
                nomeFileModello.setText( nfc.modello );
                nomeFileStampa.setText( nfc.stampa );
                nomeFileCompiti.setText( nfc.correzione );
                nomeFileSvolgimenti.setText( nfc.risposte );
                nomeFileStatistiche.setText( nfc.statistiche );
                // carico tutte le informazioni possibili
                aggiornaNomeFileDaStampare();
                aggiornaNomeFileStatistiche();
                try {
                    LavoroAttuale.caricaModello( nomeFileModello.getText(), encoding.getValue() );
                    LavoroAttuale.caricaCompiti( nomeFileCompiti.getText());
                    LavoroAttuale.caricaSvolgimenti( nomeFileSvolgimenti.getText() );
                } catch (IOException e) {
                    new FinestraInformazioni(null, e);
                } catch (ContenutoProblematico e) {
                    new FinestraInformazioni(e.getDettagliProblema(), e);
                }

                success = true;
            }catch(Exception ve){
                new FinestraInformazioni(null, ve);
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
    
    @FXML
    private void cambiatoEncoding() {
        // FIXME
    }
    
    @FXML
    private void aggiornaNomeFileDaStampare() {
        LavoroAttuale.setNomeFileStampa( nomeFileStampa.getText() );
    }
    
    @FXML
    private void aggiornaNomeFileStatistiche() {
        LavoroAttuale.setNomeFileStatistiche( nomeFileStatistiche.getText() );
    }
    
    /********************************************************************************************
     * Avvio dell'applicazione
     *******************************************************************************************/
    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scena = new Scene( FXMLLoader.load(Main.class.getResource("main.fxml")) );
        primaryStage.setTitle("Test time! "+Versione.getVersione());
        primaryStage.setScene(scena);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
