package org.foi.nwtis.damdrempe.pomocno;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.damdrempe.web.podaci.Dnevnik;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 * Klasa za sve operacije nad bazom podataka.
 *
 * @author ddrempetic
 */
public class BazaPodatakaOperacije {

    private String url;
    private String korisnik;
    private String lozinka;
    private BP_Konfiguracija bpk;
    private Connection veza;
    private String klasaDrivera;

    /**
     * Konstruktor klase koji prima kontekst servleta. Učitava potrebne postavke
     * iz konfiguracije i otvara vezu nad bazom. Registrira driver.
     *
     * @param sc kontekst servleta
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public BazaPodatakaOperacije(ServletContext sc) throws SQLException, ClassNotFoundException {
        this.bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.url = bpk.getServerDatabase() + bpk.getAdminDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();
        this.klasaDrivera = bpk.getDriverDatabase();

        Class.forName(klasaDrivera);
        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }

    /**
     * Konstruktor klase koji čita kontekst servleta iz slušača aplikacije.
     * Učitava potrebne postavke iz konfiguracije i otvara vezu nad bazom.
     * Registrira driver.
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public BazaPodatakaOperacije() throws SQLException, ClassNotFoundException {
        ServletContext sc = SlusacAplikacije.servletContext;
        this.bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.url = bpk.getServerDatabase() + bpk.getAdminDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();
        this.klasaDrivera = bpk.getDriverDatabase();

        Class.forName(klasaDrivera);
        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }

    /**
     * Zatvara vezu nad bazom podataka.
     *
     * @throws SQLException
     */
    public void zatvoriVezu() throws SQLException {
        veza.close();
    }

    /**
     * Vraća sva parkirališta.
     *
     * @return listu svih parkirališta
     * @throws SQLException
     */
    public ArrayList<Parkiraliste> parkiralistaSelectSvaParkiralista() throws SQLException {
        ArrayList<Parkiraliste> dohvacenaParkiralista = new ArrayList<>();
        String upit = "SELECT * FROM PARKIRALISTA";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            Parkiraliste p = new Parkiraliste();
            p.setId(rs.getInt("ID"));
            p.setNaziv(rs.getString("NAZIV"));
            Lokacija lokacija = new Lokacija();
            lokacija.setLatitude(rs.getString("LATITUDE"));
            lokacija.setLongitude(rs.getString("LONGITUDE"));
            p.setGeoloc(lokacija);
            p.setAdresa(rs.getString("ADRESA"));
            p.setKapacitet(rs.getInt("KAPACITET"));
            p.setStatus(rs.getString("STATUS"));

            dohvacenaParkiralista.add(p);
        }

