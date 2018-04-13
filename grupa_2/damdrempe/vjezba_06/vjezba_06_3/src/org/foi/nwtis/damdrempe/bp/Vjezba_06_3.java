/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.bp;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author grupa_2
 */
public class Vjezba_06_3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ispisiPodatke(args[0]);
    }
    
    private static void ispisiPodatke(String datoteka){  
        try {  
            BP_Konfiguracija bpk = new BP_Konfiguracija(datoteka);
            
            String url = bpk.getServerDatabase() + bpk.getUserDatabase();
            String korisnik = bpk.getAdminUsername();
            String lozinka = bpk.getAdminPassword();
            String upit = "create table test_damdrempe("
                    + "kor_ime varchar(10) NOT NULL DEFAULT '', "
                    + "zapis varchar(250) NOT NULL DEFAULT ''"
                    + ")";      

            try (   //TODO obrati paznju na try with resources
                Connection con = DriverManager.getConnection(url, korisnik, lozinka);
                Statement stmt = con.createStatement();
                ) {
                
                boolean statOper = stmt.execute(upit);
                stmt.close();
                con.close();                
            } catch(SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
            }
            
        } catch(NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                Logger.getLogger(Vjezba_06_3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
