package org.foi.nwtis.damdrempe.web.podaci;

import java.util.Date;

/**
 * Klasa za rad s zapisima iz dnevnika rada.
 * @author ddrempetic
 */
public class DnevnikPodaci {

    private int id;
    private String korisnik;
    private String url;
    private Date vrijeme;
    private String ipadresa;
    private int trajanje;
    private int status;

    public DnevnikPodaci(int id, String korisnik, String url, Date vrijeme, String ipadresa, int trajanje, int status) {
        this.id = id;
        this.korisnik = korisnik;
        this.url = url;
        this.vrijeme = vrijeme;
        this.ipadresa = ipadresa;
        this.trajanje = trajanje;
        this.status = status;
    }

    public DnevnikPodaci() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getIpadresa() {
        return ipadresa;
    }

    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
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
