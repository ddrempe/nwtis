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
public class Vjezba_06_3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Premalo ili previše agumenata.");
            return;
        }
        Vjezba_06_3 v = new Vjezba_06_3();
        v.ispisiPodatke(args[0]);
    }

    private void ispisiPodatke(String datoteka) {
        try {
            BP_Konfiguracija bpk = new BP_Konfiguracija(datoteka);

            String url = bpk.getServerDatabase() + bpk.getUserDatabase();
            String korisnik = bpk.getAdminUsername();
            String lozinka = bpk.getAdminPassword();
            String upit = "create table test_dkermek ("
                    + "kor_ime varchar(10) NOT NULL DEFAULT '', "
                    + "zapis varchar(250) NOT NULL DEFAULT ''"
                    + ")";

            try (
                    Connection con = DriverManager.getConnection(url, korisnik, lozinka);
                    Statement stmt = con.createStatement();) {
                boolean statOper = stmt.execute(upit);
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
            }
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(Vjezba_06_3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
