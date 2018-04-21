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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
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
    private String privitak;
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
        privitak = k.dajPostavku("mail.attachmentFilename");        
        
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
            message.setText(privitakSadrzaj);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "";
        }   
        
        return "";
    }
    
    public String obrisiPoruku(){
        return "";
    }
    
    public void preuzmiSadrzaj(){        
        if (odabranaDatoteka == null || odabranaDatoteka.length() == 0) {
            System.out.println("Ne postoji datoteka.");
        }
        
        ServletContext sc = SlusacAplikacije.servletContext;
        String putanja = sc.getRealPath("/WEB-INF/" + odabranaDatoteka);

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
        
        privitakSadrzaj = sadrzaj;
    } 
}