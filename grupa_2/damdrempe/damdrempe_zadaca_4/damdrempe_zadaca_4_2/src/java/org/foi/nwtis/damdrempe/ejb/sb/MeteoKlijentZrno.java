/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.OWMKlijentPrognoza;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;

/**
 *
 * @author grupa_2
 */
@Stateless
@LocalBean
public class MeteoKlijentZrno {
    private String apiKey;
    private String gmApiKey;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void postaviKorisnickePodatke(String apiKey, String gmApiKey) {
        this.apiKey = apiKey;
        this.gmApiKey = gmApiKey;
    }
    
    public Lokacija dajLokaciju(String adresa){
        GMKlijent gmk = new GMKlijent(gmApiKey);
        Lokacija lokacija = gmk.getGeoLocation(adresa);
        return lokacija;
    }
    
    public MeteoPrognoza[] dajMeteoPrognoze(int id, String adresa){
        OWMKlijentPrognoza klijentPrognoza = new OWMKlijentPrognoza(apiKey);
        Lokacija l = dajLokaciju(adresa);
        MeteoPrognoza[] meteoPrognoze = klijentPrognoza.getWeatherForecast(id, l.getLatitude(),l.getLongitude());
        
        return meteoPrognoze;
    }
    
}
