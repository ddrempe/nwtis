/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoOsvjezivac;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoPrognosticar;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.websocket.ParkiralisteEndpoint;
import org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste;

/**
 *
 * @author grupa_2
 */
@Named(value = "meteoPrognoza")
@SessionScoped
public class MeteoPrognoza implements Serializable {

    @EJB
    private MeteoOsvjezivac meteoOsvjezivac;

    @EJB
    private MeteoPrognosticar meteoPrognosticar;
    
    private String odabranoParkiraliste;
    private List<String> parkiralista;
    private List<MeteoPodaci> meteoPodaci;
    private String naziv;
    private String adresa;

    /**
     * Creates a new instance of MeteoPrognoza
     */
    public MeteoPrognoza() {
    }   
    
    public String dodajParkiraliste() {
        parkiralista = meteoPrognosticar.dajParkiralista();
        Parkiraliste p = new Parkiraliste();
        p.setNaziv(naziv);
        p.setAdresa(adresa);
        
        meteoOsvjezivac.sendJMSMessageToNWTiS_vjezba_12(
                "Dodano parkiralište: " + naziv + adresa);
        
        ParkiralisteEndpoint.obavijestiPromjenu("Dodano parkiralište: " + naziv + adresa);
        
        return "";
    }    

    public String getOdabranoParkiraliste() {
        return odabranoParkiraliste;
    }

    public void setOdabranoParkiraliste(String odabranoParkiraliste) {
        this.odabranoParkiraliste = odabranoParkiraliste;
    }

    public List<String> getParkiralista() {
        parkiralista = meteoPrognosticar.getParkiralista();
        
        return parkiralista;
    }

    public void setParkiralista(List<String> parkiralista) {
        this.parkiralista = parkiralista;
    }

    public List<MeteoPodaci> getMeteoPodaci() {
        meteoPodaci=meteoPrognosticar.getMeteoPodaci();
        return meteoPodaci;
    }

    public void setMeteoPodaci(List<MeteoPodaci> meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    
    
    
    
}
