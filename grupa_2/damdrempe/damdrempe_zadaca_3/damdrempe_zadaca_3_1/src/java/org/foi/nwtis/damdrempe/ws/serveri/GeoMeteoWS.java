/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ws.serveri;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.damdrempe.PomocnaKlasa;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.web.BazaPodatakaOperacije;
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
     * @return 
     */
    @WebMethod(operationName = "dajSvaParkiralista")
    public java.util.List<Parkiraliste> dajSvaParkiralista() {
        List<Parkiraliste> svaParkiralista = new ArrayList<>();
        
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            svaParkiralista = bpo.parkiralistaSelect();
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return svaParkiralista;
    }

    //TODO vidjeti da li treba
//    /**
//     * Web service operation
//     * @param parkiraliste
//     */
//    @WebMethod(operationName = "dodajParkiraliste")
//    @Oneway
//    public void dodajParkiraliste(Parkiraliste parkiraliste) {
//        //TODO ne znam cemu sluzi        
//    }
    
    /**
     * Web service operation
     * @param naziv
     * @param adresa
     * @return 
     */
    @WebMethod(operationName = "dodajParkiraliste")
    public Boolean dodajUredjaj(@WebParam(name = "naziv") String naziv, @WebParam(name = "adresa") String adresa) {

        boolean rezultat = false;
        
        //TODO dohvati geolokaciju iz naziva i adrese
        Lokacija lokacija = PomocnaKlasa.dohvatiGMLokaciju(adresa);
        String latitude = lokacija.getLatitude();
        String longitude = lokacija.getLongitude();
        
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            rezultat = bpo.parkiralistaInsert(naziv, adresa, latitude, longitude);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rezultat;
    }   
}
