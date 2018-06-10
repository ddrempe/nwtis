package org.foi.nwtis.damdrempe.web.podaci;

import java.util.Date;

/**
 * Klasa za rad s korisnicima.
 * @author ddrempetic
 */
public class Korisnik {

    private int id;
    private String ki;
    private String ime;
    private String prezime;
    private String lozinka;
    private String email;
    private int vrsta;
    private Date datum_kreiranja;
    private Date datum_promjene;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public int getVrsta() {
        return vrsta;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    public Date getDatum_kreiranja() {
        return datum_kreiranja;
    }

    public void setDatum_kreiranja(Date datum_kreiranja) {
        this.datum_kreiranja = datum_kreiranja;
    }

    public Date getDatum_promjene() {
        return datum_promjene;
    }

    public void setDatum_promjene(Date datum_promjene) {
        this.datum_promjene = datum_promjene;
    }

    public String getKi() {
        return ki;
    }

    public void setKi(String ki) {
        this.ki = ki;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    
}
