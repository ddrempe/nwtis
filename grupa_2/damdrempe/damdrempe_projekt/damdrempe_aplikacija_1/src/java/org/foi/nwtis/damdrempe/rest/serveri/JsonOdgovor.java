package org.foi.nwtis.damdrempe.rest.serveri;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

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
     * Gradi json niz od liste dobivenih parkirališta.
     * @param svaParkiralista lista parkirališta
     * @return json rezultat u obliku stringa
     */
    public JsonArray postaviSvaParkiralistaJsonDio(List<Parkiraliste> svaParkiralista){
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();
        for (Parkiraliste parkiraliste : svaParkiralista) {
            jsonBuilder.add(Json.createObjectBuilder()
                    .add("id", parkiraliste.getId())
                    .add("naziv", parkiraliste.getNaziv())
                    .add("adresa", parkiraliste.getAdresa())
                    .add("latitude", parkiraliste.getGeoloc().getLatitude())
                    .add("longitude", parkiraliste.getGeoloc().getLongitude()));
        }
        
        return jsonBuilder.build();
    }
    
    /**
     * Gradi json objekt od dobivenog parkirališta.
     * @param parkiraliste jedno parkiralište
     * @return json rezultat u obliku stringa
     */
    public JsonArray postaviParkiralisteJsonDio(Parkiraliste parkiraliste){
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();
        
        jsonBuilder.add(Json.createObjectBuilder()
                    .add("id", parkiraliste.getId())
                    .add("naziv", parkiraliste.getNaziv())
                    .add("adresa", parkiraliste.getAdresa())
                    .add("latitude", parkiraliste.getGeoloc().getLatitude())
                    .add("longitude", parkiraliste.getGeoloc().getLongitude()));       
        
        return jsonBuilder.build();
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
    public String vratiKompletanJsonOdgovor(JsonArray odgovor){
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