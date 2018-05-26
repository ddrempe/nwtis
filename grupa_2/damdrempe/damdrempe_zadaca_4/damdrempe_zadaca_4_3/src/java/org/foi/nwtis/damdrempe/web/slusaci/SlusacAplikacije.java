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
 * Slušač aplikacije.
 * @author ddrempetic
 */
public class SlusacAplikacije implements ServletContextListener {
    
    public static ServletContext servletContext;

    /**
     * Obavlja posao nakon inicijalizacije konteksta.
     * Čita konfiguraciju i sprema u kontekst.
     * Sprema kontekst u varijablu klase.
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
     * Obavlja posao nakon uništenja konteksta.
     * @param sce događaj konteksta servleta
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
