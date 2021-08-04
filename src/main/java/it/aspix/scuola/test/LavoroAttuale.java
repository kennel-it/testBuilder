package it.aspix.scuola.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

import it.aspix.scuola.test.compito.Compito;
import it.aspix.scuola.test.io.ReaderModelloOds;
import it.aspix.scuola.test.io.ReaderModelloTxt;
import it.aspix.scuola.test.modello.Modello;
import it.aspix.scuola.test.svolgimento.Svolgimento;
import it.aspix.scuola.test.svolgimento.SvolgimentoDomanda;

/************************************************************************************************
 * 
 * È una specie di stato globale ma funziona dando per assunto
 * che si lavora su un compito alla volta
 *
 ***********************************************************************************************/
public class LavoroAttuale {
    
    // un elenco di ascoltatori da chiamare quando cambia qualche nome di file
    private static ArrayList<Consumer<ParteCompito>> ascoltatoriCompitoCambiato = new ArrayList<Consumer<ParteCompito>>();
    // i nomi dei file in uso
    private static String nomeFileModello;
    private static String encodingFileModello = "UTF-8"; // default accettabile
    private static String nomeFileStampa;
    private static String nomeFileCompiti;
    private static String nomeFileSvolgimenti;
    private static String nomeFileStatistiche;    
    // gli oggetti che rappresentano le diverse fasi di lavorazione
    private static Modello modello;
    private static List<Compito> compiti;
    private static List<Svolgimento> svolgimenti;

    /********************************************************************************************
     * Il parametro che viene passato all'ascoltatore indica quale parte del compito è cambiato
     * 
     * @param ascoltatore verrà chiamato quando cambiano le informazioni sui compiti in uso
     *******************************************************************************************/
    public static void addAscoltatoreCompitoCambiato(Consumer<ParteCompito> ascoltatore) {
        ascoltatoriCompitoCambiato.add(ascoltatore);
    }
    
    private static void cambiatoPezzo(ParteCompito x) {
        for( var e: ascoltatoriCompitoCambiato ) {
            e.accept(x);
        }
    }
    
    public static String getNomeFileModello() { 
        return nomeFileModello; 
    }
    public static String getEncodingFileModello() {
        return encodingFileModello;
    }
    /********************************************************************************************
     * Carica le informazioni per generare un compito
     * 
     * @param nomeFileModello da cui caricare i dati
     * @throws IOException
     * @throws ContenutoProblematico se il documento presenta dei problemi 
     *******************************************************************************************/
    public static void caricaModello(String nomeFileModello, String encoding) throws IOException, ContenutoProblematico{
        LavoroAttuale.nomeFileModello = nomeFileModello;
        LavoroAttuale.encodingFileModello = encoding;
        File fileModello = new File(LavoroAttuale.nomeFileModello);
        modello = null;
        Modello vecchioModello = modello;
        Modello nuovoModello;
        // se il file esiste carico i dati
        if(fileModello.exists()) {
            if(nomeFileModello.toString().endsWith("ods")) {
                nuovoModello = ReaderModelloOds.leggi(fileModello, LavoroAttuale.encodingFileModello);
            } else {
                nuovoModello = ReaderModelloTxt.leggi(fileModello, LavoroAttuale.encodingFileModello);
            }
            if( nuovoModello.check().length()!=0 ){
                throw new ContenutoProblematico(nuovoModello.check());
            } else {
                System.out.println("caricato il modello dei compiti "+nuovoModello.sizeDomande());
            }
            if(nuovoModello != vecchioModello ) {
                LavoroAttuale.modello = nuovoModello;
                cambiatoPezzo(ParteCompito.MODELLO);
            }
        }
    }

    public static String getNomeFileStampa() {
        return nomeFileStampa;
    }
    public static void setNomeFileStampa(String nomeFileStampa) {
        LavoroAttuale.nomeFileStampa = nomeFileStampa;
    }

