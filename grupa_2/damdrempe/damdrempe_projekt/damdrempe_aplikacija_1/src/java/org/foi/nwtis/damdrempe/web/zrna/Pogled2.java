/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

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
import org.foi.nwtis.damdrempe.web.podaci.DnevnikPodaci;


/**
 *
 * @author ddrempetic
 */
@Named(value = "pogled2")
@ManagedBean
@SessionScoped
public class Pogled2 {
    
    private String poruka;    
    private List<DnevnikPodaci> listaZapisa = new ArrayList<>();
    private Stranicenje stranicenje;

    /**
     * Creates a new instance of Pogled2
     */
    public Pogled2() {
    }
    
    /**
     * Dohvaća popis korisnika.
     */
    @PostConstruct
    public void init() {
        preuzmiSveKorisnike();
        int brojZapisaPoStranici = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("stranicenje.pogled2"));
        stranicenje = new Stranicenje(listaZapisa, brojZapisaPoStranici);    
    }
    
    public void preuzmiSveKorisnike(){      
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            listaZapisa = bpo.dnevnikSelectSviZapisi();
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
