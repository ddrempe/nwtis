/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.kontrole.Izbornik;
import org.foi.nwtis.damdrempe.web.kontrole.Poruka;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

@Named(value = "pregledPoruka")
@RequestScoped
public class PregledPoruka {
    
    private String posluzitelj;
    private String korIme;
    private String lozinka;
    private List<Izbornik> popisMapa;
    private String odabranaMapa;
    private List<Poruka> popisPoruka;
    private String posebnaMapa;
    private int ukupnoPoruka;

    public PregledPoruka() {
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");       
        posluzitelj = k.dajPostavku("mail.server");
        korIme = k.dajPostavku("mail.usernameThread");
        lozinka = k.dajPostavku("mail.passwordThread");
        posebnaMapa = k.dajPostavku("mail.folderNWTiS");
        odabranaMapa = "INBOX";
        
        preuzmiMape();
        preuzmiPoruke();
    }

    private void preuzmiMape() {
        popisMapa = new ArrayList<>();
        popisMapa.add(new Izbornik("INBOX", "INBOX"));
        
        try { 
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", posluzitelj);
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(posluzitelj, korIme, lozinka);
            Folder[] sveMape = store.getDefaultFolder().list();            
            store.close();
            
            for (Folder folder : sveMape) {
                if(posebnaMapa.equals(folder.getName())){
                    popisMapa.add(new Izbornik(posebnaMapa, posebnaMapa));
                    break;
                }
            }
            
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void preuzmiPoruke() {
        popisPoruka = new ArrayList<>();
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", posluzitelj);
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(posluzitelj, korIme, lozinka);
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);
            
            ukupnoPoruka = folder.getMessageCount();
            
            if(ukupnoPoruka == 0){
                return;
            }
            
            //TODO dohvati samo n najsvjezijih, sortirati
            Message[] messages = folder.getMessages();
            
            for (Message m : messages) {
                MimeMessage message = (MimeMessage) m;       
                String id = message.getMessageID();                
                Date vrijemeSlanja = message.getSentDate();             
                Date vrijemePrijema = message.getReceivedDate();
                String salje = message.getFrom()[0].toString();
                String predmet = message.getSubject();
                String privitak = message.getContent().toString();
                Poruka.VrstaPoruka vrsta = Poruka.VrstaPoruka.neNWTiS_poruka;
                if(false){   //TODO provjeri vrstu
                    vrsta = Poruka.VrstaPoruka.NWTiS_poruka;
                }
                Poruka poruka = new Poruka(id, vrijemeSlanja, vrijemePrijema, salje, predmet, privitak, vrsta);
                popisPoruka.add(poruka);
            }
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public String promjenaMape(){
        preuzmiPoruke();
        return "pregledPoruka";
    }
    
    public String prethodnePoruke(){
        return "";
    }
    
    public String sljedecePoruke(){
        return "";
    }

    public String getPosluzitelj() {
        return posluzitelj;
    }

    public void setPosluzitelj(String posluzitelj) {
        this.posluzitelj = posluzitelj;
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public List<Izbornik> getPopisMapa() {
        return popisMapa;
    }

    public void setPopisMapa(List<Izbornik> popisMapa) {
        this.popisMapa = popisMapa;
    }

    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    public List<Poruka> getPopisPoruka() {
        return popisPoruka;
    }

    public void setPopisPoruka(List<Poruka> popisPoruka) {
        this.popisPoruka = popisPoruka;
    }
    
    public int getUkupnoPoruka() {
        return ukupnoPoruka;
    }
    
    public String promjeniJezik() {
        return "promjeniJezik";
    }
    
    public String slanjePoruka() {
        return "slanjePoruka";
    }
        
    public String pregledDnevnika() {
        return "pregledDnevnika";
    }
    
}
