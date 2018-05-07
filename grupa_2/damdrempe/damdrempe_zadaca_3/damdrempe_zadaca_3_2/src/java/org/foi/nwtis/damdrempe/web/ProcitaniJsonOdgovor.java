/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author ddrempetic
 */
public class ProcitaniJsonOdgovor {
    private JsonObject odgovorAtribut;
    private String status;
    private String poruka;

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
    
    public String vratiVrijednostAtributaIzOdgovora(String nazivAtributa){
        return odgovorAtribut.getString(nazivAtributa);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public JsonObject getOdgovorAtribut() {
        return odgovorAtribut;
    }

    public void setOdgovorAtribut(JsonObject odgovorAtribut) {
        this.odgovorAtribut = odgovorAtribut;
    }   
}
