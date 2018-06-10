package org.foi.nwtis.damdrempe.web.podaci;

import java.util.Date;

/**
 * Klasa za rad s korisnicima iz baze podataka.
 * @author ddrempetic
 */
public class Korisnik {

    private int id;
    private String kor_ime;
    private String ime;
    private String prezime;
    private String lozinka;
    private String email_adresa;
    private int vrsta;
    private Date datum_kreiranja;
    private Date datum_promjene;

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
    public String getKor_ime() {
        return kor_ime;
    }

    /**
     *
     * @param kor_ime
     */
    public void setKor_ime(String kor_ime) {
        this.kor_ime = kor_ime;
    }

    /**
     *
     * @return
     */
    public String getIme() {
        return ime;
    }

    /**
     *
     * @param ime
     */
    public void setIme(String ime) {
        this.ime = ime;
    }

    /**
     *
     * @return
     */
    public String getPrezime() {
        return prezime;
    }

    /**
     *
     * @param prezime
     */
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    /**
     *
     * @return
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     *
     * @param lozinka
     */
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    /**
     *
     * @return
     */
    public String getEmail_adresa() {
        return email_adresa;
    }

    /**
     *
     * @param email_adresa
     */
    public void setEmail_adresa(String email_adresa) {
        this.email_adresa = email_adresa;
    }

    /**
     *
     * @return
     */
    public int getVrsta() {
        return vrsta;
    }

    /**
     *
     * @param vrsta
     */
    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    /**
     *
     * @return
     */
    public Date getDatum_kreiranja() {
        return datum_kreiranja;
    }

    /**
     *
     * @param datum_kreiranja
     */
    public void setDatum_kreiranja(Date datum_kreiranja) {
        this.datum_kreiranja = datum_kreiranja;
    }

    /**
     *
     * @return
     */
    public Date getDatum_promjene() {
        return datum_promjene;
    }

    /**
     *
     * @param datum_promjene
     */
    public void setDatum_promjene(Date datum_promjene) {
        this.datum_promjene = datum_promjene;
    }

    
}
