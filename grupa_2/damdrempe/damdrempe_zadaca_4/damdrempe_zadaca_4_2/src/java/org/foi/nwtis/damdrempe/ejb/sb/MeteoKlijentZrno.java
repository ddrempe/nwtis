package org.foi.nwtis.damdrempe.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.OWMKlijentPrognoza;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;

/**
 * Zrno za dohvaćanje lokacije i meteoprognoza putem odgovarajućih klijenata.
 * @author ddrempetic
 */
@Stateless
@LocalBean
public class MeteoKlijentZrno {
    private String apiKey;
    private String gmApiKey;

    /**
     * Metoda za postavljanje API ključeva.
     * @param apiKey
     * @param gmApiKey 
     */
    public void postaviKorisnickePodatke(String apiKey, String gmApiKey) {
        this.apiKey = apiKey;
        this.gmApiKey = gmApiKey;
    }
    
    /**
     * Dohvaća lokaciju preko GM klijenta.
     * @param adresa
     * @return 
     */
    public Lokacija dajLokaciju(String adresa){
        GMKlijent gmk = new GMKlijent(gmApiKey);
        Lokacija lokacija = gmk.getGeoLocation(adresa);
        return lokacija;
    }
    
    /**
     * Dohvaća meteoprognozu za parkiralište s zadanom adresom.
     * @param id identifikator parkiralista koji se uparuje i vraća s dohvaćenim podacima 
     * @param adresa adresa parkirališta za koje se dohvaćaju meteopodaci
     * @return 
     */
    public MeteoPrognoza[] dajMeteoPrognoze(int id, String adresa){
        OWMKlijentPrognoza klijentPrognoza = new OWMKlijentPrognoza(apiKey);
        Lokacija l = dajLokaciju(adresa);
        MeteoPrognoza[] meteoPrognoze = klijentPrognoza.getWeatherForecast(id, l.getLatitude(),l.getLongitude());
        
        return meteoPrognoze;
    }
    
}
