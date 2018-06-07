/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import javax.annotation.PostConstruct;
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
    
    private String odgovorPosluzitelj;
    private String stanjePosluzitelj;

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled2() {
        this.stanjeGrupa = "Stanje grupe: Nepoznato.";  //TODO prijevod
        this.odgovorGrupa = "Odgovor grupe: Nije pokrenuta jos nijedna komanda grupe.";  
        
        this.stanjePosluzitelj = "Stanje posluzitelja: Nepoznato.";
        this.odgovorPosluzitelj = "Odgovor posluzitelja: Nije pokrenuta jos nijedna komanda posluzitelja.";
    }
    
    @PostConstruct
    public void init(){
        grupaStanje();
        posluziteljStanje();
    }
    
    public void posluziteljPauza(){  
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("PAUZA");
        posluziteljStanje();
    }
    
    public void posluziteljKreni(){ 
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("KRENI");
        posluziteljStanje();
    }
    
    public void posluziteljPasivno(){ 
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("PASIVNO");
        posluziteljStanje();
    }
    
    public void posluziteljAktivno(){   
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("AKTIVNO");
        posluziteljStanje();
    }
    
    public void posluziteljStani(){  
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("STANI");
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
