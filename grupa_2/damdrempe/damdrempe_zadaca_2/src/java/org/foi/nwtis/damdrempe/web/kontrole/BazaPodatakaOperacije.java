package org.foi.nwtis.damdrempe.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;
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
    public void ZatvoriVezu() throws SQLException{
        veza.close();
    }
    
    /**
     * Ubacuje novi uređaj ovisno o komandi u tablicu uredaji.
     * @param komanda
     * @param sadrzaj
     * @return true ako je uspješno ubačen uređaj, false ako uređaj s tim id već postoji
     * @throws SQLException 
     */
    public boolean UredajiInsert(Komanda komanda, String sadrzaj) throws SQLException{  
        if(UredajiSelectId(komanda.id)){
            return false;
        }            
        
        String upitUredajInsert = "INSERT INTO uredaji (id,naziv,sadrzaj,vrijeme_kreiranja) values(?,?,?,?)";
        Timestamp trenutnoVrijeme = new Timestamp(System.currentTimeMillis());
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitUredajInsert);
        preparedStmt.setInt(1, komanda.id);
        preparedStmt.setString(2, komanda.naziv);
        preparedStmt.setString(3, sadrzaj);
        preparedStmt.setTimestamp(4, trenutnoVrijeme);
        preparedStmt.execute();
        
        return true;
    }
    
    /**
     * Ažurira uređaj u tablici uredaji prema traženom id.
     * @param komanda
     * @param sadrzaj
     * @return true ako je ažurirano, false ako ne postoji uređaj s traženim id
     * @throws SQLException 
     */
    public boolean UredajiUpdate(Komanda komanda, String sadrzaj) throws SQLException{  
        if(!UredajiSelectId(komanda.id)){
            return false;
        }            
        
        String upitInsert = "UPDATE uredaji SET naziv=?, sadrzaj=?, vrijeme_promjene=? WHERE id = ?";
        Timestamp trenutnoVrijeme = new Timestamp(System.currentTimeMillis());
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitInsert);
        preparedStmt.setString(1, komanda.naziv);
        preparedStmt.setString(2, sadrzaj);
        preparedStmt.setTimestamp(3, trenutnoVrijeme);
        preparedStmt.setInt(4, komanda.id);
        preparedStmt.execute();
        
        return true;
    }
    
    /**
     * Provjerava da li uređaj s traženim id postoji
     * @param id
     * @return true ako postoji, inače false
     * @throws SQLException 
     */
    public boolean UredajiSelectId(int id) throws SQLException{
        String upitSelect = "SELECT * FROM uredaji WHERE id = ?";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        
        return rs.next();
    }
    
    /**
     * Dohvaća broj zapisa za traženi vremenski period u tablici dnevnik.
     * @param vrijemeOd
     * @param vrijemeDo
     * @return
     * @throws SQLException 
     */
    public int DnevnikSelectCount(Timestamp vrijemeOd, Timestamp vrijemeDo) throws SQLException{
        String upitSelect = "SELECT COUNT(*) AS ukupno FROM dnevnik WHERE vrijeme>=? AND vrijeme<=?";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setTimestamp(1, vrijemeOd);
        preparedStmt.setTimestamp(2, vrijemeDo);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        rs.next();
        int ukupno = rs.getInt("ukupno");
        
        return ukupno;        
    }
    
    /**
     * Vraća zapise za određeni vremenski period iz tablice dnevnik.
     * @param vrijemeOd
     * @param vrijemeDo
     * @param pomak
     * @param brojZapisa
     * @return popis zapisa
     * @throws SQLException 
     */
    public List<Dnevnik> DnevnikSelectPeriod(Timestamp vrijemeOd, Timestamp vrijemeDo, int pomak, int brojZapisa) 
            throws SQLException{        
        List<Dnevnik> listaZapisa = new ArrayList<>();
        int offset = pomak * brojZapisa;        
        String upitSelect = "SELECT * FROM dnevnik WHERE vrijeme>=? AND vrijeme<=? ORDER BY vrijeme DESC LIMIT ? OFFSET ?";        
        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setTimestamp(1, vrijemeOd);
        preparedStmt.setTimestamp(2, vrijemeDo);
        preparedStmt.setInt(3, brojZapisa);
        preparedStmt.setInt(4, offset);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String sadrzaj = rs.getString("sadrzaj");
            Date vrijemeZapisa = rs.getTimestamp("vrijeme");            
            Dnevnik dnevnik = new Dnevnik(id, sadrzaj, vrijemeZapisa);            
            listaZapisa.add(dnevnik);
        }
        rs.close();
        preparedStmt.close();
        
        return listaZapisa;
    }
    
    /**
     * Ubacuje u tablicu dnevnik novi zapis.
     * @param sadrzaj
     * @param id
     * @throws SQLException 
     */
    public void DnevnikInsert(String sadrzaj, int id) throws SQLException{       
        String upitDnevnikInsert = "INSERT INTO dnevnik (id,sadrzaj,vrijeme) values(?,?,?)";
        Timestamp trenutnoVrijeme = new Timestamp(System.currentTimeMillis());
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitDnevnikInsert);
        preparedStmt.setInt(1, id);
        preparedStmt.setString(2, sadrzaj);
        preparedStmt.setTimestamp(3, trenutnoVrijeme);
        preparedStmt.execute();
    }  
}
