package org.foi.nwtis.damdrempe.web.zrna;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.kontrole.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 * Omogućava čitanje JSON sadrzaja iz datoteke te kasnije slanje mail poruke
 * kao privitka u obliku datoteke na poslužitelj.
 *
 * @author ddrempetic
 */
@Named(value = "slanjePoruka")
@RequestScoped
public class SlanjePoruka {

    private String posluzitelj;
    private String prima;
    private String salje;
    private String predmet;
    private String trazeniNazivPrivitka;
    private List<String> popisDatoteka;
    private String odabranaDatoteka;
    private String privitakSadrzaj = "{}";

    /**
     * Konstruktor klase. 
     * Učitava postavke iz konfiguracije. 
     * Učitava popis json datoteka u WEB-INF direktoriju.
     */
    public SlanjePoruka() {
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");
        posluzitelj = k.dajPostavku("mail.server");
        prima = k.dajPostavku("mail.usernameThread");
        salje = k.dajPostavku("mail.usernameEmailAddress");
        predmet = k.dajPostavku("mail.subjectEmail");
        trazeniNazivPrivitka = k.dajPostavku("mail.attachmentFilename");

        String[] webInfDatoteke = new File(sc.getRealPath("WEB-INF")).list();
        popisDatoteka = new ArrayList<>();

        for (String nazivDatoteke : webInfDatoteke) {
            if (nazivDatoteke.toUpperCase().endsWith(".JSON")) {
                popisDatoteka.add(nazivDatoteke);
            }
        }
    }

    /**
     * Za naziv JSON datoteke odabran iz liste čita sadržaj i učitava ga u polje
     * za poruku na sučelju.
     *
     * @return odredište za navigaciju
     */
    public String preuzmiSadrzaj() {
        ServletContext sc = SlusacAplikacije.servletContext;
        String putanja = sc.getRealPath("/WEB-INF/") + File.separator + odabranaDatoteka;
        privitakSadrzaj = PomocnaKlasa.ProcitajSadrzajJsonDatoteke(putanja);

        return "slanjePoruka";
    }

    /**
     * Briše učitani sadržaj poruke i postavlja ga na prazni JSON sadržaj.
     * @return odredište za navigaciju
     */
    public String obrisiPoruku() {
        privitakSadrzaj = "{}";

        return "slanjePoruka";
    }

    /**
     * Stvara JSON datoteku koja je odabrana iz liste, dodaje je kao privitak u poruku i šalje na mail poslužitelj.
     * @return odredište za navigaciju
     */
    public String saljiPoruku() {
        ServletContext sc = SlusacAplikacije.servletContext;
        String webInfPutanja = sc.getRealPath("/WEB-INF/");
        String putanja = webInfPutanja + File.separator + "poslano" + File.separator + odabranaDatoteka;
        
        PomocnaKlasa.ZapisiTekstUDatoteku(putanja, privitakSadrzaj);
        
        try {
            Multipart multipart = pripremiPrivitak(putanja);
            MimeMessage poruka = pripremiPoruku(multipart);
            Transport.send(poruka);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "";
        }

        return "slanjePoruka";
    }

    /**
     * Datoteku sa putanje dodaje u Multipart objekt pomoću kojeg će se slati privitak.
     * @param putanja putanja datoteke
     * @return multipart u kojem je sadrzana datoteka privitka
     * @throws MessagingException
     */
    private Multipart pripremiPrivitak(String putanja) throws MessagingException {
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText("");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        bodyPart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(putanja);
        bodyPart.setDataHandler(new DataHandler(dataSource));
        bodyPart.setFileName(odabranaDatoteka);
        multipart.addBodyPart(bodyPart);

        return multipart;
    }

    /**
     * Popunjava sve potrebne informacije za objekt poruke koja će se slatu.
     * @param multipart
     * @return pripremljeni objekt poruke koji će se slati.
     * @throws AddressException
     * @throws MessagingException 
     */
    private MimeMessage pripremiPoruku(Multipart multipart) throws AddressException, MessagingException {
        java.util.Properties postavke = System.getProperties();
        postavke.put("mail.smtp.host", posluzitelj);
        Session sesija = Session.getInstance(postavke, null);
        
        MimeMessage poruka = new MimeMessage(sesija);
        Address adresaPosiljatelja = new InternetAddress(salje);
        poruka.setFrom(adresaPosiljatelja);

        Address[] adresePrimatelja = InternetAddress.parse(prima);
        poruka.setRecipients(Message.RecipientType.TO, adresePrimatelja);

        poruka.setSubject(predmet);
        poruka.setSentDate(new Date(System.currentTimeMillis()));

        poruka.setContent(multipart);
        
        return poruka;
    }

    /**
     * Metoda za navigacijsko pravilo.
     * @return odredište za navigaciju
     */
    public String promjeniJezik() {
        return "promjeniJezik";
    }

    /**
     * Metoda za navigacijsko pravilo.
     * @return odredište za navigaciju
     */
    public String pregledPoruka() {
        return "pregledPoruka";
    }

    /**
     * Metoda za navigacijsko pravilo.
     * @return odredište za navigaciju
     */
    public String pregledDnevnika() {
        return "pregledDnevnika";
    }

    /* Nadalje slijede standardni getteri i setteri */
    /*------------------------------------------------*/
    
    public String getPosluzitelj() {
        return posluzitelj;
    }

    public void setPosluzitelj(String posluzitelj) {
        this.posluzitelj = posluzitelj;
    }

    public String getPrima() {
        return prima;
    }

    public void setPrima(String prima) {
        this.prima = prima;
    }

    public String getSalje() {
        return salje;
    }

    public void setSalje(String salje) {
        this.salje = salje;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getPrivitakSadrzaj() {
        return privitakSadrzaj;
    }

    public void setPrivitakSadrzaj(String privitakSadrzaj) {
        this.privitakSadrzaj = privitakSadrzaj;
    }

    public List<String> getPopisDatoteka() {
        return popisDatoteka;
    }

    public void setPopisDatoteka(List<String> popisDatoteka) {
        this.popisDatoteka = popisDatoteka;
    }

    public void setOdabranaDatoteka(String odabranaDatoteka) {
        this.odabranaDatoteka = odabranaDatoteka;
    }

    public String getOdabranaDatoteka() {
        return odabranaDatoteka;
    }
}