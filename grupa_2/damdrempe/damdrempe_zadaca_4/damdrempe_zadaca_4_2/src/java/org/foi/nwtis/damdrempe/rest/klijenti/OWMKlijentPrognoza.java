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
 * Proširenje klase za dohvat meteopodataka.
 *
 * @author ddrempetic
 */
public class OWMKlijentPrognoza extends OWMKlijent {

    /**
     * Konstruktor klase.
     *
     * @param apiKey
     */
    public OWMKlijentPrognoza(String apiKey) {
        super(apiKey);
    }

    //TODO promijeniti svoje
    //TODO vidjeti da li je dobro da dohvaca 5 podataka
    /**
     * Za objekt s određenim identifikatorom i lokacijom vraća meteoprognoze.
     *
     * @param id identifikator objekta
     * @param latitude geografska širina
     * @param longitude geografska dužina
     * @return niz meteoprognoza dohvaćenih preko servisa
     */
    public MeteoPrognoza[] getWeatherForecast(int id, String latitude, String longitude) {
        String odgovor = napraviZahtjev(latitude, longitude);

        MeteoPrognoza[] meteoprognoze = new MeteoPrognoza[40];
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(odgovor));
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("list");

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObjectPrognoza = jsonArray.getJsonObject(i);

                MeteoPodaci meteopodaci = parsajMeteoPodatke(jsonObjectPrognoza);

                MeteoPrognoza prognoza = new MeteoPrognoza();
                prognoza.setSat(i);
                prognoza.setId(id);
                prognoza.setPrognoza(meteopodaci);

                meteoprognoze[i] = prognoza;
            }
        } catch (Exception ex) {

        }

        return meteoprognoze;
    }

    /**
     * Iz JSON objekta iz odgovora čita podatke i sprema u objekt meteopodataka.
     * @param o json objekt u kojem su meteopodaci
     * @return meteopodaci
     */
    private MeteoPodaci parsajMeteoPodatke(JsonObject o) {
        MeteoPodaci mp = new MeteoPodaci();
        
        mp.setLastUpdate(new Date(o.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
        mp.setTemperatureValue(new Double(o.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
        mp.setTemperatureMin(new Double(o.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
        mp.setTemperatureMax(new Double(o.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
        mp.setTemperatureUnit("celsius");
        mp.setHumidityValue(new Double(o.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
        mp.setHumidityUnit("%");
        mp.setPressureValue(new Double(o.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
        mp.setPressureUnit("hPa");
        mp.setWindSpeedValue(new Double(o.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
        mp.setWindSpeedName("");
        mp.setWeatherValue(o.getJsonArray("weather").getJsonObject(0).getString("description"));
        mp.setWindDirectionValue(new Double(o.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
        mp.setWindDirectionCode("");
        mp.setWindDirectionName("");
        
        return mp;
    }

    /**
     * Šalje zahtjev za prognozu od 5 dana svaka 3 sata za željenu lokaciju.
     * @param latitude
     * @param longitude
     * @return odgovor zahtjeva
     */
    private String napraviZahtjev(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);
        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

        return odgovor;
    }
}
