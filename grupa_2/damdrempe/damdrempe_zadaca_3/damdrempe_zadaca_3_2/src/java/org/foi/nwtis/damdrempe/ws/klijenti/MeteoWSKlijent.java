package org.foi.nwtis.damdrempe.ws.klijenti;

/**
 * Klijent za poziv potrebnih operacija SOAP web servisa.
 * @author ddrempetic
 */
public class MeteoWSKlijent {

    /**
     * Poziva operaciju za dohvat svih parkirališta.
     * @return listu parkirališta
     */
    public static java.util.List<org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste> dajSvaParkiralista() {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSvaParkiralista();
    }    

    /**
     * Poziva operaciju za dodavanje novog parkirališta.
     * @param arg0 objekt parkirališta
     * @return true ako je uspješno dodano, inače false
     */
    public static Boolean dodajParkiraliste(org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste arg0) {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dodajParkiraliste(arg0);
    }
    
    /**
     * Poziva operaciju za dohvat meteopodataka za parkiralište u odabranom vremenskom intervalu.
     * @param arg0 id parkirališta
     * @param arg1 vrijeme početka intervala u long formatu
     * @param arg2 vrijeme završetka intervala u long formatu
     * @return listu svih meteopodataka koji su dohvaćeni
     */
    public static java.util.List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> dajSveMeteoPodatke(int arg0, long arg1, long arg2) {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatke(arg0, arg1, arg2);
    }
}
