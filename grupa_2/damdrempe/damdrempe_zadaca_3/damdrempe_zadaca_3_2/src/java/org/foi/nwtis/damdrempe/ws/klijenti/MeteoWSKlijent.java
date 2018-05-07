package org.foi.nwtis.damdrempe.ws.klijenti;

public class MeteoWSKlijent {

    public static java.util.List<org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste> dajSvaParkiralista() {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSvaParkiralista();
    }    

    public static Boolean dodajParkiraliste(org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste arg0) {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dodajParkiraliste(arg0);
    }
    
    public static java.util.List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> dajSveMeteoPodatke(int arg0, long arg1, long arg2) {
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatke(arg0, arg1, arg2);
    }
}
