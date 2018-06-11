package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import javax.inject.Named;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.pomocno.Stranicenje;
import org.foi.nwtis.damdrempe.web.podaci.DnevnikPodaci;


/**
 * Pogled 2 za pregled i filtriranje dnevnika.
 * @author ddrempetic
 */
@Named(value = "pogled2")
@ManagedBean
@SessionScoped
public class Pogled2 implements Serializable{
    
    private String poruka;    
    private List<DnevnikPodaci> listaZapisa = new ArrayList<>();
    private Stranicenje stranicenje;
    
    private String korisnik;
    private Date odVrijeme;
    private Date doVrijeme;
    private String adresaZahtjeva;
    
    private int brojZapisaPoStranici;

    /**
     * Creates a new instance of Pogled2
     */
    public Pogled2() {
    }
    
    /**
     * Dohvaća popis zapisa iz dnevnika.
     */
    @PostConstruct
    public void init() {
        korisnik = "";
        adresaZahtjeva = "";
        
        int brojDanaZaPrikaz = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("broj.dana.pogled2"));
        long trenutnoVrijemeLong = System.currentTimeMillis();
        long odVrijemeLong = trenutnoVrijemeLong - brojDanaZaPrikaz * 24 * 60 * 60 * 1000;
        doVrijeme = new Date(trenutnoVrijemeLong);
        odVrijeme = new Date(odVrijemeLong);
        
        brojZapisaPoStranici = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("stranicenje.pogled2"));
        preuzmiFiltrirano();   
    }
    
    /**
     * Preuzima zapise iz dnevnika prema unesenim podacima (filteru) na sucelju.
     */
    public void preuzmiFiltrirano(){
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            listaZapisa = bpo.dnevnikSelectFiltrirano(korisnik, new Timestamp(odVrijeme.getTime()), new Timestamp(doVrijeme.getTime()), adresaZahtjeva);
            stranicenje = new Stranicenje(listaZapisa, brojZapisaPoStranici);
            poruka = "Dohvaceno je ukupno " + listaZapisa.size() + " zapisa.";
        } catch (SQLException | ClassNotFoundException ex) {
            poruka = "Greska prilikom dohvacanja korisnika: " + ex.getMessage();
        }
    }
    
    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Stranicenje getStranicenje() {
        return stranicenje;
    }

    public void setStranicenje(Stranicenje stranicenje) {
        this.stranicenje = stranicenje;
    }  

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String Korisnik) {
        this.korisnik = Korisnik;
    }

    public Date getOdVrijeme() {
        return odVrijeme;
    }

    public void setOdVrijeme(Date odVrijeme) {
        this.odVrijeme = odVrijeme;
    }

    public Date getDoVrijeme() {
        return doVrijeme;
    }

    public void setDoVrijeme(Date doVrijeme) {
        this.doVrijeme = doVrijeme;
    }

    public String getAdresaZahtjeva() {
        return adresaZahtjeva;
    }

    public void setAdresaZahtjeva(String adresaZahtjeva) {
        this.adresaZahtjeva = adresaZahtjeva;
    }   
    
}
