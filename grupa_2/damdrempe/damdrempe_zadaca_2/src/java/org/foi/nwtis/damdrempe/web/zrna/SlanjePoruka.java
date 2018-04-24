/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.kontrole.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 *
 * @author grupa_2
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
    private String privitakSadrzaj = "";
    
    public SlanjePoruka() {        
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");       
        posluzitelj = k.dajPostavku("mail.server");
        prima = k.dajPostavku("mail.usernameThread");
        salje = k.dajPostavku("mail.usernameEmailAddress");
        predmet = k.dajPostavku("mail.subjectEmail");
        trazeniNazivPrivitka = k.dajPostavku("mail.attachmentFilename");        
        
        String [] webInfDatoteke = new File(sc.getRealPath("WEB-INF")).list();         
        popisDatoteka = new ArrayList<>();
        
        for (String nazivDatoteke: webInfDatoteke) {
            if(nazivDatoteke.toUpperCase().endsWith(".JSON")){
                popisDatoteka.add(nazivDatoteke);
            }
        }
    }

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
    
    public String promjeniJezik() {
        return "promjeniJezik";
    }
    
    public String pregledPoruka() {
        return "pregledPoruka";
    }
        
    public String pregledDnevnika() {
        return "pregledDnevnika";
    }
    
    public String saljiPoruku(){
        ServletContext sc = SlusacAplikacije.servletContext;
        String webInfPutanja = sc.getRealPath("/WEB-INF/");
        String putanja = webInfPutanja + File.separator + "poslano" + File.separator + odabranaDatoteka;
        
        PomocnaKlasa.ZapisiTekstUDatoteku(putanja, privitakSadrzaj);
        
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", posluzitelj);

            Session session = Session.getInstance(properties, null);

            MimeMessage message = new MimeMessage(session);

            Address fromAddress = new InternetAddress(salje);
            message.setFrom(fromAddress);

            Address[] toAddresses = InternetAddress.parse(prima);
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            message.setSubject(predmet);
            message.setSentDate(new Date(System.currentTimeMillis()));
            
            //dodano
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            messageBodyPart = new MimeBodyPart();            
            String filename = putanja;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(odabranaDatoteka);
            multipart.addBodyPart(messageBodyPart); 
            message.setContent(multipart);            

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "";
        }   
        
        return "";
    }
    
    public String preuzmiSadrzaj(){
        ServletContext sc = SlusacAplikacije.servletContext;        
        String putanja = sc.getRealPath("/WEB-INF/") + File.separator + odabranaDatoteka;
        privitakSadrzaj = PomocnaKlasa.ProcitajSadrzajJsonDatoteke(putanja);
        
        return "";
    }
    
    public String obrisiPoruku(){
        return "";
    }
}