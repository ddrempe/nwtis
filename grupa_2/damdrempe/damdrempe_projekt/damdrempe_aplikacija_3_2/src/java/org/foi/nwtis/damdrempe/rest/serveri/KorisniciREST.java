/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.serveri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 * REST Web Service
 *
 * @author ddrempetic
 */
@Path("korisnici")
public class KorisniciREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of KorisniciREST
     */
    public KorisniciREST() {
    }

    /**
     * Retrieves representation of an instance of org.foi.nwtis.damdrempe.rest.serveri.KorisniciREST
     * @param korisnik
     * @param lozinka
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String preuzimanjeSvihKorisnika(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; LISTAJ;";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        
        boolean uspjesno = odgovor.contains("OK");
        String poruka = odgovor.substring(odgovor.indexOf(";") + 2); 
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        if (uspjesno) {
            String nizKorisnici = odgovor.substring(odgovor.indexOf(";") + 2);   
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(nizKorisnici, JsonElement.class);
            JsonArray jsonArray = element.getAsJsonArray();
            JsonArrayBuilder builder = Json.createArrayBuilder();
            
            for (JsonElement jsonElement : jsonArray) {
                Korisnik k = gson.fromJson(jsonElement, Korisnik.class);
                builder.add(Json.createObjectBuilder()
                        .add("ki", k.getKi())
                        .add("prezime", k.getPrezime())
                        .add("ime", k.getIme())
                        .add("email", k.getEmail())
                        .add("vrsta", k.getVrsta()));
            }
            
            return jsonOdgovor.vratiKompletanJsonOdgovor(builder.build());
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }       
    }   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{korisnickoIme}")
    public String getAutentikacijaPreuzimannje(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
        if(korisnik!=null){
            return preuzimanje(korisnik, lozinka);
        } else {
            return autentikacija(lozinka);           
        }        
    }

    private String preuzimanje(String korisnik, String lozinka) {
        String korisnickoIme = context.getPathParameters().getFirst("korisnickoIme");
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; PREUZMI " + korisnickoIme + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        boolean uspjesno = odgovor.contains("OK");
        String poruka = odgovor.substring(odgovor.indexOf(";") + 2); 
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        if (uspjesno) {
            String nizKorisnik = odgovor.substring(odgovor.indexOf(";") + 2); 
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(nizKorisnik, JsonElement.class);
            JsonArray jsonArray = element.getAsJsonArray();
            JsonArrayBuilder builder = Json.createArrayBuilder();
            
            for (JsonElement jsonElement : jsonArray) {
                Korisnik k = gson.fromJson(jsonElement, Korisnik.class);
                builder.add(Json.createObjectBuilder()
                        .add("ki", k.getKi())
                        .add("prezime", k.getPrezime())
                        .add("ime", k.getIme())
                        .add("email", k.getEmail())
                        .add("vrsta", k.getVrsta()));
            }
            
            return jsonOdgovor.vratiKompletanJsonOdgovor(builder.build());
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }          
    }
    
    private String autentikacija(String lozinka) {
        String korisnik = context.getPathParameters().getFirst("korisnickoIme");
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        
        boolean uspjesno = odgovor.contains("OK");
        String poruka = odgovor.substring(odgovor.indexOf(";") + 2); 
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();        
    }   
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{korisnickoIme}")
    public String dodajKorisnika(@HeaderParam("lozinka") String lozinka, @HeaderParam("ime") String ime, @HeaderParam("prezime") String prezime) {
        String korisnik = context.getPathParameters().getFirst("korisnickoIme");
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; DODAJ " + prezime + " " + ime + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        
        boolean uspjesno = odgovor.contains("OK");
        String poruka = odgovor.substring(odgovor.indexOf(";") + 2); 
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();            
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{korisnickoIme}")
    public String azurirajKorisnika(@HeaderParam("lozinka") String lozinka, @HeaderParam("ime") String ime, @HeaderParam("prezime") String prezime) {
        String korisnik = context.getPathParameters().getFirst("korisnickoIme");
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; AZURIRAJ " + prezime + " " + ime + ";";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        
        boolean uspjesno = odgovor.contains("OK");
        String poruka = odgovor.substring(odgovor.indexOf(";") + 2); 
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();            
    }
}
