/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:we
 * [parkiranje/{id}/vozila]<br>
 * USAGE:
 * <pre>
 *        ParkiranjeRESTKlijentIdVozila client = new ParkiranjeRESTKlijentIdVozila();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author ddrempetic
 */
public class ParkiranjeRESTKlijentIdVozila {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/damdrempe_aplikacija_1/webresources/";

    public ParkiranjeRESTKlijentIdVozila(String id) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("parkiranje/{0}/vozila", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String id) {
        String resourcePath = java.text.MessageFormat.format("parkiranje/{0}/vozila", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnickoIme header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T getJsonVozila(Class<T> responseType, String korisnickoIme, String lozinka) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnickoIme", korisnickoIme).header("lozinka", lozinka).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
