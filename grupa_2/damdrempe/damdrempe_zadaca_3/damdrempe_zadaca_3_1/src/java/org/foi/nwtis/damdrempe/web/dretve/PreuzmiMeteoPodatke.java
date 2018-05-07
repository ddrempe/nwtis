/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import static java.lang.Thread.sleep;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.PomocnaKlasa;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

/**
 *
 * @author ddrempetic
 */
public class PreuzmiMeteoPodatke extends Thread {
    
    private int spavanje;
    private boolean radi = false;
    private int brojacObrada = 1;
    
    private BazaPodatakaOperacije bpo;
    private ServletContext sc;

    public PreuzmiMeteoPodatke(ServletContext sc) {
        this.sc = sc;
    }    

    @Override
    public void interrupt() {
        radi = false;
        try {  
            bpo.zatvoriVezu();
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }
    
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.       
    }

    @Override
    public void run() {
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        spavanje = Integer.parseInt(k.dajPostavku("intervalDretveZaMeteoPodatke")) * 1000;
        
        try {
            bpo = new BazaPodatakaOperacije(sc);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        radi = true;
        
        while (radi) {
            try {                
                ArrayList<Parkiraliste> dohvacenaParkiralista = bpo.parkiralistaSelect();
                for(Parkiraliste parkiraliste : dohvacenaParkiralista){
                    MeteoPodaci meteo = PomocnaKlasa.dohvatiOWMMeteo(parkiraliste.getGeoloc().getLatitude(), parkiraliste.getGeoloc().getLongitude());
                    if(meteo != null){
                        bpo.meteoInsert(meteo, parkiraliste);
                    }
                    else System.out.println("Nije moguce dohvatiti trenutne meteopodatke za parkiraliste " + parkiraliste.getNaziv() + " na adresi " + parkiraliste.getAdresa());
                }                
                              
                System.out.println("KRAJ! Završila obrada: " + (brojacObrada++) + ".");
                sleep(spavanje);
            } catch (InterruptedException ex) {
                System.out.println("Dretva je zaustavljena prilikom spavanja");
            } catch (SQLException ex) {
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
    }   
}
