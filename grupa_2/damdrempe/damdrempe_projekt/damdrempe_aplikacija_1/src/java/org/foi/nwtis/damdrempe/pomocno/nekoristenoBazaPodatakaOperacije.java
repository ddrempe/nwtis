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
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 * Klasa za sve operacije nad bazom podataka.
 * @author ddrempetic
 */
public class nekoristenoBazaPodatakaOperacije {
    
    private String url;
    private String korisnik;
    private String lozinka; 
    private BP_Konfiguracija bpk;
    private Connection veza;
    private String klasaDrivera;

    /**
     * Konstruktor klase koji prima kontekst servleta.
     * Učitava potrebne postavke iz konfiguracije i otvara vezu nad bazom.
     * Registrira driver.
     * @param sc kontekst servleta
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public nekoristenoBazaPodatakaOperacije(ServletContext sc) throws SQLException, ClassNotFoundException {
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
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public nekoristenoBazaPodatakaOperacije() throws SQLException, ClassNotFoundException {
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
     * @throws SQLException 
     */
    public void zatvoriVezu() throws SQLException{
        veza.close();
    } 
    
    
    
    
    
    /**
     * Briše meteopodatke za pojedino parkiralište.
     * @param idParkiralista identifikator parkirališta, vanjski ključ
     * @return false ako parkiralište ne postoji, inače true
     * @throws SQLException
     */
    public boolean meteoDelete(int idParkiralista) throws SQLException{
//        if(!meteoSelectIdParkiralista(idParkiralista)){
//            return false;
//        }   
        
        String upit = "DELETE FROM meteo WHERE id=?";        
        
        PreparedStatement preparedStmt = veza.prepareStatement(upit);        
        preparedStmt.setInt(1, idParkiralista);
        preparedStmt.execute(); 
        
        return true;
    }
    
    /**
     * Dohvaća minimalnu i maksimalnu temeperaturu za pojedino parkiralište u zadanom intervalu.
     * @param id identifikator parkirališta, vanjski ključ
     * @param odVrijeme početno vrijeme intervala
     * @param doVrijeme završno vrijeme intervala
     * @return listu od dva elementa tipa float koji predstavljaju minimalnu i maksimalnu temperaturu
     * @throws SQLException
     */
    public ArrayList<Float> meteoPodaciSelectMinMax(int id, long odVrijeme, long doVrijeme) throws SQLException {
        String upit = "SELECT MIN(TEMPMIN) AS mintemp, MAX(TEMPMAX) AS maxtemp FROM METEO WHERE ID =? AND preuzeto>=? and preuzeto<=?";
        
        Timestamp odTimestamp = new Timestamp(odVrijeme);
        Timestamp doTimestamp = new Timestamp(doVrijeme);
        
        PreparedStatement preparedStmt = veza.prepareStatement(upit);
        preparedStmt.setInt(1, id);
        preparedStmt.setTimestamp(2, odTimestamp);
        preparedStmt.setTimestamp(3, doTimestamp);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();
            
        ArrayList<Float> tempMinMax = new ArrayList<>();
        tempMinMax.add(rs.getFloat("mintemp"));
        tempMinMax.add(rs.getFloat("maxtemp"));

        return tempMinMax;
    }
}
