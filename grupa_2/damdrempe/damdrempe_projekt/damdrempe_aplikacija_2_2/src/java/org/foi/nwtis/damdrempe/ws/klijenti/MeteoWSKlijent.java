package org.foi.nwtis.damdrempe.ws.klijenti;

/**
 * Klijent za pozivanje operacija SOAP web servisa za meteorološke podatke iz
 * aplikacije 1.
 *
 * @author ddrempetic
 */
public class MeteoWSKlijent {

    /**
     * Dohvaća vazece meteopodatke preko OWM web servisa.
     *
     * @param arg0 identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param arg1 korisničko ime za autentifikaciju
     * @param arg2 lozinka za autentifikaciju
     * @return objekt meteopodataka
     */
    public static MeteoPodaci dajVazeceMeteoPodatke(int arg0, java.lang.String arg1, java.lang.String arg2) {
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVazeceMeteoPodatke(arg0, arg1, arg2);
    }

    /**
     * Dohvaća zadnje meteopodatke iz baze podataka.
     *
     * @param arg0 identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param arg1 korisničko ime za autentifikaciju
     * @param arg2 lozinka za autentifikaciju
     * @return objekt meteopodataka
     */
    public static MeteoPodaci dajZadnjeMeteoPodatke(int arg0, java.lang.String arg1, java.lang.String arg2) {
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajZadnjeMeteoPodatke(arg0, arg1, arg2);
    }
}
