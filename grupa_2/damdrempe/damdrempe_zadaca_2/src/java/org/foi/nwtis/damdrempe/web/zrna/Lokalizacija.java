package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.context.FacesContext;

/**
 * Klasa za lokalizaciju.
 * @author ddrempetic
 */
@Named(value = "lokalizacija")
@SessionScoped
public class Lokalizacija implements Serializable {
    
    private String odabraniJezik = "hr";

    /**
     * Konstruktor klase.
     */
    public Lokalizacija() {
    }  
    
    /**
     * Mijenja trenutno odabrani jezik.
     * @param jezik
     * @return odredište za navigaciju
     */
    public Object odaberiJezik(String jezik) {
        Locale locale = new Locale(jezik);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        odabraniJezik = jezik;   
        return "";
    } 
    
    /**
     * Metoda za navigacijsko pravilo.
     *
     * @return odredište za navigaciju
     */
    public String slanjePoruka() {
        return "slanjePoruka";
    }
    
    /**
     * Metoda za navigacijsko pravilo.
     *
     * @return odredište za navigaciju
     */
    public String pregledPoruka() {
        return "pregledPoruka";
    }
    
    /**
     * Metoda za navigacijsko pravilo.
     *
     * @return odredište za navigaciju
     */
    public String pregledDnevnika() {
        return "pregledDnevnika";
    }
    
    /**
     * Getter za odabrani jezik
     * @return odabrani jezik (hr, de, en)
     */
    public String getOdabraniJezik() {
        return odabraniJezik;
    } 
}
