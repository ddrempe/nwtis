package org.foi.nwtis.damdrempe.web;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Klasa koja iz teksta odgovora kojeg je poslao REST servis cita sve elemente.
 * @author ddrempetic
 */
public class ProcitaniJsonOdgovor {
    private JsonObject odgovorAtribut;
    private String status;
    private String poruka;

    /**
     * Cita sve elemente odgovora.
     * @param jsonOdgovor
     */
    public ProcitaniJsonOdgovor(String jsonOdgovor) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonOdgovor));
        JsonObject jsonObject = jsonReader.readObject();        
        
        try {
            JsonArray jsonArray = jsonObject.getJsonArray("odgovor");
            this.odgovorAtribut = jsonArray.getJsonObject(0);            
        } catch (Exception e) {
            this.odgovorAtribut = Json.createObjectBuilder().build();
        }      
        
        this.status = jsonObject.getString("status");
        try {
            this.poruka = jsonObject.getString("poruka");            
        } catch (NullPointerException e) {
            this.poruka = "";
        }
    } 
    
    /**
     *
     * @param nazivAtributa
     * @return
     */
    public String vratiVrijednostAtributaIzOdgovora(String nazivAtributa){
        return odgovorAtribut.getString(nazivAtributa);
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     *
     * @param poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     *
     * @return
     */
    public JsonObject getOdgovorAtribut() {
        return odgovorAtribut;
    }

    /**
     *
     * @param odgovorAtribut
     */
    public void setOdgovorAtribut(JsonObject odgovorAtribut) {
        this.odgovorAtribut = odgovorAtribut;
    }   
}
