package org.foi.nwtis.damdrempe.web.podaci;

import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;

/**
 * Klasa za rad s zapisima iz dnevnika rada.
 * @author ddrempetic
 */
public class Dnevnik {

    private String korisnik;
    private String url;
    private String ipAdresa = "";
    private int trajanje;
    private int status;
    
    private long pocetakRada;
    private long krajRada;

    public Dnevnik() {
        this.pocetakRada = System.currentTimeMillis();
        this.status = 0;
    }
    
    public void postaviUspjesanStatus(){
        this.status = 1;
    }
    
    public void zavrsiDnevnik(String korisnik, String url){
        this.ipAdresa = PomocnaKlasa.dajTrenutnuIPAdresu();        
        this.korisnik = korisnik;
        this.url = url;
        this.krajRada = System.currentTimeMillis();
        this.trajanje = (int) (this.krajRada - this.pocetakRada);
    }
    
    public void zavrsiISpremiDnevnik(String korisnik, String url){
        this.zavrsiDnevnik(korisnik, url);
        PomocnaKlasa.zapisiUDnevnik(this);
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
