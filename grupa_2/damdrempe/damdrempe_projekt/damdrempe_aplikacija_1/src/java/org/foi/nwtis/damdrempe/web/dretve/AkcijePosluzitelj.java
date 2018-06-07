/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.rest.serveri.JsonOdgovor;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 *
 * @author ddrempetic
 */
public class AkcijePosluzitelj {
    
    public static String dodaj(String ime, String prezime){    
        String odgovor = "";
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if(bpo.korisniciSelectImePrezimePostoji(ime, prezime) == true){
                odgovor = OdgovoriKomandi.POSLUZITELJ_DODAJ_ERR;
            } else {
                Korisnik korisnik = stvoriKorisnika(ime, prezime);
                bpo.korisniciInsert(korisnik);
                odgovor = OdgovoriKomandi.POSLUZITELJ_DODAJ_OK;
            }    
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return odgovor;
    }
    
    public static String azuriraj(String korisnickoIme, String ime, String prezime){
        String odgovor = "";
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if(bpo.korisniciSelectKorimePostoji(korisnickoIme) == false){
                odgovor = OdgovoriKomandi.POSLUZITELJ_AZURIRAJ_ERR;
            } else {
                bpo.korisniciUpdatePrezimeIme(korisnickoIme, ime, prezime);
                odgovor = OdgovoriKomandi.POSLUZITELJ_AZURIRAJ_OK;
            }    
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return odgovor;
    }
    
    private static Korisnik stvoriKorisnika(String ime, String prezime){
        Korisnik korisnik = new Korisnik();
        korisnik.setIme(ime);
        korisnik.setPrezime(prezime);
        if(prezime.length() >= 9){
            prezime = prezime.substring(0, 9);
        }
        
        String korisnickoIme = ime.substring(0, 1) + prezime;
        korisnickoIme = korisnickoIme.toLowerCase();
        korisnik.setKor_ime(korisnickoIme);
        korisnik.setLozinka(korisnickoIme);
        korisnik.setEmail_adresa(korisnickoIme + "@foi.hr");
        
        return korisnik;
    }
    
    public static String listaj(){
        List<Korisnik> korisnici = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            korisnici = bpo.korisniciSelectSviKorisnici();
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(korisnici.isEmpty()){
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_ERR;          
        } else {
            JsonOdgovor jsonOdgovor = new JsonOdgovor(true, "");
            JsonArray korisniciJsonDio = jsonOdgovor.postaviSviKorisniciJsonDio(korisnici);            
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_OK + korisniciJsonDio.toString();
        }       
    }

    public static String preuzmi(String korisnickoIme){
        Korisnik korisnik = new Korisnik();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            korisnik = bpo.korisniciSelectKorimeKorisnik(korisnickoIme);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(false){  //TODO provjeriti ako je korisnik prazan
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_ERR;          
        } else {
            JsonOdgovor jsonOdgovor = new JsonOdgovor(true, "");
            JsonArray korisniciJsonDio = jsonOdgovor.postaviKorisnikJsonDio(korisnik);            
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_OK + korisniciJsonDio.toString();
        }    
    }    
    
    public static String pauza(){
        if (ServerSustava.serverKomandePokrenut == false) {
            return OdgovoriKomandi.POSLUZITELJ_PAUZA_ERR;
        }
        
        ServerSustava.serverKomandePokrenut = false;
        return OdgovoriKomandi.POSLUZITELJ_PAUZA_OK;
    }
    
    public static String kreni(){
        if (ServerSustava.serverKomandePokrenut == true) {
            return OdgovoriKomandi.POSLUZITELJ_KRENI_ERR;
        }
        
        ServerSustava.serverKomandePokrenut = true;
        return OdgovoriKomandi.POSLUZITELJ_KRENI_OK;
    }
    
    public static String pasivno(){
        if (ServerSustava.serverMeteoAktivan == false) {
            return OdgovoriKomandi.POSLUZITELJ_PASIVNO_ERR;
        }
        
        ServerSustava.serverMeteoAktivan = false;
        return OdgovoriKomandi.POSLUZITELJ_PASIVNO_OK;
    }
    
    public static String aktivno(){
        if (ServerSustava.serverMeteoAktivan == true) {
            return OdgovoriKomandi.POSLUZITELJ_AKTIVNO_ERR;
        }
        
        ServerSustava.serverMeteoAktivan = true;
        return OdgovoriKomandi.POSLUZITELJ_AKTIVNO_OK;
    }
    
    public static String stani(){
        if (ServerSustava.serverPotpunoPokrenut == false) {
            return OdgovoriKomandi.POSLUZITELJ_STANI_ERR;
        }
        
        //TODO zaustavi server i prekini s radom
        ServerSustava.serverPotpunoPokrenut = false;
        return OdgovoriKomandi.POSLUZITELJ_STANI_OK;
    }
    
    public static String stanje(){
        String odgovor = "";
        if(ServerSustava.serverKomandePokrenut && ServerSustava.serverMeteoAktivan){
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK;
        } else if(ServerSustava.serverKomandePokrenut && !ServerSustava.serverMeteoAktivan){
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK2;
        } else if(!ServerSustava.serverKomandePokrenut && ServerSustava.serverMeteoAktivan){
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK3;
        } else if(!ServerSustava.serverKomandePokrenut && !ServerSustava.serverMeteoAktivan){
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK4;
        }
        
        return odgovor;
    }
}
