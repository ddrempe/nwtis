package org.foi.nwtis.damdrempe.web.podaci;

/**
 * Klasa za rad s zapisima iz dnevnika rada.
 * @author ddrempetic
 */
public class Dnevnik {

    private String korisnik;
    private String url;
    private String ipAdresa;
    private int trajanje;
    private int status;

    public Dnevnik(String korisnik, String url, String ipAdresa, int trajanje, int status) {
        this.korisnik = korisnik;
        this.url = url;
        this.ipAdresa = ipAdresa;
        this.trajanje = trajanje;
        this.status = status;
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
