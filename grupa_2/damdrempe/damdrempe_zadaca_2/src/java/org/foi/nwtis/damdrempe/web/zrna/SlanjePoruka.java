/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
    
    public SlanjePoruka() {        
        // TODO preuzmi iz postavki
        posluzitelj = "127.0.0.1";
        prima = "servis@nwtis.nastava.foi.hr";
        salje = "admin@nwtis.nastava.foi.hr";
        predmet = "IOT poruka";
        privitak = "{}";
        
        popisDatoteka = new ArrayList<>();
        //TODO preuzmi naziv datoteka iz JSON iz WEB-INF
        for (int i = 0; i < 10; i++) {
            popisDatoteka.add("primjer"+i+".json");
        }
        popisDatoteka.add("primjer1.json");
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

    public String getPrivitak() {
        return privitak;
    }

    public void setPrivitak(String privitak) {
        this.privitak = privitak;
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

            Session session =
                    Session.getInstance(properties, null);

            MimeMessage message = new MimeMessage(session);

            Address fromAddress = new InternetAddress(salje);
            message.setFrom(fromAddress);

            Address[] toAddresses = InternetAddress.parse(prima);
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            message.setSubject(predmet);
            message.setText(privitak);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "";
        }   
        
        privitak = "{}";
        return "";
    }
    
    public String obrisiPoruku(){
        return "";
    }
    
    public String preuzmiSadrzaj(){
        //TODO preuzmi sadrzaj datoteke čiji je naziv u varijabli odabranaDatoteka
        privitak = odabranaDatoteka;
        return "";
    }
}