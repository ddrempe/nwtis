/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
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

    public PregledPoruka() {
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");       
        posluzitelj = k.dajPostavku("mail.server");
        korIme = k.dajPostavku("mail.usernameThread");
        lozinka = k.dajPostavku("mail.passwordThread");
        posebnaMapa = k.dajPostavku("mail.folderNWTiS");

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
        //TODO tu preuzmi stvarne poruke
        popisPoruka = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            popisPoruka.add(new Poruka(Integer.toString(i), new Date(), new Date(), "damdrempe@foi.hr", "Poruka " + i, "{}", Poruka.VrstaPoruka.NWTiS_poruka));
        }
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
    
    public String promjeniJezik() {
        return "promjeniJezik";
    }
    
    public String slanjePoruke() {
        return "slanjePoruke";
    }
        
    public String pregledDnevnika() {
        return "pregledDnevnika";
    }
    
}
