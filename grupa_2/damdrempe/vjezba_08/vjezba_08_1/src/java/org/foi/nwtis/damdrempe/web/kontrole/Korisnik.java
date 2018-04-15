/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.kontrole;

/**
 *
 * @author grupa_2
 */
public class Korisnik {
    String korime;
    String ime;
    String prezime;
    String id;
    int vrsta;
    String ipAdresa;  

    public Korisnik(String korime, String ime, String prezime, String id, int vrsta, String ipAdresa) {
        this.korime = korime;
        this.ime = ime;
        this.prezime = prezime;
        this.id = id;
        this.vrsta = vrsta;
        this.ipAdresa = ipAdresa;
    }

    public String getKorime() {
        return korime;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVrsta() {
        return vrsta;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }
    
    
}
