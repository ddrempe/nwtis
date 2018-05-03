/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author grupa_2
 */
public class SlusacAplikacije implements ServletContextListener {
    
    private static ServletContext servletContext;

    public static ServletContext getServletContext() {
        return servletContext;
    }   

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String datoteka = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("/WEB-INF") + java.io.File.separator;
        String puniNazivDatoteke = putanja + datoteka;
        
        BP_Konfiguracija bpk;
        try {
            bpk = new BP_Konfiguracija(puniNazivDatoteke);
            sc.setAttribute("BP_Konfig", bpk);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        sc.removeAttribute("BP_Konfig");
    } 
}
