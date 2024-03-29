package org.foi.nwtis.damdrempe.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:we [meteo]<br>
 * USAGE:
 * <pre>
 *        MeteoRESTKlijent client = new MeteoRESTKlijent();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author ddrempetic
 */
public class MeteoRESTKlijent {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/damdrempe_zadaca_3_1/webresources/";

    /**
     *
     */
    public MeteoRESTKlijent() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("meteo");
    }

    /**
     * @param <T>
     * @param responseType Class representing the response
     * @param requestEntity request data@return response object (instance of responseType class)
     * @return 
     */
    public <T> T postJson(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
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
