package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.foi.nwtis.damdrempe.pomocno.JsonGraditelj;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.rest.klijenti.ParkiranjeRESTKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.ParkiranjeRESTKlijentId;
import org.foi.nwtis.damdrempe.rest.klijenti.ParkiranjeRESTKlijentIdVozila;
import org.foi.nwtis.damdrempe.rest.klijenti.ProcitaniJsonOdgovor;
import org.foi.nwtis.damdrempe.web.podaci.Izbornik;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;
import org.foi.nwtis.damdrempe.web.podaci.Vozilo;
import org.foi.nwtis.damdrempe.ws.klijenti.MeteoPodaci;
import org.foi.nwtis.damdrempe.ws.klijenti.MeteoWSKlijent;

/**
 * Zrno za pogled 3.
 *
 * @author ddrempetic
 */
@Named(value = "pogled3")
@SessionScoped
public class Pogled3 implements Serializable {

    private String naziv;
    private String adresa;
    private String kapacitet;
    private String odabraniParking;
    private String poruka;
    private List<Parkiraliste> listaParkiralista = new ArrayList<>();
    private List<Izbornik> popisParking = new ArrayList<>();

    private String statusParkiralista;
    private List<Vozilo> listaVozila = new ArrayList<>();
    org.foi.nwtis.damdrempe.ws.klijenti.MeteoPodaci meteoPodaci;

    private String korisnickoIme;
    private String lozinka;

    /**
     *
     */
    public Pogled3() {
    }

    /**
     * Dohvaća popis svih parkirališta i postavlja ostale inicijalne
     * vrijednosti.
     */
    @PostConstruct
    public void init() {
        korisnickoIme = PomocnaKlasa.dohvatiPostavku("korisnickoIme");
        lozinka = PomocnaKlasa.dohvatiPostavku("lozinka");
        preuzmiSvaParkiralistaREST();
        odabraniParking = popisParking.get(0).getVrijednost();
    }

    /**
     * Preuzima sva parkirališta putem REST servisa iz prve aplikacije.
     */
    public void preuzmiSvaParkiralistaREST() {
        ParkiranjeRESTKlijent klijent = new ParkiranjeRESTKlijent();
        String odgovorJsonTekst = klijent.getJson(String.class, korisnickoIme, lozinka);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        listaParkiralista = procitaniJsonOdgovor.vratiNizParkiralista();

        popisParking = new ArrayList<>();

        for (Parkiraliste p : listaParkiralista) {
            if(p.getKorisnik().equals(korisnickoIme)){
                Izbornik i = new Izbornik(p.getNaziv(), Integer.toString(p.getId()));
                popisParking.add(i);
            }
        }
    }

    /**
     * Ispisuje poruku stanja parkirališta iz liste.
     */
    public void dajStatusParkiralista() {
        Parkiraliste p = dajOdabranoParkiraliste();
        statusParkiralista = p.getStatus();

        poruka = "Dohvaćen je status.";
    }

