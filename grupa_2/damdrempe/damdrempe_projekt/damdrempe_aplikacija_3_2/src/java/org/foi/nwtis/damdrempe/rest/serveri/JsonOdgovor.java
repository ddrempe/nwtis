package org.foi.nwtis.damdrempe.rest.serveri;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Klasa za izgradnju odgovora u JSON formatu.
 * Koristi se u REST servisu.
 * @author ddrempetic
 */
public class JsonOdgovor {
    
    private boolean uspjesno;
    private String poruka;
    private String status;

    /**
     * Konstruktor klase
     * @param uspjesno
     * @param poruka 
     */
    public JsonOdgovor(boolean uspjesno, String poruka) {
        this.uspjesno = uspjesno;
        this.poruka = poruka;
        this.status = uspjesno ? "OK" : "ERR";
    }    
    
    /**
     * Gradi kompletnu strukturu json odgovora koji mora vracati REST servis.
     * Unutar odgovora postoji atribut odgovor koji je u ovom slučaju prazan.
     * @return json rezultat u obliku stringa
     */
    public String vratiKompletanJsonOdgovor(){
        JsonObject jsonOdgovor;        

        if (uspjesno) {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", "[]")
                    .add("status", "OK")
                    .build());
        } else {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", "[]")
                    .add("status", "ERR")
                    .add("poruka", poruka)
                    .build());
        }
        
        return jsonOdgovor.toString();
    }
    
    /**
     * Gradi kompletnu strukturu json odgovora koji mora vracati REST servis.
     * Unutar odgovora se šalje poseban dio.
     * Primjerice poseban dio može sadržavati podatke o jednom parkiralištu ili o svim parkiralištima.
     * @param odgovor
     * @return json rezultat u obliku stringa
     */
    public String vratiKompletanJsonOdgovor(String odgovor){
        JsonObject jsonOdgovor;        

        if (uspjesno) {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", odgovor)
                    .add("status", "OK")
                    .build());
        } else {
            jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                    .add("odgovor", odgovor)
                    .add("status", "ERR")
                    .add("poruka", poruka)
                    .build());
        }
        
        return jsonOdgovor.toString();
    }    
}