/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dkermek.bp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.dkermek.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author grupa_2
 */
public class Vjezba_06_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Premalo ili previše agumenata.");
            return;
        }
        Vjezba_06_2 v = new Vjezba_06_2();
        v.ispisiPodatke(args[0]);
    }

    private void ispisiPodatke(String datoteka) {
        try {
            BP_Konfiguracija bpk = new BP_Konfiguracija(datoteka);
            
            String url = bpk.getServerDatabase() + bpk.getUserDatabase();
            String korisnik = bpk.getUserUsername();
            String lozinka = bpk.getUserPassword();
            String upit = "select maticni_broj, prezime, ime from polaznici";
            
            try (
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
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
            }
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(Vjezba_06_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
