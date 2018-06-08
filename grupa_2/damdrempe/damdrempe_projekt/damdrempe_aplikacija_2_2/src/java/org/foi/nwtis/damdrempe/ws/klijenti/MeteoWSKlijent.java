/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ws.klijenti;

/**
 *
 * @author ddrempetic
 */
public class MeteoWSKlijent {

    public static MeteoPodaci dajVazeceMeteoPodatke(int arg0, java.lang.String arg1, java.lang.String arg2) {
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVazeceMeteoPodatke(arg0, arg1, arg2);
    }

    public static MeteoPodaci dajZadnjeMeteoPodatke(int arg0, java.lang.String arg1, java.lang.String arg2) {
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajZadnjeMeteoPodatke(arg0, arg1, arg2);
    }   
}
