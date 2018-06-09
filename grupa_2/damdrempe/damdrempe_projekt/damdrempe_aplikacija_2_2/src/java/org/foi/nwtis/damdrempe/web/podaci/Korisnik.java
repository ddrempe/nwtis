/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.podaci;

import java.util.Date;

/**
 *
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKor_ime() {
        return kor_ime;
    }

    public void setKor_ime(String kor_ime) {
        this.kor_ime = kor_ime;
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

    public String getEmail_adresa() {
        return email_adresa;
    }

    public void setEmail_adresa(String email_adresa) {
        this.email_adresa = email_adresa;
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

    
}
