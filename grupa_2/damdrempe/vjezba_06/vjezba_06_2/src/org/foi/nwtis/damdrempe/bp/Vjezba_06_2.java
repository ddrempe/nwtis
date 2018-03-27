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
public class Vjezba_06_2 {

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
            String korisnik = bpk.getUserUsername();
            String lozinka = bpk.getUserPassword();
            String upit = "select maticni_broj, prezime, ime from POLAZNICI";      

            try (   //TODO obrati paznju na try with resources
                Connection con = DriverManager.getConnection(url, korisnik, lozinka);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(upit);) {

                System.out.println("Popis polaznika:");
                while (rs.next()) {
                    String mb = rs.getString("maticni_broj");
                    String pr = rs.getString("prezime");
                    String im = rs.getString("ime");
                    System.out.println(mb + " " + pr + " " + im);
                }
                rs.close();
                stmt.close();
                con.close();                
            } catch(SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
            }
            
        } catch(NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                Logger.getLogger(Vjezba_06_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
