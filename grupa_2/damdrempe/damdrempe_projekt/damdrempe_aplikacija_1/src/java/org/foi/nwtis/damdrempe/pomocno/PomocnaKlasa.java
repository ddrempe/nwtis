package org.foi.nwtis.damdrempe.pomocno;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.OWMKlijent;
import org.foi.nwtis.damdrempe.web.dretve.RadnaDretva;
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
        KorisnikPodaci korisnik = new KorisnikPodaci(korisnickoIme, lozinka);
        
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
    
    public static KorisnikPodaci dohvatiKorisnickePodatkeZaSvn(){
        String korisnickoIme;
        String lozinka;        
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Konfig");
        
        korisnickoIme = konf.dajPostavku("svn.username");
        lozinka = konf.dajPostavku("svn.password");
        KorisnikPodaci korisnik = new KorisnikPodaci(korisnickoIme, lozinka);
        
        return korisnik;
    }
    
    /**
     * Čita niz znakova sa konzole koje je poslao korisnik.
     *
     * @return vraća pročitani niz znakova koji predstavlja komandu serveru
     */
    public static String procitajKomandu(Socket socket) {
        StringBuffer buffer = new StringBuffer();

        try {
            InputStream is = socket.getInputStream();

            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                buffer.append((char) znak);
            }
        } catch (IOException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buffer.toString();
    }
    
    /**
     * Ispituje znakovni niz prema zadanom regularnom izrazu
     *
     * @param komanda znakovni niz koji predstavlja komandu za server
     * @param regularniIzraz regularni izraz koji komanda mora zadovoljiti
     * @return true ako je komanda ispravna, false ako nije
     */
    public static Matcher provjeriIspravnostKomande(String komanda, String regularniIzraz) {
        Pattern pattern = Pattern.compile(regularniIzraz);
        Matcher m = pattern.matcher(komanda);

        return m;
    }
    
    /**
     * Služi za slanje odgovora na konzolu korisnika sustava.
     *
     * @param odgovor tekst odgovora koji se šalje
     * @param socket
     */
    public static void posaljiOdgovor(String odgovor, Socket socket) {
        try {
            OutputStream os;
            os = socket.getOutputStream();
            os.write(odgovor.getBytes());
            os.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
