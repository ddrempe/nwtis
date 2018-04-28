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
 * Klasa za pregled zapisa iz dnevnika uz straničenje.
 * @author ddrempetic
 */
@Named(value = "pregledDnevnika")
@RequestScoped
public class PregledDnevnika {
    
    private List<Dnevnik> popisZapisa = new ArrayList<>();
    private static int ukupnoZapisa;    
    private static int brojZapisaZaPrikaz;    
    private static int pomak = 0;
    private static int maksPomak = 0;    
    private Date datumOd = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
    private Date datumDo = Date.from(ZonedDateTime.now().toInstant());

    /**
     * Konstruktor klase.
     */
    public PregledDnevnika() {
        ServletContext sc = SlusacAplikacije.servletContext;       
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");       
        brojZapisaZaPrikaz = Integer.parseInt(k.dajPostavku("mail.numLogItemsToShow"));
        
        PreuzmiZapise();
    }
    
    /**
     * Dohvaća zapise iz dnevnika za određeni datumski period.
     * Podržava stranićenje.
     */
    private void PreuzmiZapise(){
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            ukupnoZapisa = bpo.DnevnikSelectCount(new Timestamp(datumOd.getTime()), new Timestamp(datumDo.getTime()));             
            
            maksPomak = ukupnoZapisa / brojZapisaZaPrikaz;
            if(ukupnoZapisa % brojZapisaZaPrikaz == 0){
                maksPomak--;
            }
            
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
    
    /**
     * Ponovno preuzima zapise nakon promjene intervala datuma za prikaz.
     * Vraća pomak na početnu vrijednost.
     * @return odredište za navigaciju
     */
    public String promjenaIntervala() {
        pomak=0;
        PreuzmiZapise();
        
        return "promjenaIntervala";
    }
    
    /**
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.
     *
     * @return odredište za navigaciju
     */
    public String prethodniZapisi() {
        pomak--;        
        PreuzmiZapise();
        
        return "prethodniZapisi";
    }
    
    /**
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.
     *
     * @return odredište za navigaciju
     */
    public String sljedeciZapisi() {
        pomak++;
        PreuzmiZapise();
        
        return "sljedeciZapisi";
    }
    
    /**
     * Metoda za navigacijsko pravilo.
     *
     * @return odredište za navigaciju
     */
    public String promjeniJezik() {
        return "promjeniJezik";
    }
    
    /**
     * Metoda za navigacijsko pravilo.
     *
     * @return odredište za navigaciju
     */
    public String slanjePoruka() {
        return "slanjePoruka";
    }
    
    /**
     * Metoda za navigacijsko pravilo.
     *
     * @return odredište za navigaciju
     */
    public String pregledPoruka() {
        return "pregledPoruka";
    }    

    /* Nadalje slijede standardni getteri i setteri */
    /*------------------------------------------------*/
    
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