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
 * Jersey REST client generated for REST resource:we [meteo/{id}]<br>
 * USAGE:
 * <pre>
 *        MeteoRESTKlijentId client = new MeteoRESTKlijentId();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author ddrempetic
 */
public class MeteoRESTKlijentId {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/damdrempe_zadaca_3_1/webresources/";

    /**
     *
     * @param id
     */
    public MeteoRESTKlijentId(String id) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("meteo/{0}", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     *
     * @param id
     */
    public void setResourcePath(String id) {
        String resourcePath = java.text.MessageFormat.format("meteo/{0}", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param <T>
     * @param responseType Class representing the response
     * @param requestEntity request data@return response object (instance of responseType class)
     * @return 
     */
    public <T> T putJson(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
    }

    /**
     * @param <T>
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)
     */
    public <T> T deleteJson(Class<T> responseType) throws ClientErrorException {
        return webTarget.request().delete(responseType);
    }

    /**
     * @param <T>
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)
     */
    public <T> T getJson(Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     *
     */
    public void close() {
        client.close();
    }
    
}