    /**
     * Poziva operaciju za dodavanje novog parkirališta putem REST servisa iz
     * prve aplikacije.
     */
    public void dodajParkiralisteREST() {
        if (naziv.isEmpty() || adresa.isEmpty() || kapacitet.isEmpty()) {
            poruka = "Niste popunili sve podatke za unos parkirališta.";
            return;
        }

        String parkiralistePodaci = JsonGraditelj.napraviJsonZaDodajParkiraliste(naziv, adresa, kapacitet);
        ParkiranjeRESTKlijent klijent = new ParkiranjeRESTKlijent();
        String odgovorJsonTekst = klijent.postJson(parkiralistePodaci, String.class, korisnickoIme, lozinka);

        preuzmiSvaParkiralistaREST();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za brisanje parkirališta putem REST servisa iz prve
     * aplikacije.
     */
    public void obrisiParkiralisteREST() {
        ParkiranjeRESTKlijentId klijent = new ParkiranjeRESTKlijentId(odabraniParking);
        String odgovorJsonTekst = klijent.deleteJson(String.class, korisnickoIme, lozinka);

        preuzmiSvaParkiralistaREST();
        odabraniParking = popisParking.get(0).getVrijednost();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za ažuriranje parkirališta putem REST servisa iz prve
     * aplikacije. Mijenja status u AKTIVAN.
     */
    public void aktivirajParkiralisteREST() {
        Parkiraliste p = dajOdabranoParkiraliste();
        p.setStatus("AKTIVAN");
        String parkiralistePodaci = JsonGraditelj.napraviJsonZaAzurirajParkiraliste(p);

        ParkiranjeRESTKlijentId klijent = new ParkiranjeRESTKlijentId(odabraniParking);
        String odgovorJsonTekst = klijent.putJson(parkiralistePodaci, String.class, korisnickoIme, lozinka);

        preuzmiSvaParkiralistaREST();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za ažuriranje parkirališta putem REST servisa iz prve
     * aplikacije. Mijenja status u PASIVAN.
     */
    public void blokirajParkiralisteREST() {
        Parkiraliste p = dajOdabranoParkiraliste();
        p.setStatus("PASIVAN");
        String parkiralistePodaci = JsonGraditelj.napraviJsonZaAzurirajParkiraliste(p);

        ParkiranjeRESTKlijentId klijent = new ParkiranjeRESTKlijentId(odabraniParking);
        String odgovorJsonTekst = klijent.putJson(parkiralistePodaci, String.class, korisnickoIme, lozinka);

        preuzmiSvaParkiralistaREST();
        poruka = odgovorJsonTekst;
    }

    /**
     * Poziva operaciju za dohvat vozila parkirališta putem REST servisa iz prve
     * aplikacije.
     */
    public void preuzmiVozilaParkiralistaREST() {

        ParkiranjeRESTKlijentIdVozila klijent = new ParkiranjeRESTKlijentIdVozila(odabraniParking);
        String odgovorJsonTekst = klijent.getJsonVozila(String.class, korisnickoIme, lozinka);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        listaVozila = procitaniJsonOdgovor.vratiNizVozila();

        if (listaVozila.isEmpty()) {
            poruka = "Nije dohvaceno nijedno vozilo za odabrano parkiraliste.";
            return;
        }

        poruka = "Dohvaceno je " + listaVozila.size() + " vozila.";
    }

    /**
     * Poziva operaciju za dohvat zadnjih meteopodataka parkirališta putem SOAP
     * servisa iz prve aplikacije.
     */
    public void dohvatiZadnjeMeteoSOAP() {
        int idOdabraniParking = Integer.parseInt(odabraniParking);
        meteoPodaci = MeteoWSKlijent.dajZadnjeMeteoPodatke(idOdabraniParking, korisnickoIme, lozinka);

        if (meteoPodaci == null) {
            poruka = "Nije moguće dohvatiti zadnje meteopodatke.";
            return;
        }

        poruka = "Dohvaceni su zadnji meteopodaci.";
    }

    /**
     * Poziva operaciju za dohvat vazecih meteopodataka parkirališta putem SOAP
     * servisa iz prve aplikacije.
     */
    public void dohvatiVazeceMeteoSOAP() {
        int idOdabraniParking = Integer.parseInt(odabraniParking);
        meteoPodaci = MeteoWSKlijent.dajVazeceMeteoPodatke(idOdabraniParking, korisnickoIme, lozinka);

        if (meteoPodaci == null) {
            poruka = "Nije moguće dohvatiti vazece meteopodatke.";
            return;
        }

        poruka = "Dohvaceni su vazeci meteopodaci.";
    }

    /**
     * Dohvaća objekt parkriališta prema odabiru iz liste na sučelju.
     *
     * @return
     */
    private Parkiraliste dajOdabranoParkiraliste() {
        int idOdabraniParking = Integer.parseInt(odabraniParking);
        Parkiraliste parkiraliste = null;
        for (Parkiraliste p : listaParkiralista) {
            if (p.getId() == idOdabraniParking) {
                parkiraliste = p;
            }
        }

        return parkiraliste;
    }

    /**
     *
     * @return
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     *
     * @param adresa
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     *
     * @return
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     *
     * @param naziv
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**
     *
     * @return
     */
    public String getKapacitet() {
        return kapacitet;
    }

    /**
     *
     * @param kapacitet
     */
    public void setKapacitet(String kapacitet) {
        this.kapacitet = kapacitet;
    }

    /**
     *
     * @return
     */
    public String getOdabraniParking() {
        return odabraniParking;
    }

    /**
     *
     * @param odabraniParking
     */
    public void setOdabraniParking(String odabraniParking) {
        this.odabraniParking = odabraniParking;
    }

    /**
     *
     * @return
     */
    public List<Izbornik> getPopisParking() {
        return popisParking;
    }

    /**
     *
     * @param popisParking
     */
    public void setPopisParking(List<Izbornik> popisParking) {
        this.popisParking = popisParking;
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
    public String getStatusParkiralista() {
        return statusParkiralista;
    }

    /**
     *
     * @param statusParkiralista
     */
    public void setStatusParkiralista(String statusParkiralista) {
        this.statusParkiralista = statusParkiralista;
    }

    /**
     *
     * @return
     */
    public List<Vozilo> getListaVozila() {
        return listaVozila;
    }

    /**
     *
     * @param listaVozila
     */
    public void setListaVozila(List<Vozilo> listaVozila) {
        this.listaVozila = listaVozila;
    }

    /**
     *
     * @return
     */
    public MeteoPodaci getMeteoPodaci() {
        return meteoPodaci;
    }

    /**
     *
     * @param meteoPodaci
     */
    public void setMeteoPodaci(MeteoPodaci meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

}
