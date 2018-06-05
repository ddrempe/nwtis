/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.serveri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;

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
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //preuzimanje svih korisnika
        //poslati komandu na server
        //vratiti odgovor OK ERR
        
        String odgovor = PomocnaKlasa.posaljiKomanduPosluzitelju("Ovo je test komanda prema posluzitelju u vrijeme " + LocalDateTime.now().toString());
        
        return odgovor;
    }
}