        return dohvacenaParkiralista;
    }

    /**
     * Vraća parkiralište s traženim id.
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Parkiraliste parkiralistaSelectIdJednoParkiraliste(int id) throws SQLException {
        String upit = "SELECT * FROM parkiralista WHERE id = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();

        Parkiraliste parkiraliste = new Parkiraliste();
        parkiraliste.setId(rs.getInt("ID"));
        parkiraliste.setNaziv(rs.getString("NAZIV"));

        Lokacija lokacija = new Lokacija();
        lokacija.setLatitude(rs.getString("LATITUDE"));
        lokacija.setLongitude(rs.getString("LONGITUDE"));
        parkiraliste.setGeoloc(lokacija);
        parkiraliste.setAdresa(rs.getString("ADRESA"));
        parkiraliste.setKapacitet(rs.getInt("KAPACITET"));
        parkiraliste.setStatus(rs.getString("STATUS"));

        return parkiraliste;
    }

    /**
     * Dodaje novo parkiralište.
     *
     * @param naziv
     * @param adresa
     * @param latitude
     * @param longitude
     * @return false ako parkiralište s tim nazivom već postoji, inače true
     * @throws SQLException
     */
    public boolean parkiralistaInsertOsnovno(String naziv, String adresa, String latitude, String longitude) throws SQLException {
        if (parkiralistaSelectNazivPostoji(naziv)) {
            return false;
        }

        String upit = "INSERT INTO parkiralista(naziv, adresa, latitude, longitude) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, naziv);
        preparedStmt.setString(2, adresa);
        preparedStmt.setString(3, latitude);
        preparedStmt.setString(4, longitude);
        preparedStmt.execute();

        return true;
    }

    /**
     * Dodaje novo parkiralište.
     *
     * @param parkiraliste objekt parkirališta
     * @return false ako parkiralište s tim nazivom već postoji, inače true
     * @throws SQLException
     */
    public boolean parkiralistaInsert(Parkiraliste parkiraliste) throws SQLException {
        if (parkiralistaSelectNazivPostoji(parkiraliste.getNaziv())) {
            return false;
        }

        String upit = "INSERT INTO parkiralista(naziv, adresa, latitude, longitude, kapacitet) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, parkiraliste.getNaziv());
        preparedStmt.setString(2, parkiraliste.getAdresa());
        preparedStmt.setString(3, parkiraliste.getGeoloc().getLatitude());
        preparedStmt.setString(4, parkiraliste.getGeoloc().getLongitude());
        preparedStmt.setInt(5, parkiraliste.getKapacitet());

        preparedStmt.execute();

        return true;
    }

    /**
     * Provjerava da li parkiralište s traženim nazivom postoji
     *
     * @param naziv
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean parkiralistaSelectNazivPostoji(String naziv) throws SQLException {
        String upitSelect = "SELECT * FROM parkiralista WHERE naziv = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, naziv);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Provjerava da li parkiralište s traženim id postoji
     *
     * @param id
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean parkiralistaSelectIdPostoji(int id) throws SQLException {
        String upitSelect = "SELECT * FROM parkiralista WHERE id = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Ažurira parkiralište s novim podacima.
     *
     * @param parkiraliste
     * @return false ako parkiralište ne postoji, inače true
     * @throws SQLException
     */
    public boolean parkiralistaUpdate(Parkiraliste parkiraliste) throws SQLException {
        if (!parkiralistaSelectIdPostoji(parkiraliste.getId())) {
            return false;
        }

        String upit = "UPDATE parkiralista SET naziv=?, adresa=?, latitude=?, longitude=?, kapacitet=?, status=? WHERE id=?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        preparedStmt.setString(1, parkiraliste.getNaziv());
        preparedStmt.setString(2, parkiraliste.getAdresa());
        preparedStmt.setString(3, parkiraliste.getGeoloc().getLatitude());
        preparedStmt.setString(4, parkiraliste.getGeoloc().getLongitude());
        preparedStmt.setInt(5, parkiraliste.getKapacitet());
        preparedStmt.setString(6, parkiraliste.getStatus());
        preparedStmt.setInt(7, parkiraliste.getId());

        preparedStmt.execute();

        return true;
    }

    /**
     * Briše parkiralište s traženim id.
     *
     * @param id
     * @return false ako parkiralište ne postoji, inače true
     * @throws SQLException
     */
    public boolean parkiralistaDelete(int id) throws SQLException {
        if (!parkiralistaSelectIdPostoji(id)) {
            return false;
        }

        String upit = "DELETE FROM parkiralista WHERE id=?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();

        return true;
    }

    /**
     * Provjerava da li meteopodaci s traženim id parkiralista postoje
     *
     * @param id identifikator parkirališta, vanjski ključ
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean meteoSelectIdParkiralistaPostoje(int id) throws SQLException {
        String upitSelect = "SELECT * FROM meteo WHERE id = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Dodaje nove metopodatke za određeno parkiralište.
     *
     * @param meteoPodaci objekt meteopodataka
     * @param parkiraliste objekt parkirališta
     * @throws SQLException
     */
    public void meteoInsert(MeteoPodaci meteoPodaci, Parkiraliste parkiraliste) throws SQLException {
        String upit = "INSERT INTO METEO (ID, LATITUDE, LONGITUDE, VRIJEME, VRIJEMEOPIS, TEMP, TEMPMIN, TEMPMAX, VLAGA, TLAK, VJETAR, VJETARSMJER)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        String vrijeme = String.valueOf(meteoPodaci.getWeatherNumber());
        vrijeme = vrijeme.substring(0, Math.min(vrijeme.length(), 24));
        String vrijemeOpis = meteoPodaci.getWeatherValue();
        vrijemeOpis = vrijemeOpis.substring(0, Math.min(vrijemeOpis.length(), 24));

        preparedStmt.setInt(1, parkiraliste.getId());
        preparedStmt.setString(2, parkiraliste.getGeoloc().getLatitude());
        preparedStmt.setString(3, parkiraliste.getGeoloc().getLongitude());
        preparedStmt.setString(4, vrijeme);
        preparedStmt.setString(5, vrijemeOpis);
        preparedStmt.setDouble(6, meteoPodaci.getTemperatureValue());
        preparedStmt.setDouble(7, meteoPodaci.getTemperatureMin());
        preparedStmt.setDouble(8, meteoPodaci.getTemperatureMax());
        preparedStmt.setDouble(9, meteoPodaci.getHumidityValue());
        preparedStmt.setDouble(10, meteoPodaci.getPressureValue());
        preparedStmt.setDouble(11, meteoPodaci.getWindSpeedValue());
        preparedStmt.setDouble(12, meteoPodaci.getWindDirectionValue());

        preparedStmt.execute();
    }

    /**
     * Vraća meteopodatke za pojedino parkiralište u vremenskom intervalu.
     *
     * @param id identifikator parkirališta, vanjski ključ
     * @param odVrijeme početno vrijeme intervala
     * @param doVrijeme završno vrijeme intervala
     * @return listu meteopodataka
     * @throws SQLException
     */
    public ArrayList<MeteoPodaci> meteoSelectIdMeteoPeriod(int id, long odVrijeme, long doVrijeme) throws SQLException {
        ArrayList<MeteoPodaci> dohvaceniMeteopodaci = new ArrayList<>();
        Timestamp odTimestamp = new Timestamp(odVrijeme);
        Timestamp doTimestamp = new Timestamp(doVrijeme);

        String upit = "SELECT * FROM meteo WHERE id=? AND preuzeto>=? AND preuzeto<=?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setInt(1, id);
        preparedStmt.setTimestamp(2, odTimestamp);
        preparedStmt.setTimestamp(3, doTimestamp);
        preparedStmt.execute();

        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            MeteoPodaci meteo = procitajVrijednostiMeteopodataka(rs);
            dohvaceniMeteopodaci.add(meteo);
        }

        return dohvaceniMeteopodaci;
    }

    /**
     * Vraća n meteopodataka za pojedino parkiralište.
     *
     * @param id identifikator parkirališta, vanjski ključ
     * @param n broj meteopodataka
     * @return listu meteopodataka
     * @throws SQLException
     */
    public ArrayList<MeteoPodaci> meteoSelectIdMeteoN(int id, int n) throws SQLException {
        ArrayList<MeteoPodaci> dohvaceniMeteopodaci = new ArrayList<>();

        String upit = "SELECT * FROM meteo WHERE id=? ORDER BY preuzeto DESC LIMIT ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setInt(1, id);
        preparedStmt.setInt(2, n);
        preparedStmt.execute();

        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            MeteoPodaci meteo = procitajVrijednostiMeteopodataka(rs);
            dohvaceniMeteopodaci.add(meteo);
        }

        return dohvaceniMeteopodaci;
    }

    /**
     * Vraća zadnje meteopodatke za traženo parkiralište.
     *
     * @param id identifikator parkirališta, vanjski ključ
     * @return objekt meteopodataka
     * @throws SQLException
     */
    public MeteoPodaci meteoPodaciSelectIdZadnjiMeteo(int id) throws SQLException {
        String upit = "SELECT * FROM meteo WHERE id=? ORDER BY preuzeto DESC";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();

        return procitajVrijednostiMeteopodataka(rs);
    }

    /**
     * Pomoćna metoda koja iz odgovora upita nad bazom podataka pročita sve
     * vrijednosti i dodijeli u objekt meteopodataka.
     *
     * @param rs rezultat upita nad bazom podataka
     * @return objekt meteopodataka
     * @throws SQLException
     */
    public MeteoPodaci procitajVrijednostiMeteopodataka(ResultSet rs) throws SQLException {
        MeteoPodaci meteo = new MeteoPodaci();
        meteo.setWeatherValue(rs.getString("VRIJEMEOPIS"));
        meteo.setTemperatureValue(rs.getFloat("TEMP"));
        meteo.setTemperatureMin(rs.getFloat("TEMPMIN"));
        meteo.setTemperatureMax(rs.getFloat("TEMPMAX"));
        meteo.setHumidityValue(rs.getFloat("VLAGA"));
        meteo.setPressureValue(rs.getFloat("TLAK"));
        meteo.setWindSpeedValue(rs.getFloat("VJETAR"));
        meteo.setLastUpdate(rs.getDate("PREUZETO"));
        //meteo.setWeatherNumber("VRIJEME");                        //ne postoji setter
        //meteo.setWindDirectionValue("VJETARSMJER");               //ne postoji setter

        return meteo;
    }

    /**
     * Dodaje novi zapis u dnevnik
     *
     * @param dnevnik podaci dnevnika
     * @throws SQLException
     */
    public void dnevnikInsert(Dnevnik dnevnik) throws SQLException {
        String upit = "INSERT INTO dnevnik (KORISNIK, URL, IPADRESA, TRAJANJE, STATUS)"
                + " VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        preparedStmt.setString(1, dnevnik.getKorisnik());
        preparedStmt.setString(2, dnevnik.getUrl());
        preparedStmt.setString(3, dnevnik.getIpAdresa());
        preparedStmt.setInt(4, dnevnik.getTrajanje());
        preparedStmt.setInt(5, dnevnik.getStatus());

        preparedStmt.execute();
    }

    /**
     * Provjerava da li korisnik postoji
     *
     * @param korisnik objekt korisnika
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean korisniciSelectKorisnikPostoji(KorisnikPodaci korisnik) throws SQLException {
        String upitSelect = "SELECT * FROM korisnici WHERE kor_ime = ? AND lozinka = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, korisnik.getKorisnickoIme());
        preparedStmt.setString(2, korisnik.getLozinka());
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Provjerava da li korisnik postoji
     *
     * @param ime
     * @param prezime
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean korisniciSelectImePrezimePostoji(String ime, String prezime) throws SQLException {
        String upitSelect = "SELECT * FROM korisnici WHERE ime = ? AND prezime = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, ime);
        preparedStmt.setString(2, prezime);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Provjerava da li korisnik postoji
     *
     * @param korisnickoIme
     * @return true ako postoji, inače false
     * @throws SQLException
     */
    public boolean korisniciSelectKorimePostoji(String korisnickoIme) throws SQLException {
        String upitSelect = "SELECT * FROM korisnici WHERE kor_ime = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, korisnickoIme);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();

        return rs.next();
    }

    /**
     * Vraća sve korisnike.
     *
     * @return listu svih korisnika
     * @throws SQLException
     */
    public ArrayList<Korisnik> korisniciSelectSviKorisnici() throws SQLException {
        ArrayList<Korisnik> dohvaceniKorisnici = new ArrayList<>();
        String upit = "SELECT * FROM KORISNICI";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        while (rs.next()) {
            Korisnik k = new Korisnik();
            k.setId(rs.getInt("ID"));
            k.setVrsta(rs.getInt("VRSTA"));
            k.setIme(rs.getString("IME"));
            k.setPrezime(rs.getString("PREZIME"));
            k.setKor_ime(rs.getString("KOR_IME"));
            k.setEmail_adresa(rs.getString("EMAIL_ADRESA"));
            k.setDatum_kreiranja(rs.getDate("DATUM_KREIRANJA"));
            k.setDatum_promjene(rs.getDate("DATUM_PROMJENE"));
            k.setLozinka("skriveno");
            dohvaceniKorisnici.add(k);
        }

        return dohvaceniKorisnici;
    }

    /**
     * Vraća jednog korisnika.
     *
     * @param korisnickoime
     * @return jednog korisnika
     * @throws SQLException
     */
    public Korisnik korisniciSelectKorimeKorisnik(String korisnickoime) throws SQLException {
        String upit = "SELECT * FROM KORISNICI WHERE kor_ime = ?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setString(1, korisnickoime);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();
        Korisnik k = new Korisnik();
        k.setId(rs.getInt("ID"));
        k.setVrsta(rs.getInt("VRSTA"));
        k.setIme(rs.getString("IME"));
        k.setPrezime(rs.getString("PREZIME"));
        k.setKor_ime(rs.getString("KOR_IME"));
        k.setEmail_adresa(rs.getString("EMAIL_ADRESA"));
        k.setDatum_kreiranja(rs.getDate("DATUM_KREIRANJA"));
        k.setDatum_promjene(rs.getDate("DATUM_PROMJENE"));
        k.setLozinka("skriveno");

        return k;
    }

    /**
     * Dodaje novi zapis za korisnike
     *
     * @param korisnik
     * @throws SQLException
     */
    public void korisniciInsert(Korisnik korisnik) throws SQLException {
        String upit = "INSERT INTO korisnici (kor_ime, ime, prezime, lozinka, email_adresa)"
                + " VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        preparedStmt.setString(1, korisnik.getKor_ime());
        preparedStmt.setString(2, korisnik.getIme());
        preparedStmt.setString(3, korisnik.getPrezime());
        preparedStmt.setString(4, korisnik.getLozinka());
        preparedStmt.setString(5, korisnik.getEmail_adresa());

        preparedStmt.execute();
    }

    /**
     * Ažurira korisnika s novim imenom i prezimenom.
     *
     * @param korisnickoIme
     * @param ime
     * @param prezime
     * @return false ako ne postoji, inače true
     * @throws SQLException
     */
    public boolean korisniciUpdatePrezimeIme(String korisnickoIme, String ime, String prezime) throws SQLException {
        String upit = "UPDATE korisnici SET ime=?, prezime=? WHERE kor_ime=?";

        PreparedStatement preparedStmt = veza.prepareStatement(upit);

        preparedStmt.setString(1, ime);
        preparedStmt.setString(2, prezime);
        preparedStmt.setString(3, korisnickoIme);

        preparedStmt.execute();

        return true;
    }

}
