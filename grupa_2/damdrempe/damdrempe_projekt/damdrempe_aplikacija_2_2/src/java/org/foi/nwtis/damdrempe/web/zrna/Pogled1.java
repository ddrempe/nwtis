package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.pomocno.Stranicenje;
import org.foi.nwtis.damdrempe.rest.klijenti.KorisniciRESTKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.KorisniciRESTKlijentKorime;
import org.foi.nwtis.damdrempe.rest.klijenti.ProcitaniJsonOdgovor;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 * Zrno za prvi pogled.
 *
 * @author ddrempetic
 */
@Named(value = "pogled1")
@SessionScoped
public class Pogled1 implements Serializable {

    private String korisnickoIme;
    private String lozinka;
    private int stranicenjeBrojZapisa;

    private String imeReg;
    private String prezimeReg;
    private String imeAzu;
    private String prezimeAzu;
    private String poruka;

    private List<Korisnik> listaKorisnika = new ArrayList<>();
    private List<Korisnik> listaKorisnikaZaPrikaz = new ArrayList<>();
    private Stranicenje stranicenje;

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled1() {
    }

    /**
     * Dohvaća inicijalni popis korisnika. Dohvaća pojedine vrijednosti iz
     * konfiguracije.
     */
    @PostConstruct
    public void init() {
        korisnickoIme = PomocnaKlasa.dohvatiPostavku("korisnickoIme");
        lozinka = PomocnaKlasa.dohvatiPostavku("lozinka");
        stranicenjeBrojZapisa = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("stranicenjePogled1"));

        preuzmiSveKorisnikeREST();
        stranicenje = new Stranicenje(listaKorisnika, stranicenjeBrojZapisa);
        listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
    }

    /**
     * Preuzima sve korisnike putem REST servisa iz treće aplikacije.
     */
    public void preuzmiSveKorisnikeREST() {
        KorisniciRESTKlijent klijent = new KorisniciRESTKlijent();
        String odgovorJsonTekst = klijent.preuzimanjeSvihKorisnika(String.class, korisnickoIme, lozinka);
        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);

        listaKorisnika = procitaniJsonOdgovor.vratiNizKorisnika();
    }

    /**
     * Dodaje novog korisnika putem REST servisa iz treće aplikacije.
     */
    public void registracijaKorisnikaREST() {
        if (imeReg.isEmpty() || prezimeReg.isEmpty()) {
            poruka = "Niste popunili ime i/ili prezime za registraciju korisnika.";
            return;
        }

        KorisniciRESTKlijentKorime klijent = new KorisniciRESTKlijentKorime(korisnickoIme);
        String odgovorJsonTekst = klijent.dodajKorisnika(String.class, lozinka, imeReg, prezimeReg);
        poruka = odgovorJsonTekst;

        preuzmiSveKorisnikeREST();
    }

    /**
     * Ažurira korisnika putem REST servisa iz treće aplikacije.
     */
    public void azuriranjeKorisnikaREST() {
        if (imeAzu.isEmpty() || prezimeAzu.isEmpty()) {
            poruka = "Niste popunili ime i/ili prezime za azuriranje korisnika.";
            return;
        }

        KorisniciRESTKlijentKorime klijent = new KorisniciRESTKlijentKorime(korisnickoIme);
        String odgovorJsonTekst = klijent.azurirajKorisnika(String.class, lozinka, imeAzu, prezimeAzu);
        poruka = odgovorJsonTekst;

        preuzmiSveKorisnikeREST();
    }

    /**
     * 
     */
    public void sljedecaStranica() {
        if (stranicenje.sljedeciZapisi() == true) {
            listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
        }
    }

    /**
     *
     */
    public void prethodnaStranica() {
        if (stranicenje.prethodniZapisi() == true) {
            listaKorisnikaZaPrikaz = stranicenje.dajZapiseZaPrikaz();
        }
    }

    /**
     *
     * @return
     */
    public String getImeReg() {
        return imeReg;
    }

    /**
     *
     * @param imeReg
     */
    public void setImeReg(String imeReg) {
        this.imeReg = imeReg;
    }

    /**
     *
     * @return
     */
    public String getPrezimeReg() {
        return prezimeReg;
    }

    /**
     *
     * @param prezimeReg
     */
    public void setPrezimeReg(String prezimeReg) {
        this.prezimeReg = prezimeReg;
    }

    /**
     *
     * @return
     */
    public String getImeAzu() {
        return imeAzu;
    }

    /**
     *
     * @param imeAzu
     */
    public void setImeAzu(String imeAzu) {
        this.imeAzu = imeAzu;
    }

    /**
     *
     * @return
     */
    public String getPrezimeAzu() {
        return prezimeAzu;
    }

    /**
     *
     * @param prezimeAzu
     */
    public void setPrezimeAzu(String prezimeAzu) {
        this.prezimeAzu = prezimeAzu;
    }

    /**
     *
     * @return
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     *
     * @param poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     *
     * @return
     */
    public List<Korisnik> getListaKorisnikaZaPrikaz() {
        return listaKorisnikaZaPrikaz;
    }

    /**
     *
     * @param listaKorisnikaZaPrikaz
     */
    public void setListaKorisnikaZaPrikaz(List<Korisnik> listaKorisnikaZaPrikaz) {
        this.listaKorisnikaZaPrikaz = listaKorisnikaZaPrikaz;
    }

}
