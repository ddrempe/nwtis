package org.foi.nwtis.damdrempe.pomocno;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.OWMKlijent;
import org.foi.nwtis.damdrempe.web.podaci.Dnevnik;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 * Klasa sa svim pomocnim operacijama koje se koriste na više mjesta.
 * Trenutno sadržava samo operacije za dohvat Google Maps lokacije i OpenWeatherMaps meteopodataka.
 * @author ddrempetic
 */
public class PomocnaKlasa {
    
    /**
     * Dohvaca lokaciju pomocu Google Maps servisa
     * @param adresa adresa za koju se trazi lokacija
     * @return objekt Lokacija
     */
    public static Lokacija dohvatiGMLokaciju(String adresa){
        ServletContext sc = SlusacAplikacije.servletContext; 
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        String gmapiKey = k.dajPostavku("gmapikey");
        GMKlijent gmk = new GMKlijent(gmapiKey);
        return gmk.getGeoLocation(adresa);
    }    
    
    /**
     * Dohvaca meteopodatke pomocu OpenWeatherMaps servisa
     * @param latitude geografska širina
     * @param longitude geografska dužina
     * @return objekt koji sadrži sve dostupne meteopodatke za lokaciju
     */
    public static MeteoPodaci dohvatiOWMMeteo(String latitude, String longitude){
        ServletContext sc = SlusacAplikacije.servletContext; 
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        String apiKey = k.dajPostavku("apikey");
        OWMKlijent owmk = new OWMKlijent(apiKey);
        MeteoPodaci meteo = null;
        try {
            meteo = owmk.getRealTimeWeather(latitude, longitude);            
        } catch (NullPointerException ex) {
            Logger.getLogger("Nije moguće dohvatiti sve meteo podatke!");
        }
        return meteo;
    }  
    
    /**
     * Provjerava da li korisnik postoji
     * @param korisnickoIme
     * @param lozinka
     * @return true ako postoji, inače false
     */
    public static boolean autentificirajKorisnika(String korisnickoIme, String lozinka){
        boolean postoji = false;
        Korisnik korisnik = new Korisnik(korisnickoIme, lozinka);
        
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            postoji = bpo.korisniciSelectKorisnikPostoji(korisnik);
            bpo.zatvoriVezu();            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return postoji;
    }
    
    /**
     * Zapisuje novi zapis u tablicu dnevnik
     * @param dnevnik 
     */
    public static void zapisiUDnevnik(Dnevnik dnevnik){
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            bpo.dnevnikInsert(dnevnik);
            bpo.zatvoriVezu();            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String dajTrenutnuIPAdresu(){
        String ipAdresa = "";
        try {
            ipAdresa = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ipAdresa;
    }
}
