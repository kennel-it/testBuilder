package it.aspix.scuola.test.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import it.aspix.scuola.test.compito.Compito;

public class WriterODT {

    private static final String fileDaInserire[]={
        "META-INF/manifest.xml",
        "mimetype",
        "styles.xml",
        // "content.xml"     questo lo genera il programma
    };

    public static void scrivi(File fileDestinazioneOdt, ArrayList<Compito> compiti) throws Exception{
        StringBuilder content = new StringBuilder();
        StringBuilder contentSoluzioni = new StringBuilder();

        content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
        "<office:document-content "+
        "xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\"\n"+
        "xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\"\n"+
        "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\"\n"+
        "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\"\n"+
        "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\"\n"+
        "xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\"\n"+
        "xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"+
        "xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\"\n"+
        "xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\"\n"+
        "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\"\n"+
        "xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\"\n"+
        "xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\"\n"+
        "xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\"\n"+
        "xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\"\n"+
        "xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\"\n"+
        "xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\"\n"+
        "xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"+
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rpt=\"http://openoffice.org/2005/report\"\n"+
        "xmlns:of=\"urn:oasis:names:tc:opendocument:xmlns:of:1.2\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"\n"+
        "xmlns:grddl=\"http://www.w3.org/2003/g/data-view#\" xmlns:tableooo=\"http://openoffice.org/2009/table\"\n"+
        "xmlns:field=\"urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0\"\n"+
        "xmlns:formx=\"urn:openoffice:names:experimental:ooxml-odf-interop:xmlns:form:1.0\"\n"+
        "xmlns:css3t=\"http://www.w3.org/TR/css3-text/\" office:version=\"1.2\">\n"+
        "<office:scripts />\n"+
        "<office:font-face-decls>\n"+
        "<style:font-face style:name=\"Times New Roman\" svg:font-family=\"&apos;Times New Roman&apos;\" style:font-family-generic=\"roman\" style:font-pitch=\"variable\" />\n"+
        "<style:font-face style:name=\"Arial\" svg:font-family=\"Arial\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\" />\n"+
        "<style:font-face style:name=\"Arial Unicode MS\" svg:font-family=\"&apos;Arial Unicode MS&apos;\" style:font-family-generic=\"system\" style:font-pitch=\"variable\" />\n"+
        "</office:font-face-decls>\n"+
        "<office:automatic-styles>\n"+
        "<style:style style:name=\"P1\" style:family=\"paragraph\" style:parent-style-name=\"risposta\">\n"+
        "<style:paragraph-properties fo:break-before=\"page\" />\n"+
        "</style:style>\n"+
        "<style:style style:name=\"T1\" style:family=\"text\">\n"+
        "<style:text-properties style:text-position=\"super 58%\" />\n"+
        "</style:style>\n"+
        "</office:automatic-styles>\n"+
        "<office:body>\n"+
        "<office:text text:use-soft-page-breaks=\"true\">\n"+
        "<text:sequence-decls>\n"+
        "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\" />\n"+
        "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\" />\n"+
        "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\" />\n"+
        "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\" />\n"+
        "</text:sequence-decls>\n");
        for(int j=0;j<compiti.size();j++){
            Compito c = compiti.get(j);
            contentSoluzioni.append((j+1)+": "+c.toString()+"\n");
            content.append("<text:p text:style-name=\"intestazione\">Compito numero "+(c.id)+": nome ________________________</text:p>");
            content.append(c.toODT(c.organizzazioneRisposte));
            content.append("<text:p text:style-name=\"Standard\" />\n");
        }
        content.append(
        "</office:text>\n"+
        "</office:body>\n"+
        "</office:document-content>");

        ByteArrayOutputStream bOS = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bOS, "UTF-8");
        writer.write(content.toString());
        writer.close();
        bOS.close();
        byte[] buffer = bOS.toByteArray();

        InputStream is;
        FileOutputStream fos = new FileOutputStream(fileDestinazioneOdt);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for(int i=0 ; i<fileDaInserire.length ; i++){
            is = WriterODT.class.getResourceAsStream("odt/"+fileDaInserire[i]);
            addEntry(zos, fileDaInserire[i], is);
        }
        addEntry(zos,"content.xml",new ByteArrayInputStream(buffer));
        zos.close();
        fos.close();
    }

    /************************************************************************
     * l'InputStream da cui leggere i dati viene chiuso alla fine 
     * @param zos l'OutputStream zippato
     * @param nomeEntrata il nome che il file dovrà avere
     * @param inputStream da cui leggere i dati
     * @throws IOException
     ***********************************************************************/
    private static final void addEntry(ZipOutputStream zos, String nomeEntrata, InputStream inputStream) throws IOException{
        byte buffer[] = new byte[1000];
        int datiLetti;
        zos.putNextEntry(new ZipEntry(nomeEntrata));
        do{
            datiLetti = inputStream.read(buffer);
            if(datiLetti!=-1)
                zos.write(buffer,0,datiLetti);
        }while(datiLetti!=-1);      
        inputStream.close();
    }
}
