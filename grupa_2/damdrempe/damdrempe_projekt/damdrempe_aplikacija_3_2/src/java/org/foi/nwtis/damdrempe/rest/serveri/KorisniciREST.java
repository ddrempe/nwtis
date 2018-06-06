/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.serveri;

import com.google.gson.Gson;
import javax.json.JsonArray;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
    public String getJson(@HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; LISTAJ;";
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju(komanda);
        
        boolean uspjesno = odgovor.contains("OK");
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, "Nema korisnika za dohvacanje");
        if (uspjesno) {
            String nizKorisnici = odgovor.substring(odgovor.indexOf(";") + 1);    
            return jsonOdgovor.vratiKompletanJsonOdgovor(nizKorisnici); //TODO dodaju se escape znakovi, treba proslijediti ko JSON baš
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }
        
    }
}
