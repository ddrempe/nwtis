/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.kontrole.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.web.kontrole.Dnevnik;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 *
 * @author ddrempetic
 */
@Named(value = "pregledDnevnika")
@RequestScoped
public class PregledDnevnika {
    
    private List<Dnevnik> popisZapisa = new ArrayList<>();
    private int ukupnoZapisa;
    
    private static int brojZapisaZaPrikaz;    
    private static int pomak = 0;
    private static int maksPomak = 0;
    
    private Date datumOd = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
    private Date datumDo = Date.from(ZonedDateTime.now().toInstant());

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
        ServletContext sc = SlusacAplikacije.servletContext;       
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");       
        brojZapisaZaPrikaz = Integer.parseInt(k.dajPostavku("mail.numLogItemsToShow"));
        
        PreuzmiZapise();
    }
    
    public void PreuzmiZapise(){
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            ukupnoZapisa = bpo.DnevnikSelectCount(); 
            
            maksPomak = ukupnoZapisa / brojZapisaZaPrikaz;
            
            popisZapisa = bpo.DnevnikSelectPeriod(
                    new Timestamp(datumOd.getTime()), 
                    new Timestamp(datumDo.getTime()),
                    pomak,
                    brojZapisaZaPrikaz);
            bpo.ZatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PregledDnevnika.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String promjenaIntervala() {
        pomak=0;
        PreuzmiZapise();
        
        return "pregledDnevnika";   //TODO promjenaIntervala
    }
    
    public String prethodniZapisi() {
        pomak--;        
        PreuzmiZapise();
        
        return "pregledDnevnika";   //TOOD prethodniZapisi
    }
    
    public String sljedeciZapisi() {
        pomak++;
        PreuzmiZapise();
        
        return "pregledDnevnika";    //TODO sljedeciZapisi
    }
    
    public String promjeniJezik() {
        return "promjeniJezik";
    }
    
    public String slanjePoruka() {
        return "slanjePoruka";
    }
    
    public String pregledPoruka() {
        return "pregledPoruka";
    }    

    public List<Dnevnik> getPopisZapisa() {
        return popisZapisa;
    }

    public void setPopisZapisa(List<Dnevnik> popisZapisa) {
        this.popisZapisa = popisZapisa;
    }

    public int getUkupnoZapisa() {
        return ukupnoZapisa;
    }     

    public Date getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(Date datumOd) {
        this.datumOd = datumOd;
    }

    public Date getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(Date datumDo) {
        this.datumDo = datumDo;
    } 

    public int getPomak() {
        return pomak;
    }

    public void setPomak(int pomak) {
        this.pomak = pomak;
    }

    public int getMaksPomak() {
        return maksPomak;
    }

    public void setMaksPomak(int maksPomak) {
        this.maksPomak = maksPomak;
    } 
}