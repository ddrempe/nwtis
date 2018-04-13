/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.konfiguracije.bp;

import java.util.Enumeration;
import java.util.Properties;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author grupa_2
 */
public class BP_Konfiguracija implements BP_Sucelje{
    private Konfiguracija konfiguracija;
    
    public BP_Konfiguracija(String datoteka) throws NemaKonfiguracije, NeispravnaKonfiguracija {
        konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
    }

    @Override
    public String getAdminDatabase() {
        return konfiguracija.dajPostavku("admin.database");
    }

    @Override
    public String getAdminPassword() {
        return konfiguracija.dajPostavku("admin.password");
    }

    @Override
    public String getAdminUsername() {
        return konfiguracija.dajPostavku("admin.username");
    }

    @Override
    public String getDriverDatabase() {
        String serverDatabase = getServerDatabase();
        return getDriverDatabase(serverDatabase);
    }

    @Override
    public String getDriverDatabase(String bp_url) {
        String[] parametri = bp_url.split(":"); //TODO split regex? iz ovog stringa jdbc:mysql://192.168.15.1/ zelimo izvaditi mysql
        return konfiguracija.dajPostavku("driver.database." + parametri[1]);
    }

    @Override
    public Properties getDriversDatabase() {
        Properties postavke = new Properties();
        for(Enumeration e = konfiguracija.dajSvePostavke().keys(); e.hasMoreElements();){
            String kljuc = (String) e.nextElement();
            if(kljuc.startsWith("driver.database.")){
                String vrijednost = konfiguracija.dajPostavku(kljuc);
                postavke.setProperty(kljuc, vrijednost);
            }
        }
        
        return postavke;
    }

    @Override
    public String getServerDatabase() {
        return konfiguracija.dajPostavku("server.database");
    }

    @Override
    public String getUserDatabase() {
        return konfiguracija.dajPostavku("user.database");
    }

    @Override
    public String getUserPassword() {
        return konfiguracija.dajPostavku("user.password");
    }

    @Override
    public String getUserUsername() {
        return konfiguracija.dajPostavku("user.username");
    }    
}