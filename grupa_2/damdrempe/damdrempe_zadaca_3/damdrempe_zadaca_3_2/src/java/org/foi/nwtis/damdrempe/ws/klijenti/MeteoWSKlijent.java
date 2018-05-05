package org.foi.nwtis.damdrempe.ws.klijenti;

public class MeteoWSKlijent {

    public static java.util.List<org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste> dajSvaParkiralista() {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSvaParkiralista();
    }

    public static Boolean dodajParkiraliste(java.lang.String naziv, java.lang.String adresa) {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dodajParkiraliste(naziv, adresa);
    }   
}
