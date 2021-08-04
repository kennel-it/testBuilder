package it.aspix.scuola.test;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ControlloAiuto extends Stage {
	
	@FXML
	private TextArea esempioTesto;
	
    @FXML
    void initialize() {
        String x="";
        try {
            Scanner scanner;
            scanner = new Scanner( new InputStreamReader( this.getClass().getResourceAsStream("esempioFile.txt"),"UTF-8"));
            x = scanner.useDelimiter("\\z").next();
            scanner.close();
        } catch (UnsupportedEncodingException e1) {
            // piuttosto improbabile che UTF-8 non esista!
            e1.printStackTrace();
        }
        esempioTesto.setText(x);
    }

}
