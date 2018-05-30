package org.foi.nwtis.damdrempe.ws.serveri;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.pomocno.Korisnik;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.podaci.Dnevnik;
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

    @Resource
    private WebServiceContext context;

    /**
     * Poziva upit nad bazom podataka. Dohvaća sve meteopodatke za parkiralište
     * u zadanom vremenskom intervalu.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param odVrijeme početak vremenskog intervala
     * @param doVrijeme kraj vremenskog intervala
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return listu meteopodataka iz rezultata upita
     */
    public List<MeteoPodaci> dajMeteoPodatkeZaPeriodLong(int id, long odVrijeme, long doVrijeme, String korisnickoIme, String lozinka) {
        Korisnik korisnik = new Korisnik(korisnickoIme, lozinka);
        if (autentificirajIZapisiUDnevnik(korisnik, "dajMeteoPodatkeZaPeriodLong") == false) {
            return null;
        }
        
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
     * Poziva upit nad bazom podataka. Dohvaća sve meteopodatke za parkiralište
     * u zadanom vremenskom intervalu.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param odString početak vremenskog intervala
     * @param doString kraj vremenskog intervala
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return listu meteopodataka iz rezultata upita
     */
    public List<MeteoPodaci> dajMeteoPodatkeZaPeriodTimestamp(int id, String odString, String doString, String korisnickoIme, String lozinka) {
        Korisnik korisnik = new Korisnik(korisnickoIme, lozinka);
        if (autentificirajIZapisiUDnevnik(korisnik, "dajMeteoPodatkeZaPeriodTimestamp") == false) {
            return null;
        }

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
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return objekt meteopodataka
     */
    public MeteoPodaci dajZadnjeMeteoPodatke(int id, String korisnickoIme, String lozinka) {
        Korisnik korisnik = new Korisnik(korisnickoIme, lozinka);
        if (autentificirajIZapisiUDnevnik(korisnik, "dajZadnjeMeteoPodatke") == false) {
            return null;
        }

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
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return objekt meteopodataka
     */
    public MeteoPodaci dajVazeceMeteoPodatke(int id, String korisnickoIme, String lozinka) {
        Korisnik korisnik = new Korisnik(korisnickoIme, lozinka);
        if (autentificirajIZapisiUDnevnik(korisnik, "dajVazeceMeteoPodatke") == false) {
            return null;
        }

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
     * parkiralište.
     *
     * @param id identifikator parkirališta za kojeg se dohvaćaju podaci
     * @param n broj meteopodataka
     * @param korisnickoIme korisničko ime za autentifikaciju
     * @param lozinka lozinka za autentifikaciju
     * @return listu meteopodataka iz rezultata upita
     */
    public List<MeteoPodaci> dajZadnjihNMeteopodataka(int id, int n, String korisnickoIme, String lozinka) {
        List<MeteoPodaci> meteopodaci = null;

        Korisnik korisnik = new Korisnik(korisnickoIme, lozinka);
        if (autentificirajIZapisiUDnevnik(korisnik, "dajZadnjihNMeteopodataka") == false) {
            return null;
        }

        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            meteopodaci = bpo.meteoSelectIdMeteoN(id, n);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return meteopodaci;
    }

    /**
     * Provjerava da li korisnik postoji u bazi podataka i zapisuje poziv metode servisa u dnevnik.
     * @param korisnik korisnički podaci za autentifikaciju
     * @param akcija naziv metode koja se poziva
     * @return 
     */
    private boolean autentificirajIZapisiUDnevnik(Korisnik korisnik, String akcija) {
        boolean rezultat;
        HttpServletRequest hsr = (HttpServletRequest) context.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        String adresaZahtjeva = hsr.getRequestURL().toString() + "/" + akcija;
        String ipAdresa = "";
        try {
            ipAdresa = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }

        rezultat = PomocnaKlasa.autentificirajKorisnika(korisnik);
        int status = rezultat ? 1 : 0;
        Dnevnik dnevnik = new Dnevnik(korisnik.getKorisnickoIme(), adresaZahtjeva, ipAdresa, 0, status);
        PomocnaKlasa.zapisiUDnevnik(dnevnik);

        return rezultat;
    }
}
