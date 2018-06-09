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
import org.foi.nwtis.damdrempe.pomocno.JsonGraditelj;
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

    private String korisnickoIme = "admin"; //TODO stvarni podaci prijavljenog korisnika
    private String lozinka = "123456";

    public Pogled3() {
    }

    /**
     * Dohvaća popis parkirališta.
     */
    @PostConstruct
    public void init() {
        preuzmiSvaParkiralistaREST();
        odabraniParking = popisParking.get(0).getVrijednost();
    }

    public void preuzmiSvaParkiralistaREST() {
        ParkiranjeRESTKlijent klijent = new ParkiranjeRESTKlijent();
        String odgovorJsonTekst = klijent.getJson(String.class, korisnickoIme, lozinka);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        listaParkiralista = procitaniJsonOdgovor.vratiNizParkiralista();

        popisParking = new ArrayList<>();

        for (Parkiraliste p : listaParkiralista) {
            Izbornik i = new Izbornik(p.getNaziv(), Integer.toString(p.getId()));
            popisParking.add(i);
        }
    }

    public void dajStatusParkiralista() {
        Parkiraliste p = dajOdabranoParkiraliste();
        statusParkiralista = p.getStatus();

        poruka = "Dohvaćen je status.";
    }

    public void dodajParkiralisteREST() {
        if(naziv.isEmpty() || adresa.isEmpty() || kapacitet.isEmpty()){
            poruka = "Niste popunili sve podatke za unos parkirališta.";
            return;
        }
        
        String parkiralistePodaci = JsonGraditelj.napraviJsonZaDodajParkiraliste(naziv, adresa, kapacitet);        
        ParkiranjeRESTKlijent klijent = new ParkiranjeRESTKlijent();
        String odgovorJsonTekst = klijent.postJson(parkiralistePodaci, String.class, korisnickoIme, lozinka);
        
        preuzmiSvaParkiralistaREST();
        poruka = odgovorJsonTekst;       
    }

    public void obrisiParkiralisteREST() {
        ParkiranjeRESTKlijentId klijent = new ParkiranjeRESTKlijentId(odabraniParking);
        String odgovorJsonTekst = klijent.deleteJson(String.class, korisnickoIme, lozinka);
        
        preuzmiSvaParkiralistaREST();
        odabraniParking = popisParking.get(0).getVrijednost();
        poruka = odgovorJsonTekst;
    }

    public void aktivirajParkiralisteREST() {
        Parkiraliste p = dajOdabranoParkiraliste();
        p.setStatus("AKTIVAN");
        String parkiralistePodaci = JsonGraditelj.napraviJsonZaAzurirajParkiraliste(p);
        
        ParkiranjeRESTKlijentId klijent = new ParkiranjeRESTKlijentId(odabraniParking);
        String odgovorJsonTekst = klijent.putJson(parkiralistePodaci, String.class, korisnickoIme, lozinka);
        
        preuzmiSvaParkiralistaREST();
        poruka = odgovorJsonTekst;
    }

    public void blokirajParkiralisteREST() {
        Parkiraliste p = dajOdabranoParkiraliste();
        p.setStatus("PASIVAN");
        String parkiralistePodaci = JsonGraditelj.napraviJsonZaAzurirajParkiraliste(p);
        
        ParkiranjeRESTKlijentId klijent = new ParkiranjeRESTKlijentId(odabraniParking);
        String odgovorJsonTekst = klijent.putJson(parkiralistePodaci, String.class, korisnickoIme, lozinka);
        
        preuzmiSvaParkiralistaREST();
        poruka = odgovorJsonTekst;
    }

    public void preuzmiVozilaParkiralistaREST() {
        
        ParkiranjeRESTKlijentIdVozila klijent = new ParkiranjeRESTKlijentIdVozila(odabraniParking);
        String odgovorJsonTekst = klijent.getJsonVozila(String.class, korisnickoIme, lozinka);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        listaVozila = procitaniJsonOdgovor.vratiNizVozila();
        
        if(listaVozila.isEmpty()){
            poruka = "Nije dohvaceno nijedno vozilo za odabrano parkiraliste.";
            return;
        }

        poruka = "Dohvaceno je " + listaVozila.size() + " vozila.";
    }

    public void dohvatiZadnjeMeteoSOAP() {
        int idOdabraniParking = Integer.parseInt(odabraniParking);
        meteoPodaci = MeteoWSKlijent.dajZadnjeMeteoPodatke(idOdabraniParking, korisnickoIme, lozinka);
        
        if(meteoPodaci == null){
            poruka = "Nije moguće dohvatiti zadnje meteopodatke.";
            return;
        }
        
        poruka = "Dohvaceni su zadnji meteopodaci.";
    }

    public void dohvatiVazeceMeteoSOAP() {
        int idOdabraniParking = Integer.parseInt(odabraniParking);
        meteoPodaci = MeteoWSKlijent.dajVazeceMeteoPodatke(idOdabraniParking, korisnickoIme, lozinka);
        
        if(meteoPodaci == null){
            poruka = "Nije moguće dohvatiti vazece meteopodatke.";
            return;
        }
        
        poruka = "Dohvaceni su vazeci meteopodaci.";
    }

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

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(String kapacitet) {
        this.kapacitet = kapacitet;
    }

    public String getOdabraniParking() {
        return odabraniParking;
    }

    public void setOdabraniParking(String odabraniParking) {
        this.odabraniParking = odabraniParking;
    }

    public List<Izbornik> getPopisParking() {
        return popisParking;
    }

    public void setPopisParking(List<Izbornik> popisParking) {
        this.popisParking = popisParking;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String getStatusParkiralista() {
        return statusParkiralista;
    }

    public void setStatusParkiralista(String statusParkiralista) {
        this.statusParkiralista = statusParkiralista;
    }

    public List<Vozilo> getListaVozila() {
        return listaVozila;
    }

    public void setListaVozila(List<Vozilo> listaVozila) {
        this.listaVozila = listaVozila;
    }

    public MeteoPodaci getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(MeteoPodaci meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }
    
    

}
