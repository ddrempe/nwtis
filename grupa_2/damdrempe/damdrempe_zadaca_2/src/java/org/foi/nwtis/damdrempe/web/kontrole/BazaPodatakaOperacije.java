/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
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

    public BazaPodatakaOperacije() throws SQLException, ClassNotFoundException {
        ServletContext sc = SlusacAplikacije.servletContext;
        this.bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.url = bpk.getServerDatabase() + bpk.getUserDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();
        this.klasaDrivera = bpk.getDriverDatabase();
        
        Class.forName(klasaDrivera); //TODO vidjeti da li je u redu dodana mysql biblioteka
        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }
    
    public void ZatvoriVezu() throws SQLException{
        veza.close();
    }
    
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
    
    public boolean UredajiSelectId(int id) throws SQLException{
        String upitSelect = "SELECT * FROM uredaji WHERE id = ?";
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitSelect);
        preparedStmt.setInt(1, id);
        preparedStmt.execute();
        ResultSet rs = preparedStmt.getResultSet();
        
        return rs.next();
    }
    
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
    
    public void DnevnikInsert(String sadrzaj) throws SQLException{       
        String upitDnevnikInsert = "INSERT INTO dnevnik (sadrzaj,vrijeme) values(?,?)";
        Timestamp trenutnoVrijeme = new Timestamp(System.currentTimeMillis());
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitDnevnikInsert);
        preparedStmt.setString(1, sadrzaj);
        preparedStmt.setTimestamp(2, trenutnoVrijeme);
        preparedStmt.execute();
    }
        
    public Date ParsirajDatum(String vrijemeTekst) throws ParseException{//TODO ne koristi se 
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        Date vrijeme =  df.parse(vrijemeTekst); 
        
        return vrijeme;        
    }   
}
