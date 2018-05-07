/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.serveri;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.damdrempe.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;
import org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS;

/**
 * REST Web Service
 *
 * @author grupa_2
 */
@Path("meteo")
public class MeteoREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MeteoREST
     */
    public MeteoREST() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.matnovak.rest.serveri.MeteoREST
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        boolean uspjesno = true;
        String poruka = "";
        List<Parkiraliste> svaParkiralista = new ArrayList<>();

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            svaParkiralista = bpo.parkiralistaSelect();
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        JsonArray parkiralistaJsonDio = jsonOdgovor.postaviSvaParkiralistaJsonDio(svaParkiralista);
        return jsonOdgovor.vratiKompletanJsonOdgovor(parkiralistaJsonDio);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String postJson(String podaci) {
        boolean uspjesno = true;
        String poruka = "";

        Parkiraliste parkiraliste;
        try {
            Gson gson = new Gson();
            parkiraliste = gson.fromJson(podaci, Parkiraliste.class);

            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (bpo.parkiralistaSelectNaziv(parkiraliste.getNaziv())) {
                uspjesno = false;
                poruka = "Parkiraliste s nazivom " + parkiraliste.getNaziv() + " vec postoji!";
            } else {
                Lokacija lokacija = PomocnaKlasa.dohvatiGMLokaciju(parkiraliste.getAdresa());
                bpo.parkiralistaInsert(parkiraliste.getNaziv(), parkiraliste.getAdresa(), lokacija.getLatitude(), lokacija.getLongitude());
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(MeteoREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getJson(@PathParam("id") String id) {
        boolean uspjesno = true;
        String poruka = "";
        int idParkiralista = Integer.parseInt(id);

        Parkiraliste p = new Parkiraliste();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (!bpo.parkiralistaSelectId(idParkiralista)) {
                uspjesno = false;
                poruka = "Parkiraliste s id " + id + " ne postoji!";
            } else {
                p = bpo.parkiralistaSelectIdVrati(idParkiralista);
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(MeteoREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        if (uspjesno) {
            JsonArray meteoJsonDio = jsonOdgovor.postaviParkiralisteJsonDio(p);
            return jsonOdgovor.vratiKompletanJsonOdgovor(meteoJsonDio);
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }
    }

    /**
     * PUT method for updating or creating an instance of MeteoREST
     *
     * @param id
     * @param podaci
     * @return 
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String putJson(@PathParam("id") String id, String podaci) {
        boolean uspjesno = true;
        String poruka = "";
        int idParkiralista = Integer.parseInt(id);

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (!bpo.parkiralistaSelectId(idParkiralista)) {
                uspjesno = false;
                poruka = "Parkiraliste s id " + id + " ne postoji!";

            } else {
                Gson gson = new Gson();
                Parkiraliste parkiraliste = gson.fromJson(podaci, Parkiraliste.class);
                Lokacija lokacija = PomocnaKlasa.dohvatiGMLokaciju(parkiraliste.getAdresa());
                parkiraliste.setId(idParkiralista);
                parkiraliste.setGeoloc(lokacija);
                bpo.parkiralistaUpdate(parkiraliste); 
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(MeteoREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String deleteJson(@PathParam("id") String id) {
        boolean uspjesno = true;
        String poruka = "";
        int idParkiralista = Integer.parseInt(id);

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (!bpo.parkiralistaSelectId(idParkiralista)) {
                uspjesno = false;
                poruka = "Parkiraliste s id " + id + " ne postoji!";
            } else {
                if (bpo.meteoSelectIdParkiralista(idParkiralista)){
                    uspjesno = false;
                    poruka = "Postoje meteopodaci za parkiraliste s id " + id + " !"; 
                } else {
                    bpo.parkiralistaDelete(idParkiralista); 
                }                
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(MeteoREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }
}
