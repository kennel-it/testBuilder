package it.aspix.scuola.test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Versione {
    public static String getVersione() {
        return leggiInteroFile("versione.txt", "V?.?");
    }
    
    private static String leggiInteroFile(String nome, String predefinito) {
        try {
            InputStream is = Versione.class.getResourceAsStream( nome );
            InputStreamReader isr = new InputStreamReader(is);
            int numero;
            char buffer[] = new char[20];
            numero = isr.read(buffer);
            isr.close();
            is.close();
            return new String(buffer,0,numero);
        }catch(Exception ex) {
            return predefinito;
        }
    }
}
