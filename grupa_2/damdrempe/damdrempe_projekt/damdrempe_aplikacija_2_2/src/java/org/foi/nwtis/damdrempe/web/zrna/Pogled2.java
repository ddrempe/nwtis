package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;

/**
 * Zrno za pogled 2.
 * @author ddrempetic
 */
@Named(value = "pogled2")
@SessionScoped
public class Pogled2 implements Serializable{
    
    private String odgovorGrupa; 
    private String stanjeGrupa;
    
    private String odgovorPosluzitelj;
    private String stanjePosluzitelj;
    
    private String korisnickoIme;
    private String lozinka;

    /**
     * Creates a new instance of Pogled1
     */
    public Pogled2() {
    }
    
    /**
     * Šalje komandu za pauzu poslužitelja.
     */
    public void posluziteljPauza(){  
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("PAUZA");
    }
    
    /**
     * Šalje komandu za pokretanje poslužitelja.
     */
    public void posluziteljKreni(){ 
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("KRENI");
    }
    
    /**
     * Šalje komandu za pasiviranje poslužitelja.
     */
    public void posluziteljPasivno(){ 
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("PASIVNO");
    }
    
    /**
     * Šalje komandu za aktiviranje poslužitelja.
     */
    public void posluziteljAktivno(){   
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("AKTIVNO");
    }
    
    /**
     * Šalje komandu za zaustavljanje poslužitelja.
     */
    public void posluziteljStani(){  
        odgovorPosluzitelj = posaljiKomanduPosluzitelja("STANI");
        stanjePosluzitelj = "Posluzitelj je POTPUNO ZAUSTAVLJEN.";
    }
    
    /**
     * Šalje komandu za provjeru stanja poslužitelja.
     */
    public void posluziteljStanje(){
        stanjePosluzitelj = posaljiKomanduPosluzitelja("STANJE");
    }
    
    /**
     * Šalje komandu poslužitelja.
     * @param naziv
     * @return 
     */
    private String posaljiKomanduPosluzitelja(String naziv){
        korisnickoIme = PomocnaKlasa.dohvatiPostavku("korisnickoIme");
        lozinka = PomocnaKlasa.dohvatiPostavku("lozinka");
        String komanda = "KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; " + naziv + ";";
        
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        System.out.println("Primljen odgovor za komandu posluzitelja: " + odgovor);
        
        return odgovor;
    }
    
    /**
     * Šalje komandu za registraciju grupe.
     */
    public void grupaDodaj(){
        odgovorGrupa = posaljiKomanduGrupe("DODAJ");
    }
    
    /**
     * Šalje komandu za deregistraciju grupe.
     */
    public void grupaPrekid(){
        odgovorGrupa = posaljiKomanduGrupe("PREKID");
    }
    
    /**
     * Šalje komandu za aktivaciju grupe.
     */
    public void grupaKreni(){
        odgovorGrupa = posaljiKomanduGrupe("KRENI");
    }
    
    /**
     * Šalje komandu za blokiranje grupe.
     */
    public void grupaPauza(){
        odgovorGrupa = posaljiKomanduGrupe("PAUZA");
    }
    
    /**
     * Šalje komandu za provjeru stanja grupe.
     */
    public void grupaStanje(){
        stanjeGrupa = posaljiKomanduGrupe("STANJE");
    }
    
    /**
     * Šalje komandu grupe.
     * @param naziv
     * @return 
     */
    private String posaljiKomanduGrupe(String naziv){
        korisnickoIme = PomocnaKlasa.dohvatiPostavku("korisnickoIme");
        lozinka = PomocnaKlasa.dohvatiPostavku("lozinka");
        String komanda = "KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA " + naziv + ";";
        
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        System.out.println("Primljen odgovor za komandu grupe: " + odgovor);
        
        return odgovor;
    }

    /**
     *
     * @return
     */
    public String getOdgovorGrupa() {
        return odgovorGrupa;
    }

    /**
     *
     * @param odgovorGrupa
     */
    public void setOdgovorGrupa(String odgovorGrupa) {
        this.odgovorGrupa = odgovorGrupa;
    }

    /**
     *
     * @return
     */
    public String getStanjeGrupa() {
        return stanjeGrupa;
    }

    /**
     *
     * @param stanjeGrupa
     */
    public void setStanjeGrupa(String stanjeGrupa) {
        this.stanjeGrupa = stanjeGrupa;
    }   

    /**
     *
     * @return
     */
    public String getOdgovorPosluzitelj() {
        return odgovorPosluzitelj;
    }

    /**
     *
     * @param odgovorPosluzitelj
     */
    public void setOdgovorPosluzitelj(String odgovorPosluzitelj) {
        this.odgovorPosluzitelj = odgovorPosluzitelj;
    }

    /**
     *
     * @return
     */
    public String getStanjePosluzitelj() {
        return stanjePosluzitelj;
    }

    /**
     *
     * @param stanjePosluzitelj
     */
    public void setStanjePosluzitelj(String stanjePosluzitelj) {
        this.stanjePosluzitelj = stanjePosluzitelj;
    }
    
    
}
