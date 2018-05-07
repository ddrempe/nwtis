package org.foi.nwtis.damdrempe.web;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Pomoćna klasa za sve statične metode koje se koriste na više mjesta u aplikaciji.
 * @author ddrempetic
 */
public class PomocnaKlasa {
    
    /**
     * Gradi json tekst preko kojeg se šalje naziv i adresa parkirališta
     * @param naziv
     * @param adresa
     * @return json tekst
     */
    public static String napraviJsonZaSlanjeParkiraliste(String naziv, String adresa){
        JsonObject jsonOdgovor;        
        jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("naziv", naziv)
                    .add("adresa", adresa)
                    .build());
        
        return jsonOdgovor.toString();
    }
    
}
