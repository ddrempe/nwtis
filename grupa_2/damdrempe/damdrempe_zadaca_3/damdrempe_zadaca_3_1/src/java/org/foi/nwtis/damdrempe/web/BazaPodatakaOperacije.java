package org.foi.nwtis.damdrempe.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 * Klasa za sve operacije nad bazom podataka.
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
     * Konstrktor klase.
     * Učitava potrebne postavke iz konfiguracije i otvara vezu nad bazom.
     * Registrira driver.
     * @param sc
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public BazaPodatakaOperacije(ServletContext sc) throws SQLException, ClassNotFoundException {
        this.bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.url = bpk.getServerDatabase() + bpk.getUserDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();
        this.klasaDrivera = bpk.getDriverDatabase();
        
        Class.forName(klasaDrivera);
        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }
    
    /**
     * Konstrktor klase.
     * Učitava potrebne postavke iz konfiguracije i otvara vezu nad bazom.
     * Registrira driver.
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public BazaPodatakaOperacije() throws SQLException, ClassNotFoundException {
        ServletContext sc = SlusacAplikacije.servletContext;
        this.bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.url = bpk.getServerDatabase() + bpk.getUserDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();
        this.klasaDrivera = bpk.getDriverDatabase();
        
        Class.forName(klasaDrivera);
        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }
    
    /**
     * Zatvara vezu nad bazom podataka.
     * @throws SQLException 
     */
    public void zatvoriVezu() throws SQLException{
        veza.close();
    } 
    
    //TODO moglo bi se umjesto 4 argumenta staviti objekt Parkiraliste
    public boolean parkiralistaInsert(String naziv, String adresa, String latitude, String longitude) throws SQLException{
        if(parkiralistaSelectNaziv(naziv)){
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
     * Provjerava da li parkiralište s traženim nazivom postoji
     * @param naziv
     * @return true ako postoji, inače false
     * @throws SQLException 
     */
    public boolean parkiralistaSelectNaziv(String naziv) throws SQLException{
        String upitSelect = "SELECT * FROM parkiralista WHERE naziv = ?";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setString(1, naziv);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        
        return rs.next();
    }
    
    /**
     * Provjerava da li parkiralište s traženim id postoji
     * @param id
     * @return true ako postoji, inače false
     * @throws SQLException 
     */
    public boolean parkiralistaSelectId(int id) throws SQLException{
        String upitSelect = "SELECT * FROM parkiralista WHERE id = ?";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        
        return rs.next();
    }
    
    public Parkiraliste parkiralistaSelectIdVrati(int id) throws SQLException{        
        String upit = "SELECT * FROM parkiralista WHERE id = ?";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet odgovor = preparedStmt.executeQuery();
        odgovor.next(); 
        Parkiraliste parkiraliste = new Parkiraliste();
        parkiraliste.setId(odgovor.getInt("ID"));
        parkiraliste.setNaziv(odgovor.getString("NAZIV"));
        Lokacija lokacija = new Lokacija();
        lokacija.setLatitude(odgovor.getString("LATITUDE"));
        lokacija.setLongitude(odgovor.getString("LONGITUDE"));
        parkiraliste.setGeoloc(lokacija);
        parkiraliste.setAdresa(odgovor.getString("ADRESA"));
        
        return parkiraliste;
    }
    
    public ArrayList<Parkiraliste> parkiralistaSelect() throws SQLException {
        ArrayList<Parkiraliste> dohvacenaParkiralista = new ArrayList<>();
        String upit = "SELECT * FROM PARKIRALISTA";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.execute();
        ResultSet odgovor = preparedStmt.executeQuery();
        while (odgovor.next()) {
            Parkiraliste p = new Parkiraliste();
            p.setId(odgovor.getInt("ID"));
            p.setNaziv(odgovor.getString("NAZIV"));
            Lokacija lokacija = new Lokacija();
            lokacija.setLatitude(odgovor.getString("LATITUDE"));
            lokacija.setLongitude(odgovor.getString("LONGITUDE"));
            p.setGeoloc(lokacija);
            p.setAdresa(odgovor.getString("ADRESA"));
            
            dohvacenaParkiralista.add(p);
        }     

        return dohvacenaParkiralista;
    }
    
    public boolean parkiralistaUpdate(Parkiraliste parkiraliste) throws SQLException{
        if(!parkiralistaSelectId(parkiraliste.getId())){
            return false;
        }   
        
        String upit = "UPDATE parkiralista SET naziv=?, adresa=?, latitude=?, longitude=? WHERE id=?";        
        
        PreparedStatement preparedStmt = veza.prepareStatement(upit);        

        preparedStmt.setString(1, parkiraliste.getNaziv());
        preparedStmt.setString(2, parkiraliste.getAdresa());
        preparedStmt.setString(3, parkiraliste.getGeoloc().getLatitude());
        preparedStmt.setString(4, parkiraliste.getGeoloc().getLongitude());
        preparedStmt.setInt(5, parkiraliste.getId());

        preparedStmt.execute(); 
        
        return true;
    }
    
    public boolean parkiralistaDelete(int id) throws SQLException{
        if(!parkiralistaSelectId(id)){
            return false;
        }   
        
        meteoDelete(id);
        
        String upit = "DELETE FROM parkiralista WHERE id=?";        
        
        PreparedStatement preparedStmt = veza.prepareStatement(upit);        
        preparedStmt.setInt(1, id);
        preparedStmt.execute(); 
        
        return true;
    }
    
    /**
     * Provjerava da li meteopodaci s traženim id parkiralista postoje
     * @param id
     * @return true ako postoji, inače false
     * @throws SQLException 
     */
    public boolean meteoSelectIdParkiralista(int id) throws SQLException{
        String upitSelect = "SELECT * FROM meteo WHERE id = ?";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        
        return rs.next();
    }
    
    public void meteoInsert(MeteoPodaci meteoPodaci, Parkiraliste parkiraliste) throws SQLException{
        String upit = "INSERT INTO METEO (ID, LATITUDE, LONGITUDE, VRIJEME, VRIJEMEOPIS, TEMP, TEMPMIN, TEMPMAX, VLAGA, TLAK, VJETAR, VJETARSMJER)" + 
                              " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
    
    public boolean meteoDelete(int idParkiralista) throws SQLException{
        if(!meteoSelectIdParkiralista(idParkiralista)){
            return false;
        }   
        
        String upit = "DELETE FROM meteo WHERE id=?";        
        
        PreparedStatement preparedStmt = veza.prepareStatement(upit);        
        preparedStmt.setInt(1, idParkiralista);
        preparedStmt.execute(); 
        
        return true;
    }
}
