package org.foi.nwtis.damdrempe.ws.serveri;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

/**
 * SOAP web servis za pozivanje operacija nad parkiralištima i meteopodacima u
 * bazi podataka.
 *
 * @author ddrempetic
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Poziva upit nad bazom podataka. 
     * Dohvaća sve meteopodatke za parkiralište
     * u zadanom vremenskom intervalu.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param odVrijeme početak vremenskog intervala
     * @param doVrijeme kraj vremenskog intervala
     * @return listu meteopodataka iz rezultata upita
     */
    public List<MeteoPodaci> dajMeteoPodatkeZaPeriodLong(int id, long odVrijeme, long doVrijeme) {
        List<MeteoPodaci> sviMeteopodaci = null;

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviMeteopodaci = bpo.meteoSelectIdMeteoPeriod(id, odVrijeme, doVrijeme);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sviMeteopodaci;
    }

    /**
     * Poziva upit nad bazom podataka. 
     * Dohvaća sve meteopodatke za parkiralište
     * u zadanom vremenskom intervalu.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param odString početak vremenskog intervala
     * @param doString kraj vremenskog intervala
     * @return listu meteopodataka iz rezultata upita
     */
    public List<MeteoPodaci> dajMeteoPodatkeZaPeriodTimestamp(int id, String odString, String doString) {
        List<MeteoPodaci> sviMeteopodaci = null;

        try {
            Date parsedDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            parsedDate = dateFormat.parse(odString);
            Timestamp odTimestamp = new java.sql.Timestamp(parsedDate.getTime());
            parsedDate = dateFormat.parse(doString);
            Timestamp doTimestamp = new java.sql.Timestamp(parsedDate.getTime());
            
            long odVrijeme = odTimestamp.getTime();
            long doVrijeme = doTimestamp.getTime();
            
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            sviMeteopodaci = bpo.meteoSelectIdMeteoPeriod(id, odVrijeme, doVrijeme);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | ParseException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sviMeteopodaci;
    }

    /**
     * Poziva upit nad bazom podataka. Uzima najsvježije meteopodatke iz baze
     * podataka.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @return objekt meteopodataka
     */
    public MeteoPodaci dajZadnjeMeteoPodatke(int id) {
        MeteoPodaci meteopodaci = null;

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            meteopodaci = bpo.meteoPodaciSelectIdZadnjiMeteo(id);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meteopodaci;
    }

    /**
     * Poziva upit nad bazom podataka kako bi se dohvatila adresa lokacija
     * parkirališta. Za dohvaćenu adresu parkirališta dohvaća trenutne
     * meteopodatke sa web servisa.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @return objekt meteopodataka
     */
    public MeteoPodaci dajVazeceMeteoPodatke(int id) {
        Parkiraliste parkiraliste = new Parkiraliste();

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            parkiraliste = bpo.parkiralistaSelectIdJednoParkiraliste(id);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        MeteoPodaci meteo = null;

        try {
            meteo = PomocnaKlasa.dohvatiOWMMeteo(parkiraliste.getGeoloc().getLatitude(), parkiraliste.getGeoloc().getLongitude());
        } catch (Exception ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meteo;
    }

    /**
     * Poziva upit nad bazom podataka. Dohvaća zadnjih n meteopodataka za
     * prkiralište.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param n broj meteopodataka
     * @return listu meteopodataka iz rezultata upita
     */
    public List<MeteoPodaci> dajZadnjihNMeteopodataka(int id, int n) {
        List<MeteoPodaci> meteopodaci = null;

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            meteopodaci = bpo.meteoSelectIdMeteoN(id, n);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meteopodaci;
    }

}
