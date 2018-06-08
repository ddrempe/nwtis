package org.foi.nwtis.damdrempe.rest.klijenti;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;
import org.foi.nwtis.damdrempe.web.podaci.Vozilo;

/**
 * Klasa koja iz teksta odgovora kojeg je poslao REST servis cita sve elemente.
 * @author ddrempetic
 */
public class ProcitaniJsonOdgovor {
    private JsonObject odgovorAtribut;
    private JsonArray odgovorNiz;
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
            odgovorNiz = jsonObject.getJsonArray("odgovor");
            this.odgovorAtribut = odgovorNiz.getJsonObject(0);            
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
    
    private String vratiVrijednostString(JsonObject atribut, String nazivAtributa){
        return atribut.getString(nazivAtributa);
    }
    
    private int vratiVrijednostInt(JsonObject atribut, String nazivAtributa){
        return atribut.getInt(nazivAtributa);
    }
    
    public List<Parkiraliste> vratiNizParkiralista(){
        List<Parkiraliste> listaParkiralista = new ArrayList<>();
        for (JsonValue jsonValue : odgovorNiz) {
            String objektString = jsonValue.toString();            
            JsonReader jsonReader = Json.createReader(new StringReader(objektString));
            JsonObject jsonObject = jsonReader.readObject();  
            
            Parkiraliste p = new Parkiraliste();
            p.setId(vratiVrijednostInt(jsonObject, "id"));
            p.setNaziv(vratiVrijednostString(jsonObject, "naziv"));
            p.setAdresa(vratiVrijednostString(jsonObject, "adresa"));
            p.setStatus(vratiVrijednostString(jsonObject, "status"));
            p.setKapacitet(vratiVrijednostInt(jsonObject, "kapacitet"));
            
            listaParkiralista.add(p);
        }
        
        return listaParkiralista;
    }
    
    public List<Vozilo> vratiNizVozila(){
        List<Vozilo> listaVozila = new ArrayList<>();
        for (JsonValue jsonValue : odgovorNiz) {
            String objektString = jsonValue.toString();            
            JsonReader jsonReader = Json.createReader(new StringReader(objektString));
            JsonObject jsonObject = jsonReader.readObject();  
            
            Vozilo v = new Vozilo();
            v.setParkiraliste(vratiVrijednostInt(jsonObject, "idParkiraliste"));
            v.setRegistracija(vratiVrijednostString(jsonObject, "registracija"));
            
            String akcijaString = vratiVrijednostString(jsonObject, "status");
            Vozilo.StatusVozila akcijaStatus = (akcijaString == "ULAZ") ? Vozilo.StatusVozila.ULAZ : Vozilo.StatusVozila.IZLAZ;
            v.setAkcija(akcijaStatus);          
            
            listaVozila.add(v);
        }
        
        return listaVozila;
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

    public JsonArray getOdgovorNiz() {
        return odgovorNiz;
    }

    public void setOdgovorNiz(JsonArray odgovorNiz) {
        this.odgovorNiz = odgovorNiz;
    }
}
