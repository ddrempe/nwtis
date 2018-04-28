/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.kontrole;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.web.dretve.ObradaPoruka;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.damdrempe.web.zrna.SlanjePoruka;

/**
 *
 * @author ddrempetic
 */
public class PomocnaKlasa {

    public static String ProcitajSadrzajJsonDatoteke(String putanja) {
        File dat = new File(putanja);

        if (!dat.exists() || dat.isDirectory()) {
            System.out.println("Ne postoji datoteka ili je mapa.");
        }

        Gson gson = new Gson();
        String sadrzaj = "";
        try {
            JsonElement json = gson.fromJson(new FileReader(dat), JsonElement.class);
            sadrzaj = gson.toJson(json);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlanjePoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sadrzaj;
    }

    public static boolean ProvjeriSintaksuPrivitka(String tekstPrivitka) {
        String pattern = "\\{(\"id\"): ?(\\d{1,4}), ?(\"komanda\"): ?(\"dodaj\"|\"azuriraj\"), ?((?:\"[a-zA-Z ]{1,30}\": ?(?:[0-9]{1,3}|[0-9]{1,3}\\.[0-9]{1,2}|\"[a-zA-Z ]{1,30}\"),\\s?){1,5}) ?(\"vrijeme\"): ?\"([0-9]{4}\\.[0-9]{2}\\.[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}){1,3}\"\\}";
        boolean rezultat;
        rezultat = tekstPrivitka.matches(pattern);
        //TODO odkomentirati provjeru regexa
        //rezultat = true;
        
        return rezultat;
    }

    public static PrivitakInformacije IspitajDatotekuPrivitka(Message message, String trazeniNazivPrivitka) {
        //TODO provjeriti "text/json" ili "application/json"
        ServletContext sc = SlusacAplikacije.servletContext;
        String webInfPutanja = sc.getRealPath("/WEB-INF/");
        String putanjaPrivitaka = webInfPutanja + File.separator + "privici";

        File directory = new File(putanjaPrivitaka);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String saveDirectory = "";
        int brojPrivitaka = 0;
        String fileName = "";
        PrivitakInformacije privitakInformacije = new PrivitakInformacije();
        privitakInformacije.nwtisPoruka = Poruka.VrstaPoruka.neNWTiS_poruka;
        privitakInformacije.privitakSadrzaj = "";

        try {
            String attachFiles = "";
            String contentType = message.getContentType();
            String messageContent = "";
            if (contentType.contains("multipart")) {
                // content may contain attachments
                if (message.getContent() instanceof Multipart == false) {
                    System.out.println("Nije moguce citati privitak za poruku " + message.getMessageNumber());
                    
                    return privitakInformacije;
                }
                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        // this part is attachment
                        fileName = part.getFileName();
                        attachFiles += fileName + ", ";
                        saveDirectory = putanjaPrivitaka + File.separator + fileName;

                        if (fileName.contains(":")) {
                            continue;
                        }

                        part.saveFile(saveDirectory);
                        brojPrivitaka++;
                    } else {
                        // this part may be the message content
                        messageContent = part.getContent().toString();
                    }
                }
                if (attachFiles.length() > 1) {
                    attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                }

            } else if (contentType.contains("text/plain")
                    || contentType.contains("text/html")) {
                Object content = message.getContent();
                if (content != null) {
                    messageContent = content.toString();
                }
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (brojPrivitaka == 1 && fileName.equalsIgnoreCase(trazeniNazivPrivitka)) {
            privitakInformacije.privitakSadrzaj = PomocnaKlasa.ProcitajSadrzajJsonDatoteke(saveDirectory);
            privitakInformacije.nwtisPoruka =  Poruka.VrstaPoruka.NWTiS_poruka;
        }

        return privitakInformacije;
    }

    public static Poruka ProcitajPoruku(Message poruka, String trazeniNazivPrivitka) throws MessagingException, IOException {
        MimeMessage message = (MimeMessage) poruka;
        String id = message.getMessageID();

        Date vrijemeSlanja = message.getSentDate();
        if (vrijemeSlanja == null) {
            vrijemeSlanja = new Date(0);
        }
        
        PrivitakInformacije privitakInformacije = IspitajDatotekuPrivitka(poruka, trazeniNazivPrivitka);

        Date vrijemePrijema = message.getReceivedDate();
        String salje = message.getFrom()[0].toString();
        String predmet = message.getSubject();
        String privitak = privitakInformacije.privitakSadrzaj;
        Poruka.VrstaPoruka vrsta = privitakInformacije.nwtisPoruka;

        return new Poruka(id, vrijemeSlanja, vrijemePrijema, salje, predmet, privitak, vrsta);
    }

    public static void ZapisiTekstUDatoteku(String putanja, String tekst) {
        try {
            File file = new File(putanja);

            String parent = file.getParent();
            File directory = new File(parent);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // creates the file
            file.createNewFile();

            // creates a FileWriter Object
            FileWriter writer = new FileWriter(file);

            // Writes the content to the file
            writer.write(tekst);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Komanda ParsirajJsonKomande(String tekstJson) {
        Gson gson = new Gson();
        Komanda komanda = gson.fromJson(tekstJson, Komanda.class);

        return komanda;
    }

    public static void ZapisiEvidencijuUDatoteku(Evidencija evidencija, String nazivDatoteke) throws IOException {
        ServletContext sc = SlusacAplikacije.servletContext;
        String webInfPutanja = sc.getRealPath("/WEB-INF/");
        String putanjaDatoteke = webInfPutanja + File.separator + nazivDatoteke;
        
        String newLine = System.getProperty("line.separator");

        FileOutputStream fos = new FileOutputStream(putanjaDatoteke, true);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        osw.append("Obrada poruka broj: " + evidencija.redniBrojObrade + newLine);
        osw.write("Obrada započela u: " + evidencija.pocetakObrade + newLine);
        osw.write("Obrada završila u: " + evidencija.zavrsetakObrade + newLine);
        osw.write("Trajanje obrade u ms: " + evidencija.trajanjeObrade + newLine);
        osw.write("Broj poruka: " + evidencija.brojPoruka + newLine);
        osw.write("Broj dodanih IOT: " + evidencija.brojDodanihIot + newLine);
        osw.write("Broj ažuriranih IOT: " + evidencija.brojAzuriranihIot + newLine);
        osw.write("Broj neispravnih poruka: " + evidencija.brojNeispravnihPoruka + newLine);
        osw.write(newLine);
        
        osw.close();
    } 
    
    public static void ObrisiSadrzajDatoteke(String nazivDatoteke) throws FileNotFoundException, IOException{
        ServletContext sc = SlusacAplikacije.servletContext;
        String webInfPutanja = sc.getRealPath("/WEB-INF/");
        String putanjaDatoteke = webInfPutanja + File.separator + nazivDatoteke;
        
        File file = new File(putanjaDatoteke);
        if(!file.exists()){
            file.createNewFile();
        }
        
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }
    
    public static boolean ValidirajJsonIzStringa(String sadrzaj){ 
        boolean ispravnost = true;
        Gson gson = new Gson();
        Properties postavke = new Properties();
        
        try {
            postavke = gson.fromJson(sadrzaj, Properties.class);
        } catch (JsonSyntaxException e) {
            ispravnost = false;
        }        
        
        if(postavke == null){
            ispravnost = false;
        }
        
        return ispravnost;
    }
}

class PrivitakInformacije{
    String privitakSadrzaj;
    Poruka.VrstaPoruka nwtisPoruka;
}