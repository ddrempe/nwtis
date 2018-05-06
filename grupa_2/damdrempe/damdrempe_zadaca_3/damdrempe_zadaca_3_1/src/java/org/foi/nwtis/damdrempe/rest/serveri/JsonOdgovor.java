/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.serveri;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

/**
 *
 * @author ddrempetic
 */
public class JsonOdgovor {
    
    private boolean uspjesno;
    private String poruka;
    private String status;

    public JsonOdgovor(boolean uspjesno, String poruka) {
        this.uspjesno = uspjesno;
        this.poruka = poruka;
        this.status = uspjesno ? "OK" : "ERR";
    }
    
    public JsonArray postaviParkiralistaJsonDio(List<Parkiraliste> svaParkiralista){
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
    
    public JsonArray postaviMeteoJsonDio(MeteoPodaci meteo, Parkiraliste parkiraliste){
        JsonArrayBuilder jsonBuilder = Json.createArrayBuilder();
        
        jsonBuilder.add(Json.createObjectBuilder()
                .add("temp", meteo.getTemperatureValue())
                .add("vlaga", meteo.getHumidityValue())
                .add("tlak", meteo.getPressureValue())
                .add("naziv", parkiraliste.getNaziv())
                .add("adresa", parkiraliste.getAdresa()));        
        
        return jsonBuilder.build();
    }
    
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
