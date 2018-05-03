/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.serveri;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

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
     * Retrieves representation of an instance of org.foi.nwtis.matnovak.rest.serveri.MeteoREST
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
       return "{\"odgovor\": [{\"id\": 1, "
               +"\"naziv\": \"Podzemna garaža\","
               +"\"adresa\": \"Kapucinski trg 1, Varaždin\"}"
               +",{\"id\": 2, "
               +"\"naziv\": \"FOI2\","
               +"\"adresa\": \"Petra krešimira IV, Varaždin\"}],"
               + "\"status\": \"OK\"}"; 
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String postJson(String podaci) {
        //TODO provjeri da li postoji parkiralište s id iz argumenta
        //TODO ako ne postoji dodati u bazu pdoataka
       return "{\"odgovor\": [], "
               +"\"status\": \"OK\"}"; 
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getJson(@PathParam("id") String id) {
        //TODO provjeri da li postoji parkiralište s id iz argumenta
        if(Integer.parseInt(id) == 0){
            return "{\"odgovor\": [], "
               +"\"status\": \"ERR\", "
               +"\"poruka\": \"Parkiralište ne postoji\"}";
        } else {
            return "{\"odgovor\": [{\"id\": 1, "
               +"\"naziv\": \"Podzemna garaža\","
               +"\"adresa\": \"Kapucinski trg 1, Varaždin\"}],"
               + "\"status\": \"OK\"}"; 
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String postJson(@PathParam("id") String id, String podaci) {
        //TODO provjeri da li postoji parkiralište s id iz argumenta
        return "{\"odgovor\": [], "
               +"\"status\": \"ERR\","
               +"\"poruka\": \"Nije dozvoljeno\"}"; 
    }
    
    
    /**
     * PUT method for updating or creating an instance of MeteoREST
     * @param content representation for the resource
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String putJson(@PathParam("id") String id, String podaci) {
        //TODO provjeri da li postoji parkiralište s id iz argumenta
        //TODO ako postoji ažurirati u bazi pdoataka
        if(Integer.parseInt(id) == 0){
            return "{\"odgovor\": [], "
               +"\"status\": \"ERR\", "
               +"\"poruka\": \"Parkiralište ne postoji\"}";
        } else {
            return "{\"odgovor\": [],"
               + "\"status\": \"OK\"}"; 
        }
    }
}
