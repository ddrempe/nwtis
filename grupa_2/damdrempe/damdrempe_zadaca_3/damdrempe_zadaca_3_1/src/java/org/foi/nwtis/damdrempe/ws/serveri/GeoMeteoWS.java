package org.foi.nwtis.damdrempe.ws.serveri;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import org.foi.nwtis.damdrempe.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

/**
 * SOAP web servis za pozivanje operacija nad parkiralištima i meteopodacima u bazi podataka.
 * @author ddrempetic
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Poziva upit nad bazom podataka.
     * @return listu svih parkirališta iz baze podataka
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
     * Poziva upit nad bazom podataka.
     * Dodaje parkiralište.
     * @param parkiraliste
     * @return true ako je parkiralište dodano, inače false
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
    
    /**
     * Poziva upit nad bazom podataka.
     * Dohvaća sve meteopodatke za parkiralište u zadanom vremenskom intervalu.
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param odVrijeme početak vremenskog intervala
     * @param doVrijeme kraj vremenskog intervala
     * @return listu meteopodataka iz rezultata upita
     */
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
    
    /**
     * Poziva upit nad bazom podataka.
     * Uzima najsvježije meteopodatke iz baze podataka.
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @return objekt meteopodataka
     */
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
    
    /**
     * Poziva upit nad bazom podataka.
     * Za dohvaćenu adresu parkirališta dohvaća trenutne meteopodatke sa web servisa.
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @return objekt meteopodataka
     */
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
    
    /**
     * Poziva upit nad bazom podataka.
     * Dohvaća minimalnu i maksimalnu temperaturu za parkiralište.
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param odVrijeme početak vremenskog intervala
     * @param doVrijeme kraj vremenskog intervala
     * @return listu od dva objekta tipa float koji predstavljaju minimalnu i maksimalnu temepraturu
     */
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
