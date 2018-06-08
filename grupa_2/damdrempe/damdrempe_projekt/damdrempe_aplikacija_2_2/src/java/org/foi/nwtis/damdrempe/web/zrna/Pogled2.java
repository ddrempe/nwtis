/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;

/**
 *
 * @author ddrempetic
 */
@Named(value = "pogled2")
@SessionScoped
public class Pogled2 implements Serializable{
    
    private String odgovorGrupa = "Odgovor grupe: Nije pokrenuta jos nijedna komanda grupe.";  
    private String stanjeGrupa = "Stanje grupe: Nepoznato.";  //TODO prijevod
    
    private String odgovorPosluzitelj = "Odgovor posluzitelja: Nije pokrenuta jos nijedna komanda posluzitelja.";
    private String stanjePosluzitelj = "Stanje posluzitelja: Nepoznato.";

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled2() {
    }
    
    public void posluziteljPauza(){  
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("PAUZA");
    }
    
    public void posluziteljKreni(){ 
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("KRENI");
    }
    
    public void posluziteljPasivno(){ 
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("PASIVNO");
    }
    
    public void posluziteljAktivno(){   
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("AKTIVNO");
    }
    
    public void posluziteljStani(){  
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("STANI");
        stanjePosluzitelj = "Posluzitelj je POTPUNO ZAUSTAVLJEN.";
    }
    
    public void posluziteljStanje(){
        stanjePosluzitelj = posaljiKomanduPosluzitelja("STANJE");
    }
    
    private String posaljiKomanduPosluzitelja(String naziv){
        String korisnik = "admin";  //TODO prijavljeni korisnik
        String lozinka = "123456";  //TODO prijavljeni korisnik
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; " + naziv + ";";
        
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        System.out.println("Primljen odgovor za komandu posluzitelja: " + odgovor);
        
        return odgovor;
    }
    
    public void grupaDodaj(){
        odgovorGrupa = posaljiKomanduGrupe("DODAJ");
    }
    
    public void grupaPrekid(){
        odgovorGrupa = posaljiKomanduGrupe("PREKID");
    }
    
    public void grupaKreni(){
        odgovorGrupa = posaljiKomanduGrupe("KRENI");
    }
    
    public void grupaPauza(){
        odgovorGrupa = posaljiKomanduGrupe("PAUZA");
    }
    
    public void grupaStanje(){
        stanjeGrupa = posaljiKomanduGrupe("STANJE");
    }
    
    private String posaljiKomanduGrupe(String naziv){
        String korisnik = "admin";  //TODO prijavljeni korisnik
        String lozinka = "123456";  //TODO prijavljeni korisnik
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA " + naziv + ";";
        
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        System.out.println("Primljen odgovor za komandu grupe: " + odgovor);
        
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

    public String getOdgovorPosluzitelj() {
        return odgovorPosluzitelj;
    }

    public void setOdgovorPosluzitelj(String odgovorPosluzitelj) {
        this.odgovorPosluzitelj = odgovorPosluzitelj;
    }

    public String getStanjePosluzitelj() {
        return stanjePosluzitelj;
    }

    public void setStanjePosluzitelj(String stanjePosluzitelj) {
        this.stanjePosluzitelj = stanjePosluzitelj;
    }
    
    
}
