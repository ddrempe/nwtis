/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.kontrole;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.dretve.ObradaPoruka;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.damdrempe.web.zrna.SlanjePoruka;

/**
 *
 * @author ddrempetic
 */
public class PomocnaKlasa {
    
    public static String ProcitajSadrzajJsonDatoteke(String putanja){        
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
        
    private static Poruka.VrstaPoruka OdrediVrstuPoruke(String privitak){
        if(privitak.equalsIgnoreCase("")){
            return Poruka.VrstaPoruka.neNWTiS_poruka;
        }
        else return Poruka.VrstaPoruka.NWTiS_poruka;
    }
    
    private static boolean ProvjeriSintaksuPrivitka(String tekstPrivitka){
        //TODO napraviti regex za provjeru sintakse i validirati tekst prema njemu
        //TODO provjeriti "text/json" ili "application/json"
        if(tekstPrivitka.contains("komanda")){
            return true;
        }
        else return false;
    }
    
    public static String IspitajDatotekuPrivitka(Message message, String trazeniNazivPrivitka) { 
        ServletContext sc = SlusacAplikacije.servletContext;
        String webInfPutanja = sc.getRealPath("/WEB-INF/");
        String putanjaPrivitaka = webInfPutanja + File.separator + "privici";
        
        File directory = new File(putanjaPrivitaka);
        if(!directory.exists()){
            directory.mkdir();
        }
        
        String saveDirectory = "";
        int brojPrivitaka = 0;
        String fileName = "";
        
        try {
            String attachFiles = "";
            String contentType = message.getContentType();
            String messageContent = "";
            if (contentType.contains("multipart")) {
                // content may contain attachments
                if(message.getContent() instanceof Multipart == false){
                    System.out.println("Nije moguce citati privitak.");
                    return "";
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
                        
                        if(fileName.contains(":")){
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
        
        if(brojPrivitaka == 1 && fileName.equalsIgnoreCase(trazeniNazivPrivitka)){
            String sadrzajPrivitka = PomocnaKlasa.ProcitajSadrzajJsonDatoteke(saveDirectory);
            
            if(ProvjeriSintaksuPrivitka(sadrzajPrivitka) == true){
                return sadrzajPrivitka;                
            }            
        }
        
        return "";
    } 

    public static Poruka ProcitajPoruku(Message poruka, String trazeniNazivPrivitka) throws MessagingException, IOException {
        MimeMessage message = (MimeMessage) poruka;
        String id = message.getMessageID();

        Date vrijemeSlanja = message.getSentDate();
        if (vrijemeSlanja == null) {
            vrijemeSlanja = new Date(0);
        }

        Date vrijemePrijema = message.getReceivedDate();
        String salje = message.getFrom()[0].toString();
        String predmet = message.getSubject();
        String privitak = PomocnaKlasa.IspitajDatotekuPrivitka(poruka, trazeniNazivPrivitka);
        Poruka.VrstaPoruka vrsta = OdrediVrstuPoruke(privitak);
        
        return new Poruka(id, vrijemeSlanja, vrijemePrijema, salje, predmet, privitak, vrsta);
    }  
    
    public static void ZapisiTekstUDatoteku(String putanja, String tekst){
        try {
            File file = new File(putanja);
            
            String parent = file.getParent();
            File directory = new File(parent);
            if(!directory.exists()){
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
}
