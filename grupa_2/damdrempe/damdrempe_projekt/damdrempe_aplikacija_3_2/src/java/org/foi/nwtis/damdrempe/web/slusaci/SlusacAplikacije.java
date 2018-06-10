package org.foi.nwtis.damdrempe.web.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;

/**
 * Web application lifecycle listener.
 *
 * @author ddrempetic
 */
public class SlusacAplikacije implements ServletContextListener {
    
    public static ServletContext servletContext;

    /**
     * Inicijalizira kontekst.
     * Čita konfiguracije i sprema ih u kontekst.
     * Sprema kontekst u varijablu klase.
     * Pokreće dretvu obrade.
     * @param sce događaj konteksta servleta
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String datoteka = sc.getInitParameter("konfiguracija");
        String putanja = sc.getRealPath("/WEB-INF") + java.io.File.separator;
        String puniNazivDatoteke = putanja + datoteka;
        
        try {
            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(puniNazivDatoteke);
            sce.getServletContext().setAttribute("Konfig", konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        servletContext = sc;
    }

    /**
     * Briše atribute iz konteksta i prekida dretvu obrade.
     * @param sce događaj konteksta servleta
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        sc.removeAttribute("Konfig");
    }
}
