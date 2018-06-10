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

    /**
     *
     * @param id
     * @param korisnik
     * @param url
     * @param vrijeme
     * @param ipadresa
     * @param trajanje
     * @param status
     */
    public DnevnikPodaci(int id, String korisnik, String url, Date vrijeme, String ipadresa, int trajanje, int status) {
        this.id = id;
        this.korisnik = korisnik;
        this.url = url;
        this.vrijeme = vrijeme;
        this.ipadresa = ipadresa;
        this.trajanje = trajanje;
        this.status = status;
    }

    /**
     *
     */
    public DnevnikPodaci() {
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     *
     * @param korisnik
     */
    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     */
    public Date getVrijeme() {
        return vrijeme;
    }

    /**
     *
     * @param vrijeme
     */
    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    /**
     *
     * @return
     */
    public String getIpadresa() {
        return ipadresa;
    }

    /**
     *
     * @param ipadresa
     */
    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    /**
     *
     * @return
     */
    public int getTrajanje() {
        return trajanje;
    }

    /**
     *
     * @param trajanje
     */
    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    
    
}
