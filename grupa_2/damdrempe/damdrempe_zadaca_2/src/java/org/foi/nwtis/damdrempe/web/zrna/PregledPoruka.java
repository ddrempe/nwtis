/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.kontrole.Izbornik;
import org.foi.nwtis.damdrempe.web.kontrole.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.kontrole.Poruka;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

@Named(value = "pregledPoruka")
@RequestScoped
public class PregledPoruka {
    
    private String posluzitelj;
    private String korIme;
    private String lozinka;
    private List<Izbornik> popisMapa;
    private static String odabranaMapa = "INBOX";
    private List<Poruka> popisPoruka = new ArrayList<>();
    private String posebnaMapa;
    private static int brojPorukaDohvaceno;
    private static int brojPorukaZaPrikaz;
    
    private static int pocetnaPoruka;
    private static int zavrsnaPoruka;
    private static int ukupnoPoruka;
    private static int pomak = 0;
    private String trazeniNazivPrivitka;

    public PregledPoruka() {
        ServletContext sc = SlusacAplikacije.servletContext;       
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");       
        posluzitelj = k.dajPostavku("mail.server");
        korIme = k.dajPostavku("mail.usernameThread");
        lozinka = k.dajPostavku("mail.passwordThread");
        posebnaMapa = k.dajPostavku("mail.folderNWTiS");
        brojPorukaZaPrikaz = Integer.parseInt(k.dajPostavku("mail.numMessagesToShow"));
        trazeniNazivPrivitka = k.dajPostavku("mail.attachmentFilename"); 
        
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
    
    private void sortirajPoruke(){
        Collections.sort(popisPoruka, (Poruka p1, Poruka p2) -> {
            return p2.getVrijemeSlanja().compareTo(p1.getVrijemeSlanja());
        });       
    }
    
    private void preuzmiPoruke() {
        popisPoruka.clear();
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", posluzitelj);
            Session session = Session.getInstance(properties, null);
            Store store = session.getStore("imap");
            store.connect(posluzitelj, korIme, lozinka);
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);
            
            ukupnoPoruka = folder.getMessageCount();
            if(pomak == 0 || zavrsnaPoruka >= ukupnoPoruka){
                zavrsnaPoruka = ukupnoPoruka;
                pocetnaPoruka = zavrsnaPoruka - brojPorukaZaPrikaz + 1;                  
            }
            if(ukupnoPoruka < brojPorukaZaPrikaz){
                zavrsnaPoruka=ukupnoPoruka;
                pocetnaPoruka=1;
            }
            Message[] messages = folder.getMessages(pocetnaPoruka, zavrsnaPoruka);
            
            for (Message message : messages) {
                popisPoruka.add(PomocnaKlasa.ProcitajPoruku(message, trazeniNazivPrivitka));
            }
            
            brojPorukaDohvaceno = popisPoruka.size();
            if(brojPorukaDohvaceno > 0){
                sortirajPoruke();
            }            
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public String promjenaMape(){
        pomak=0;
        preuzmiPoruke();
        
        return "pregledPoruka";
    }
    
    public String prethodnePoruke(){
        pomak--;
        zavrsnaPoruka = zavrsnaPoruka + brojPorukaZaPrikaz;
        pocetnaPoruka = zavrsnaPoruka - brojPorukaZaPrikaz + 1; 
        
        preuzmiPoruke();
        
        return "pregledPoruka";
    }
    
    public String sljedecePoruke(){
        pomak++;
        zavrsnaPoruka = zavrsnaPoruka - brojPorukaZaPrikaz;        
        pocetnaPoruka = zavrsnaPoruka - brojPorukaZaPrikaz + 1;
        
        if(pocetnaPoruka<1){
            pocetnaPoruka = 1;
        }
        
        preuzmiPoruke();
        
        return "pregledPoruka";
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
    
    public int getBrojPorukaDohvaceno() {
        return brojPorukaDohvaceno;
    }

    public int getPocetnaPoruka() {
        return pocetnaPoruka;
    } 

    public int getUkupnoPoruka() {
        return ukupnoPoruka;
    }   

    public int getBrojPorukaZaPrikaz() {
        return brojPorukaZaPrikaz;
    }  

    public int getZavrsnaPoruka() {
        return zavrsnaPoruka;
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
