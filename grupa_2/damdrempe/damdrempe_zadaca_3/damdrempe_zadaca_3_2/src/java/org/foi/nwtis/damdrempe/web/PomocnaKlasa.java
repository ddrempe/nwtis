/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author ddrempetic
 */
public class PomocnaKlasa {
    
    public static String napraviJsonZaSlanjeParkiraliste(String naziv, String adresa){
        JsonObject jsonOdgovor;        
        jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("naziv", naziv)
                    .add("adresa", adresa)
                    .build());
        
        return jsonOdgovor.toString();
    }
    
}
