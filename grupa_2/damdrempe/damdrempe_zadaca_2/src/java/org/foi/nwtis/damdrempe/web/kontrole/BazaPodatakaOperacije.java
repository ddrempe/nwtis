/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
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

    public BazaPodatakaOperacije() throws SQLException, ClassNotFoundException {
        ServletContext sc = SlusacAplikacije.servletContext;
        BP_Konfiguracija bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        this.bpk = bpk;
        this.url = bpk.getServerDatabase() + bpk.getUserDatabase();
        this.korisnik = bpk.getAdminUsername();
        this.lozinka = bpk.getAdminPassword();
        
        Class.forName("com.mysql.jdbc.Driver"); //TODO vidjeti sto s tim
        veza = DriverManager.getConnection(url, korisnik, lozinka);
    }
    
    public void ZatvoriVezu() throws SQLException{
        veza.close();
    }
    
    public void UredajiInsert(Komanda komanda, String sadrzaj) throws SQLException, ParseException{  
        //TODO provjeri postoji i vrati boolean
        
        String upitUredajInsert = "INSERT INTO uredaji (id,naziv,sadrzaj,vrijeme_kreiranja) values(?,?,?,?)";
        Timestamp trenutnoVrijeme = new Timestamp(System.currentTimeMillis());
        
        PreparedStatement preparedStmt = veza.prepareStatement(upitUredajInsert);
        preparedStmt.setInt(1, komanda.id);
        preparedStmt.setString(2, komanda.naziv);
        preparedStmt.setString(3, sadrzaj);
        preparedStmt.setTimestamp(4, trenutnoVrijeme); //TODO ne string nego timestamp
        preparedStmt.execute();
    }
        
    public Date ParsirajDatum(String vrijemeTekst) throws ParseException{//TODO ne koristi se 
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        Date vrijeme =  df.parse(vrijemeTekst); 
        
        return vrijeme;        
    }   
}
