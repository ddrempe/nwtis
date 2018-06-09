/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.foi.nwtis.damdrempe.pomocno.Stranicenje;
import org.foi.nwtis.damdrempe.rest.klijenti.KorisniciRESTKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.KorisniciRESTKlijentKorime;
import org.foi.nwtis.damdrempe.rest.klijenti.ProcitaniJsonOdgovor;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 *
 * @author ddrempetic
 */
@Named(value = "pogled1")
@SessionScoped
public class Pogled1 implements Serializable {
    
    private String korisnickoIme = "admin"; //TODO stvarni podaci prijavljenog korisnika
    private String lozinka = "123456";
    
    private String imeReg;
    private String prezimeReg;    
    private String imeAzu;
    private String prezimeAzu;
    private String poruka;
    
    private List<Korisnik> listaKorisnika = new ArrayList<>();
    private List<Korisnik> listaKorisnikaZaPrikaz = new ArrayList<>();
    private Stranicenje stranicenje;
    private boolean prvaStranica;
    private boolean zadnjaStranica;

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled1() {
    }
    
    /**
     * Dohvaća popis vozila.
     */
    @PostConstruct
    public void init() {
        preuzmiSveKorisnikeREST();
        stranicenje = new Stranicenje(listaKorisnika, 5); //TODO iz konfiguracije        
        listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
        prvaStranica = true;
        zadnjaStranica = false;
    }
    
    public void preuzmiSveKorisnikeREST(){
        KorisniciRESTKlijent klijent = new KorisniciRESTKlijent();        
        String odgovorJsonTekst = klijent.preuzimanjeSvihKorisnika(String.class, korisnickoIme, lozinka);
        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        
        listaKorisnika = procitaniJsonOdgovor.vratiNizKorisnika();       
    }

    public void registracijaKorisnika(){
        if(imeReg.isEmpty() || prezimeReg.isEmpty()){
            poruka = "Niste popunili ime i/ili prezime za registraciju korisnika.";
            return;
        }  
        
        KorisniciRESTKlijentKorime klijent = new KorisniciRESTKlijentKorime(korisnickoIme);
        String odgovorJsonTekst = klijent.dodajKorisnika(String.class, lozinka, imeReg, prezimeReg);        
        poruka = odgovorJsonTekst;
        
        preuzmiSveKorisnikeREST();
    }
    
    public void azuriranjeKorisnika(){
        if(imeAzu.isEmpty() || prezimeAzu.isEmpty()){
            poruka = "Niste popunili ime i/ili prezime za azuriranje korisnika.";
            return;
        } 
        
        KorisniciRESTKlijentKorime klijent = new KorisniciRESTKlijentKorime(korisnickoIme);
        String odgovorJsonTekst = klijent.azurirajKorisnika(String.class, lozinka, imeAzu, prezimeAzu);        
        poruka = odgovorJsonTekst;
        
        preuzmiSveKorisnikeREST();
    }
    
    public void sljedecaStranica(){
        if(stranicenje.sljedeciZapisi() == true){
            listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
        }
    }
    
    public void prethodnaStranica(){
        if(stranicenje.prethodniZapisi() == true){
            listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
        }  
    }
    
    public String getImeReg() {
        return imeReg;
    }

    public void setImeReg(String imeReg) {
        this.imeReg = imeReg;
    }

    public String getPrezimeReg() {
        return prezimeReg;
    }

    public void setPrezimeReg(String prezimeReg) {
        this.prezimeReg = prezimeReg;
    }

    public String getImeAzu() {
        return imeAzu;
    }

    public void setImeAzu(String imeAzu) {
        this.imeAzu = imeAzu;
    }

    public String getPrezimeAzu() {
        return prezimeAzu;
    }

    public void setPrezimeAzu(String prezimeAzu) {
        this.prezimeAzu = prezimeAzu;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public List<Korisnik> getListaKorisnikaZaPrikaz() {
        return listaKorisnikaZaPrikaz;
    }

    public void setListaKorisnikaZaPrikaz(List<Korisnik> listaKorisnikaZaPrikaz) {
        this.listaKorisnikaZaPrikaz = listaKorisnikaZaPrikaz;
    }
    
    
    
    
}
