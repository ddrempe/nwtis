/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:we
 * [korisnici/{korisnickoIme}]<br>
 * USAGE:
 * <pre>
 *        KorisniciRESTKlijentKorime client = new KorisniciRESTKlijentKorime();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author ddrempetic
 */
public class KorisniciRESTKlijentKorime {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/damdrempe_aplikacija_3_2/webresources/";

    public KorisniciRESTKlijentKorime(String korisnickoIme) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("korisnici/{0}", new Object[]{korisnickoIme});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String korisnickoIme) {
        String resourcePath = java.text.MessageFormat.format("korisnici/{0}", new Object[]{korisnickoIme});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param lozinka header parameter[REQUIRED]
     * @param ime header parameter[REQUIRED]
     * @param prezime header parameter[REQUIRED]
     */
    public <T> T dodajKorisnika(Class<T> responseType, String lozinka, String ime, String prezime) throws ClientErrorException {
        return webTarget.request().header("lozinka", lozinka).header("ime", ime).header("prezime", prezime).post(null, responseType);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param lozinka header parameter[REQUIRED]
     * @param ime header parameter[REQUIRED]
     * @param prezime header parameter[REQUIRED]
     */
    public <T> T azurirajKorisnika(Class<T> responseType, String lozinka, String ime, String prezime) throws ClientErrorException {
        Entity<?> empty = Entity.text("");
        return webTarget.request().header("lozinka", lozinka).header("ime", ime).header("prezime", prezime).put(empty, responseType);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnik header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T getAutentikacijaPreuzimannje(Class<T> responseType, String korisnik, String lozinka) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
