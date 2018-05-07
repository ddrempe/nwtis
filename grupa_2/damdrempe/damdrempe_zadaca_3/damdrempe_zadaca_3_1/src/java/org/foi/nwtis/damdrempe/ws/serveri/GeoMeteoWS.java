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
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
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
    public List<Parkiraliste> dajSvaParkiralista() {
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
    
    /**
     * Web service operation
     * @param parkiraliste
     * @return 
     */
    @WebMethod(operationName = "dodajParkiraliste")
    public Boolean dodajParkiraliste(Parkiraliste parkiraliste) {
        String naziv = parkiraliste.getNaziv();
        String adresa = parkiraliste.getAdresa();

        boolean rezultat = false;
        
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
    
    public List<MeteoPodaci> dajSveMeteoPodatke(int id, long odVrijeme, long doVrijeme){
        List<MeteoPodaci> sviMeteopodaci = null;
        
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviMeteopodaci = bpo.meteoPodaciSelect(id, odVrijeme, doVrijeme);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sviMeteopodaci;
    }
    
    public MeteoPodaci dajZadnjeMeteoPodatke(int id){
        MeteoPodaci meteopodaci = null;        
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            meteopodaci = bpo.meteoPodaciSelectLast(id);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return meteopodaci;        
    }
    
    public MeteoPodaci dajVazeceMeteoPodatke(int id){       
        Parkiraliste parkiraliste = new Parkiraliste();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            parkiraliste = bpo.parkiralistaSelectIdVrati(id);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MeteoPodaci meteo = null;
        
        try {
            PomocnaKlasa.dohvatiOWMMeteo(parkiraliste.getGeoloc().getLatitude(), parkiraliste.getGeoloc().getLongitude());            
        } catch (Exception ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }                
        
        return meteo;
    }
    
    public List<Float> dajMinMaxTemp(int id, long odVrijeme, long doVrijeme){
        List<Float> rezultat = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            rezultat = bpo.meteoPodaciSelectMinMax(id, odVrijeme, doVrijeme);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Float nula = new Float(0);
        if(rezultat.get(0).equals(nula) && rezultat.get(1).equals(nula)){
            return null;
        }

        return rezultat;
    }
}
