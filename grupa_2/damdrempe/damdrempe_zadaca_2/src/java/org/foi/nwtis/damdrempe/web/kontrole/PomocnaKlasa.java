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
 * Klasa sa statičnim pomoćnim metodama koje se koriste u aplikaciji.
 * @author ddrempetic
 */
public class PomocnaKlasa {

    /**
     * Čita sadržaj JSON datoteke kao string.
     * @param putanja
     * @return sadržaj JSON datoteke kao string
     */
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
    
    /**
     * Provjerava sintaksu teksta tj. privitka prema regularnom izrazu
     * @param tekstPrivitka
     * @return true ako je sintaksa ispravna, inače false
     */
    public static boolean ProvjeriSintaksuPrivitka(String tekstPrivitka) {
        String pattern = "\\{(\"id\"): ?(\\d{1,4}), ?(\"komanda\"): ?(\"dodaj\"|\"azuriraj\"), ?((?:\"[a-zA-Z ]{1,30}\": ?(?:[0-9]{1,3}|[0-9]{1,3}\\.[0-9]{1,2}|\"[a-zA-Z ]{1,30}\"),\\s?){1,5}) ?(\"vrijeme\"): ?\"([0-9]{4}\\.[0-9]{2}\\.[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}){1,3}\"\\}";
        boolean rezultat;
        rezultat = tekstPrivitka.matches(pattern);
        
        return rezultat;
    }

    /**
     * Čita sve potrebno za objekt poruke.
     * @param poruka
     * @param trazeniNazivPrivitka
     * @return objekt poruke
     * @throws MessagingException
     * @throws IOException 
     */
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

    /**
     * Zapisuje tekst u datoteku na zadanoj putanji.
     * Kreira datoteku ako ne postoji.
     * @param putanja
     * @param tekst 
     */
    public static void ZapisiTekstUDatoteku(String putanja, String tekst) {
        try {
            File file = new File(putanja);

            String parent = file.getParent();
            File directory = new File(parent);
            if (!directory.exists()) {
                directory.mkdir();
            }

            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            writer.write(tekst);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Pokusava procitati tekst i utvrditi da li je ispravna komanda
     * @param tekstJson
     * @return 
     */
    public static Komanda ParsirajJsonKomande(String tekstJson) {
        Gson gson = new Gson();
        Komanda komanda = gson.fromJson(tekstJson, Komanda.class);

        return komanda;
    }

    /**
     * Zapisuje nove retke u datoteku evidencije prema podacima iz objekta.
     * @param evidencija
     * @param nazivDatoteke
     * @throws IOException 
     */
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
    
    /**
     * Briše sadržaj datoteke.
     * @param nazivDatoteke
     * @throws FileNotFoundException
     * @throws IOException 
     */
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
    
    /**
     * Za poruku ispituje privitak da bi se utvrdila vrsta poruke.
     * @param message
     * @param trazeniNazivPrivitka
     * @return sadrzaj privitka i vrstu poruke
     */
    public static PrivitakInformacije IspitajDatotekuPrivitka(Message message, String trazeniNazivPrivitka) {
        ServletContext sc = SlusacAplikacije.servletContext;
        String webInfPutanja = sc.getRealPath("/WEB-INF/");
        String putanjaPrivitaka = webInfPutanja + File.separator + "privici";

        File directory = new File(putanjaPrivitaka);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String saveDirectory = "";
        int brojPrivitaka = 0;
        String nazivDatoteke = "";
        PrivitakInformacije privitakInformacije = new PrivitakInformacije();
        privitakInformacije.nwtisPoruka = Poruka.VrstaPoruka.neNWTiS_poruka;
        privitakInformacije.privitakSadrzaj = "";

        try {
            String privici = "";
            String vrstaSadrzaja = message.getContentType();
            String sadrzajPoruke = "";
            if (vrstaSadrzaja.contains("multipart")) {
                if (message.getContent() instanceof Multipart == false) {
                    System.out.println("Nije moguce citati privitak za poruku " + message.getMessageNumber());
                    
                    return privitakInformacije;
                }
                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        nazivDatoteke = part.getFileName();
                        privici += nazivDatoteke + ", ";
                        saveDirectory = putanjaPrivitaka + File.separator + nazivDatoteke;

                        if (nazivDatoteke.contains(":")) {
                            continue;
                        }

                        part.saveFile(saveDirectory);
                        brojPrivitaka++;
                    } else {
                        sadrzajPoruke = part.getContent().toString();
                    }
                }
                if (privici.length() > 1) {
                    privici = privici.substring(0, privici.length() - 2);
                }

            } else if (vrstaSadrzaja.contains("text/plain")
                    || vrstaSadrzaja.contains("text/html")) {
                Object content = message.getContent();
                if (content != null) {
                    sadrzajPoruke = content.toString();
                }
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (brojPrivitaka == 1 && nazivDatoteke.equalsIgnoreCase(trazeniNazivPrivitka)) {
            privitakInformacije.privitakSadrzaj = PomocnaKlasa.ProcitajSadrzajJsonDatoteke(saveDirectory);
            privitakInformacije.nwtisPoruka =  Poruka.VrstaPoruka.NWTiS_poruka;
        }

        return privitakInformacije;
    }
    
    /**
     * Provjerava da li je korisnik upisao ispravan JSON.
     * @param sadrzaj
     * @return true ako je ispravan, inače false
     */
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

/**
 * Klasa pomoću koje se prenosi informacija o sadržaju privitka i vrsti poruke.
 * @author ddrempetic
 */
class PrivitakInformacije{
    String privitakSadrzaj;
    Poruka.VrstaPoruka nwtisPoruka;
}