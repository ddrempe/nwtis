/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author ddrempetic
 */
@Named(value = "login")
@RequestScoped
public class Login {
    private String korisnickoIme;
    private String lozinka;

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }    

    /**
     * Creates a new instance of Login
     */
    public Login() {
    }
    
    public String provjeraKorisnika(){
        String statusPrijave = "OK";
        return statusPrijave;
    }    
}
