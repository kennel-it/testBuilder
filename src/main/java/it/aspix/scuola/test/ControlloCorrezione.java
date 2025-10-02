package it.aspix.scuola.test;

import java.util.function.Consumer;

import it.aspix.scuola.test.compito.Compito;
import it.aspix.scuola.test.svolgimento.Svolgimento;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ControlloCorrezione {

    @FXML
    ToggleButton suono;
    @FXML
    ComboBox<Integer> selezioneCompito;
    @FXML
    private TextField valBase;
    @FXML
    private TextField valGiuste;
    @FXML
    private TextField valSbagliate;
    @FXML
    private Label punteggio;
    @FXML
    private ScrollPane scrollDomande;
    @FXML
    private Pane pannelloDomande;
    @FXML
    private Label etichettaRisposteGiuste;
    @FXML
    private Label etichettaRisposteSbagliate;
    @FXML
    private Label etichettaRisposteNonDate;

    private PannelloCorrezioneDomanda[] pannelliCorrezione;
    private int domandaInCorrezione;

    private Valutatore valutatore = new Valutatore();

    @FXML
    void initialize() {
        clickSuono();
        valoriCambiati();
        LavoroAttuale.addAscoltatoreCompitoCambiato( x -> {
            if( x == ParteCompito.SVOLGIMENTO ) {
                selezioneCompito.getItems().clear();
                for(Svolgimento s: LavoroAttuale.getSvolgimenti()) {
                    selezioneCompito.getItems().add(s.id);
                }
            }
        });
    }

    @FXML
    private void clickSuono() {
        if(suono.isSelected()) {
            suono.setText("suono on");
            SupportoAudio.acceso = true;
        } else {
            suono.setText("suono off");
            SupportoAudio.acceso = false;
        }
    }

    Consumer<Integer> cambiataRisposta = indiceRispostaCambiata -> {
        // in verità l'indice della risposta non serve
        try {
            punteggio.setText(String.format("%.2f", valutatore.getPunteggio()));
            etichettaRisposteGiuste.setText("giuste: "+
                    String.format("%2d",valutatore.getContatoreGiuste()));
            etichettaRisposteSbagliate.setText("sbagliate: "+
                    String.format("%2d",valutatore.getContatoreSbagliate()));
            etichettaRisposteNonDate.setText("non date: "+
                    String.format("%2d",valutatore.getContatoreNonDate()));
        } catch(Exception ex) {
            punteggio.setText("?");
        }
    };

    @FXML
    public void valoriCambiati() {
        try {
            valutatore.setValoreBase( Double.parseDouble(valBase.getText()) );
        }catch(Exception ex) { /* pazienza resterà zero */  };
        try {
            valutatore.setValoreGiusta( Double.parseDouble(valGiuste.getText()) );
        }catch(Exception ex) { /* pazienza resterà zero */  };
        try {
            valutatore.setValoreSbagliata( Double.parseDouble(valSbagliate.getText()) );
        }catch(Exception ex) { /* pazienza resterà zero */  };
        cambiataRisposta.accept(-1);
    }

    @FXML
    public void selezionatoCompito() {
        int id = selezioneCompito.getValue();

        Compito compito = LavoroAttuale.getCompitoPerId(id);
        Svolgimento svolgimento = LavoroAttuale.getSvolgimentoPerId(id);

        pannelloDomande.getChildren().clear();
        pannelliCorrezione = new PannelloCorrezioneDomanda[ compito.domande.size() ];
        for(int i=0 ; i<compito.domande.size() ; i++) {
            pannelliCorrezione[i] = new PannelloCorrezioneDomanda(
                    i,
                    cambiataRisposta,
                    compito.domande.get(i),
                    svolgimento.domande.get(i)
            );
            pannelliCorrezione[i].getProperties().put("numeroDomanda", i);
            pannelliCorrezione[i].setOnMouseClicked( (MouseEvent e) -> {
                domandaInCorrezione = (int) ((PannelloCorrezioneDomanda)e.getSource()).getProperties().get("numeroDomanda");
                mostraSelezionato();
            });
            pannelloDomande.getChildren().add( pannelliCorrezione[i] );
            pannelliCorrezione[i].getStyleClass().add("box");
        }
        valutatore.setCompito(compito);
        valutatore.setSvolgimento(svolgimento);
        domandaInCorrezione = 0;
        mostraSelezionato();
    }

    private void mostraSelezionato() {
        double vValue;
        if(pannelliCorrezione[0].getHeight()==0) {
            // al primo giro l'altezza è zero perché viene chiamato prima che
            // i pannelli vengano disegnati, poco importa tanto all'inizio
            // la posizione zero in verticale è quello che serve
            vValue = 0;
        } else {
            double elementiPresenti = scrollDomande.getHeight() / pannelliCorrezione[0].getHeight();
            // -2 perché voglio vedere due domande sopra
            vValue = (domandaInCorrezione-2)/(pannelliCorrezione.length-elementiPresenti);
        }
        scrollDomande.setVvalue( vValue );

        for(int i=0 ; i<pannelliCorrezione.length ; i++) {
            pannelliCorrezione[i].getStyleClass().clear();
            pannelliCorrezione[i].getStyleClass().add("box");
            if(i==domandaInCorrezione) {
                pannelliCorrezione[i].getStyleClass().add("box-edit");
            } else {
                pannelliCorrezione[i].getStyleClass().add("box-plain");
            }
        }
        System.out.println("SELEZIONATO");
    }

    @FXML
    public void salvaSvolgimenti() {
        LavoroAttuale.salvaSvolgimenti( LavoroAttuale.getNomeFileSvolgimenti() );
    }

    @FXML
    private void tastoPremuto(KeyEvent ev) {
        String carattere = ev.getCharacter();
        System.out.println(carattere);

        if( carattere.length()==1 && " abcdef".indexOf(carattere)!=-1 ) {
            int numero = ev.getCharacter().charAt(0)-'a';
            if(numero == ' '-'a') {
                numero = -1;
            }
            pannelliCorrezione[ domandaInCorrezione ].aggiornaRisposta(numero);
            switch( valutatore.getStatoRisposta(domandaInCorrezione) ) {
            case GIUSTA:
                SupportoAudio.play( SupportoAudio.SI );
                break;
            case NON_DATA:
                SupportoAudio.play( SupportoAudio.BIANCA );
                break;
            case SBAGLIATA:
                SupportoAudio.play( SupportoAudio.NO );
                break;
            }

            domandaInCorrezione++;
            if(domandaInCorrezione>=pannelliCorrezione.length) {
                domandaInCorrezione = pannelliCorrezione.length-1;
                SupportoAudio.play( SupportoAudio.FINE );
            } else {
                SupportoAudio.play( SupportoAudio.NUMERO[domandaInCorrezione] );
            }
            mostraSelezionato();
        }
    }

    private void calcolaValoriGiusteSbagliate(double frazioneSbagliata) {
        if( LavoroAttuale.getCompiti()!=null ) {
            double intervallo = 10 - Double.parseDouble( valBase.getText() );
            double numeroDomande = LavoroAttuale.getCompiti().get(0).domande.size();
            System.out.println("numero domande: "+numeroDomande);
            double giusta = intervallo/numeroDomande;
            double sbagliata = giusta*frazioneSbagliata;
            valGiuste.setText(""+giusta);
            valSbagliate.setText(""+sbagliata);
            valoriCambiati();
        } else {
            Alert dialogoAllerta = new Alert(AlertType.ERROR, "Devi prima caricare un compito per poter calcolare i parametri.");
            dialogoAllerta.showAndWait();
        }
    }

    @FXML
    public void calcolaPunteggioErrateZero() {
        calcolaValoriGiusteSbagliate(0);
    }

    @FXML
    public void calcolaPunteggioErrateMenoUnTerzo() {
        calcolaValoriGiusteSbagliate(-1/3.0);
    }

}
