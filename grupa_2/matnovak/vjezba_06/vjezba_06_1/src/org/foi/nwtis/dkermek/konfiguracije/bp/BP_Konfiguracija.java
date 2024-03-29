/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dkermek.konfiguracije.bp;

import java.util.Enumeration;
import java.util.Properties;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.matnovak.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author grupa_2
 */
public class BP_Konfiguracija implements BP_Sucelje {
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
        String server_database = getServerDatabase();
        return getDriverDatabase(server_database);
    }

    @Override
    public String getDriverDatabase(String bp_url) {
        String[] p = bp_url.split(":");
        return konfiguracija.dajPostavku("driver.database." + p[1]);
    }

    @Override
    public Properties getDriversDatabase() {
        Properties p = new Properties();
        for(Enumeration e=konfiguracija.dajSvePostavke().keys();e.hasMoreElements();) {
            String k = (String) e.nextElement();
            if(k.startsWith("driver.database.")) {
                String v = konfiguracija.dajPostavku(k);
                p.setProperty(k, v);
            }
        }
        return p;
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
