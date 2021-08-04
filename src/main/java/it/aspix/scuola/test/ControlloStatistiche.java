package it.aspix.scuola.test;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import it.aspix.scuola.test.compito.Compito;
import it.aspix.scuola.test.modello.Modello;
import it.aspix.scuola.test.modello.ModelloDomanda;
import it.aspix.scuola.test.svolgimento.Svolgimento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public class ControlloStatistiche {
    
    @FXML
    Button ricalcola;
    @FXML
    Button salvaPNG;
    @FXML
    Pane contenuto;
    @FXML 
    ToggleButton etichette;
    @FXML
    ToggleButton legenda;
    
    @FXML 
    private void aggiornaStatistiche() {
        contenuto.getChildren().clear();
        Compito compito;
        Svolgimento svolgimento;
        Valutatore v;
        String indice;
        
        // la chiave è domanda#risposta compresa la risposta "-1" non data 
        // il valore è quanti l'hanno data
        HashMap<String, Integer> totali = new HashMap<>();
        // inizializzo tutti i valori a zero
        Modello modello = LavoroAttuale.getModello();
        for(int i=0 ; i<modello.sizeDomande() ; i++) {
            ModelloDomanda md = modello.getDomanda(i);
            for(int indiceDomanda=0; indiceDomanda<md.size() ; indiceDomanda++) {
                indice = i+"#"+indiceDomanda;
                totali.put(indice, 0);
                System.out.println(">>"+indice);
            }
            totali.put(i+"#-1", 0);
        }
        
        for( int i=0; i<LavoroAttuale.getCompiti().size(); i++) {
            compito = LavoroAttuale.getCompiti().get(i);
            svolgimento = LavoroAttuale.getSvolgimentoPerId( compito.id );
            v = new Valutatore(compito, svolgimento);
            String messaggio = "";
            int risposte[] = v.getSequenzaInOriginale();
            for( int indiceDomanda=0; indiceDomanda< risposte.length; indiceDomanda++ ) {
                messaggio += risposte[indiceDomanda] + " ";
                indice = indiceDomanda+"#"+risposte[indiceDomanda];
                System.out.println(indice);
                totali.put(indice, totali.get(indice)+1);
            }
            System.out.println(messaggio);
        }
        
        ObservableList<PieChart.Data> dati;
        PieChart torta;
        for(int i=0 ; i<modello.sizeDomande() ; i++) {
            ModelloDomanda md = modello.getDomanda(i);
            System.out.println(md.getTesto());
            indice = i+"#-1";
            
            contenuto.getChildren().add( new Label( md.getTesto() ));
            dati = FXCollections.observableArrayList();
            
            for(int indiceRisposta=0; indiceRisposta<md.size() ; indiceRisposta++) {
                indice = i+"#"+indiceRisposta;
                System.out.println("  "+md.getRisposta(indiceRisposta).testo()+" -> "+totali.get(indice));
                if( totali.get(indice)>0 ) {
                    String etichetta = md.getRisposta(indiceRisposta).testo()+" ["+totali.get(indice)+"]";
                    dati.add( new PieChart.Data(etichetta, totali.get(indice)) );
                }
            }
            torta = new PieChart( dati );
            torta.setLegendVisible( legenda.isSelected() );
            torta.setLabelsVisible( etichette.isSelected() );
            contenuto.getChildren().add( torta );
        }
        
    }
    
    @FXML 
    private void salvaStatistiche() {
        WritableImage immagine = contenuto.snapshot(new SnapshotParameters(), null);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(immagine, null);
        try {
            ImageIO.write(renderedImage, "png", new File(LavoroAttuale.getNomeFileStatistiche() ));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
