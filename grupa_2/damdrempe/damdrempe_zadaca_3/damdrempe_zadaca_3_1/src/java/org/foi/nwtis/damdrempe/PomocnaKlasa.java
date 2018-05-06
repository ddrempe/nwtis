/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe;

import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.OWMKlijent;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 *
 * @author ddrempetic
 */
public class PomocnaKlasa {
    
    public static Lokacija dohvatiGMLokaciju(String adresa){
        ServletContext sc = SlusacAplikacije.servletContext; 
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        String gmapiKey = k.dajPostavku("gmapikey");
        GMKlijent gmk = new GMKlijent(gmapiKey);
        return gmk.getGeoLocation(adresa);
    }
    
    public static MeteoPodaci dohvatiOWMMeteo(String latitude, String longitude){
        ServletContext sc = SlusacAplikacije.servletContext; 
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        String apiKey = k.dajPostavku("apikey");
        OWMKlijent owmk = new OWMKlijent(apiKey);
        return owmk.getRealTimeWeather(latitude, longitude);
    }
    
}
