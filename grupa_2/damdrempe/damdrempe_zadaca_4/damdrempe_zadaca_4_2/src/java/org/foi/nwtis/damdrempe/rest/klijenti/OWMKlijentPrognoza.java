/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.klijenti;

import java.io.StringReader;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;

/**
 *
 * @author grupa_2
 */
public class OWMKlijentPrognoza extends OWMKlijent {
    
    public OWMKlijentPrognoza(String apiKey) {
        super(apiKey);
    }
    
    //TODO promijeniti svoje
    //TODO vidjeti da li je dobro da dohvaca 5 podataka
    public MeteoPrognoza[] getWeatherForecast(int id, String latitude, String longitude) {

        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        MeteoPrognoza[] mpr = new MeteoPrognoza[5];
        try {
            JsonReader reader = Json.createReader(new StringReader(odgovor));
            JsonObject jo = reader.readObject();
            JsonArray ja = jo.getJsonArray("list");
            
            for (int i = 0; i < 5; i++) {
                JsonObject objekt = ja.getJsonObject(i);
                MeteoPodaci mp = new MeteoPodaci();
                
                System.out.println("datum"+new Date(objekt.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
                mp.setLastUpdate(new Date(objekt.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
                mp.setTemperatureValue(new Double(objekt.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
                mp.setTemperatureMin(new Double(objekt.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
                mp.setTemperatureMax(new Double(objekt.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
                mp.setTemperatureUnit("celsius");
                mp.setHumidityValue(new Double(objekt.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
                mp.setHumidityUnit("%");
                mp.setPressureValue(new Double(objekt.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
                mp.setPressureUnit("hPa");
                mp.setWindSpeedValue(new Double(objekt.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
                mp.setWindSpeedName("");
                mp.setWeatherValue(objekt.getJsonArray("weather").getJsonObject(0).getString("description"));
                mp.setWindDirectionValue(new Double(objekt.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
                mp.setWindDirectionCode("");
                mp.setWindDirectionName("");
                
                
                MeteoPrognoza prognoza = new MeteoPrognoza();
                prognoza.setSat(i);
                prognoza.setId(id);
                prognoza.setPrognoza(mp);

                mpr[i] = prognoza;
            }

        } catch (Exception ex) {
            
        }

        return mpr;
    }
}
