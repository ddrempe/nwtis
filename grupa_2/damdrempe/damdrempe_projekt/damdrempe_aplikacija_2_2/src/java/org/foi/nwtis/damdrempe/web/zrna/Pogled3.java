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
import org.foi.nwtis.damdrempe.rest.klijenti.ParkiranjeRESTKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.ProcitaniJsonOdgovor;
import org.foi.nwtis.damdrempe.web.podaci.Izbornik;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

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
    private List<MeteoPrognoza> popisMeteo = new ArrayList<>();

    private String statusParkiralista;

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

    public String dodajParkiraliste() {
        
        return "";
    }

    public String obrisiParkiraliste() {

        return "";
    }

    public String aktivirajParkiraliste() {

        return "";
    }

    public String blokirajParkiraliste() {

        return "";
    }

    public String dohvatiVozilaParkiralista() {
        return "";
    }

    public String dohvatiZadnjeMeteo() {

        return "";
    }

    public String dohvatiVazeceMeteo() {

        return "";
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

    public List<MeteoPrognoza> getPopisMeteo() {
        return popisMeteo;
    }

    public void setPopisMeteo(List<MeteoPrognoza> popisMeteo) {
        this.popisMeteo = popisMeteo;
    }

    public String getStatusParkiralista() {
        return statusParkiralista;
    }

    public void setStatusParkiralista(String statusParkiralista) {
        this.statusParkiralista = statusParkiralista;
    }

}
