package org.foi.nwtis.damdrempe.pomocno;

import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.OWMKlijent;
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
}
