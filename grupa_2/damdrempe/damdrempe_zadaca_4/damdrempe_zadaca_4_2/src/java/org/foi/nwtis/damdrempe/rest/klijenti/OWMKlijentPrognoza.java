/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.klijenti;

import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;

/**
 *
 * @author grupa_2
 */
public class OWMKlijentPrognoza extends OWMKlijent {
    
    public OWMKlijentPrognoza(String apiKey) {
        super(apiKey);
    }
    
    //TODO fali
    public MeteoPrognoza[] getWeatherForecast(int id, String latitude, String longitude){
        //TODO treba preuzeti meteo prognoze od web servisa
        
        MeteoPrognoza[] meteoPrognoze = new MeteoPrognoza[1];
        return meteoPrognoze;
    
    }
}
