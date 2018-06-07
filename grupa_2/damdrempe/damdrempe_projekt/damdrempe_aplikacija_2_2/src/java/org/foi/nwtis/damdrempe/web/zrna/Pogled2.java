/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;

/**
 *
 * @author ddrempetic
 */
@Named(value = "pogled2")
@RequestScoped
public class Pogled2 {
    
    private String odgovorGrupa;
    private String stanjeGrupa;

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled2() {
        this.stanjeGrupa = "";
        this.odgovorGrupa = "Nije pokrenuta jos nijedna komanda grupe.";        
    }
    
    public void posluziteljPauza(){        
    }
    
    public void posluziteljKreni(){        
    }
    
    public void posluziteljPasivno(){        
    }
    
    public void posluziteljAktivno(){        
    }
    
    public void posluziteljStani(){        
    }
    
    public void posluziteljStanje(){        
    }
    
    public void grupaDodaj(){
        odgovorGrupa = posaljiKomanduGrupe("DODAJ");
        grupaStanje();
    }
    
    public void grupaPrekid(){
        odgovorGrupa = posaljiKomanduGrupe("PREKID");
        grupaStanje();
    }
    
    public void grupaKreni(){
        odgovorGrupa = posaljiKomanduGrupe("KRENI");
        grupaStanje();
    }
    
    public void grupaPauza(){
        odgovorGrupa = posaljiKomanduGrupe("PAUZA");
        grupaStanje();
    }
    
    public void grupaStanje(){
        stanjeGrupa = posaljiKomanduGrupe("STANJE");
    }
    
    private String posaljiKomanduGrupe(String naziv){
        String korisnik = "admin";  //TODO prijavljeni korisnik
        String lozinka = "123456";  //TODO prijavljeni korisnik
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA " + naziv + ";";
        
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        System.out.println(odgovor);
        
        return odgovor;
    }

    public String getOdgovorGrupa() {
        return odgovorGrupa;
    }

    public void setOdgovorGrupa(String odgovorGrupa) {
        this.odgovorGrupa = odgovorGrupa;
    }

    public String getStanjeGrupa() {
        return stanjeGrupa;
    }

    public void setStanjeGrupa(String stanjeGrupa) {
        this.stanjeGrupa = stanjeGrupa;
    }   
}
