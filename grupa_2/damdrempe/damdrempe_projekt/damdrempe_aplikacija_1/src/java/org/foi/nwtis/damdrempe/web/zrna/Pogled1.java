package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.pomocno.Stranicenje;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 * Pogled 1 za pregled korisnika.
 * @author ddrempetic
 */
@Named(value = "pogled1")
@ManagedBean
@SessionScoped
public class Pogled1 implements Serializable{
    
    private String poruka;    
    private List<Korisnik> listaKorisnika = new ArrayList<>();
    private Stranicenje stranicenje;

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled1() {
    }
    
    /**
     * Dohvaća popis korisnika.
     */
    @PostConstruct
    public void init() {
        preuzmiSveKorisnike();
        int brojZapisaPoStranici = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("stranicenje.pogled1"));
        stranicenje = new Stranicenje(listaKorisnika, brojZapisaPoStranici);       
    }
    
    /**
     * Preuzima listu korisnika iz baze podataka.
     */
    public void preuzmiSveKorisnike(){      
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            listaKorisnika = bpo.korisniciSelectSviKorisnici();
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
}
