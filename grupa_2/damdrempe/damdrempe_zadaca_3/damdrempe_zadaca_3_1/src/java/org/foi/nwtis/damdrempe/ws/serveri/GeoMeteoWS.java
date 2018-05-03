/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ws.serveri;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

/**
 *
 * @author grupa_2
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajSvaParkiralista")
    public java.util.List<Parkiraliste> dajSvaParkiralista() {
        List<Parkiraliste> svaParkiralista = new ArrayList<>();
        
        Lokacija lokacija = new Lokacija("46.3089756802915", "16.3396055802915");
        
        for (int i = 0; i < 10; i++) {
            Parkiraliste parkiraliste = new Parkiraliste(i, "Parkiraliste"+i, "Varaždin,Pavlinska 2", lokacija);
            svaParkiralista.add(parkiraliste);
        }
        
        return svaParkiralista;
    }
    
    public void dodajParkiraliste(Parkiraliste parkiraliste){//TODO napraviti korak 42
        
    }
}