    public static String getNomeFileCompiti() {
        return nomeFileCompiti;
    }
    /********************************************************************************************
     * Carica dei compiti già generati in precedenza
     * 
     * @param nomeFileCompiti
     * @throws StreamReadException
     * @throws DatabindException
     * @throws IOException
     *******************************************************************************************/
    public static void caricaCompiti(String nomeFileCompiti) throws StreamReadException, DatabindException, IOException {
        LavoroAttuale.nomeFileCompiti = nomeFileCompiti;
        List<Compito> vecchiCompiti = compiti;
        
        File fileCompiti = new File(LavoroAttuale.nomeFileCompiti);
        if(fileCompiti.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Compito.class);
            compiti = objectMapper.readValue(fileCompiti, javaType);
            cambiatoPezzo(ParteCompito.COMPITO);
        } else {
            compiti = null;
            System.out.println("caricaCompiti: non ho il file dei compiti");
        }
        if(compiti != vecchiCompiti ) {
            cambiatoPezzo(ParteCompito.COMPITO);
        }
    }
    public static void salvaCompiti(String nomeFileCompiti) throws StreamWriteException, DatabindException, IOException {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File(nomeFileCompiti), compiti);
        cambiatoPezzo(ParteCompito.COMPITO);
    }

    public static String getNomeFileSvolgimenti() {
        return nomeFileSvolgimenti;
    }

    public static void caricaSvolgimenti(String nomeFileSvolgimenti) throws StreamReadException, DatabindException, IOException {
        LavoroAttuale.nomeFileSvolgimenti = nomeFileSvolgimenti;
        List<Svolgimento> vecchiSvolgimenti = svolgimenti;
        
        File input = new File( LavoroAttuale.getNomeFileSvolgimenti() );
        LavoroAttuale.svolgimenti = null;
        if(input.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Svolgimento.class);
            LavoroAttuale.svolgimenti = objectMapper.readValue(input, javaType);
        } else {
            System.out.println("::"+compiti);
            if( LavoroAttuale.compiti!=null ) {
                LavoroAttuale.svolgimenti = new ArrayList<>();
                for(int i=0; i<LavoroAttuale.compiti.size(); i++) {
                    Svolgimento s = new Svolgimento();
                    //  uno svolgimento corrisponde ad un compito
                    s.id = LavoroAttuale.compiti.get(i).id;
                    // e ha spazio per lo stesso numero di domande
                    for(int j=0; j<LavoroAttuale.compiti.get(i).domande.size(); j++) {
                        s.domande.add( new SvolgimentoDomanda() );
                    }
                    LavoroAttuale.svolgimenti.add(s);
                }
                salvaSvolgimenti(LavoroAttuale.nomeFileSvolgimenti);
            } else {
                svolgimenti = null;
                System.out.println("caricaSvolgimenti: non ho i compiti");
            }
        }
        if( svolgimenti != vecchiSvolgimenti ) {
            cambiatoPezzo(ParteCompito.SVOLGIMENTO);
        }
    }
    
    public static void salvaSvolgimenti(String nomeFileSvolgimenti) {
        LavoroAttuale.nomeFileSvolgimenti = nomeFileSvolgimenti;
        File output = new File( LavoroAttuale.nomeFileSvolgimenti );
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        
        try {
            objectMapper.writeValue(output, LavoroAttuale.svolgimenti);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    public static String getNomeFileStatistiche() {
        return nomeFileStatistiche;
    }

    public static void setNomeFileStatistiche(String nomeFileStatistiche) {
        LavoroAttuale.nomeFileStatistiche = nomeFileStatistiche;
    }

    public static Modello getModello() {
        return modello;
    }

    public static void setModello(Modello modello) {
        LavoroAttuale.modello = modello;
    }

    public static List<Svolgimento> getSvolgimenti() {
        return svolgimenti;
    }

    public static void setSvolgimenti(List<Svolgimento> svolgimenti) {
        LavoroAttuale.svolgimenti = svolgimenti;
    }

    public static List<Compito> getCompiti() {
        return compiti;
    }

    public static void setCompiti(List<Compito> compiti) {
        LavoroAttuale.compiti = compiti;
    }
    
    public static Compito getCompito(int i) {
        return compiti.get(i);
    }
    
    public static final Compito getCompitoPerId(int id) {
        for(Compito c: compiti) {
            if( c.id == id ) {
                return c;
            }
        }
        return null;
    }
    
    public static final Svolgimento getSvolgimentoPerId(int id) {
        for(Svolgimento s: svolgimenti) {
            if( s.id == id ) {
                return s;
            }
        }
        return null;
    }
}
