/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.foi.nwtis.damdrempe.ejb.eb.Dnevnik;
import org.foi.nwtis.damdrempe.ejb.sb.DnevnikFacade;

/**
 *
 * @author ddrempetic
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;
    
    private String ipAdresa;
    private String odVrijeme;
    private String doVrijeme;
    private String adresaZahtjeva;
    private String trajanje;    
    
    private List<Dnevnik> listaDnevnik = new ArrayList<>();

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
        
    }
    
    @PostConstruct
    public void init(){
        if (listaDnevnik.isEmpty()) {
            preuzmi();
        }
    }
    
    public void preuzmi(){
        listaDnevnik = dnevnikFacade.findAll();
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public String getOdVrijeme() {
        return odVrijeme;
    }

    public void setOdVrijeme(String odVrijeme) {
        this.odVrijeme = odVrijeme;
    }

    public String getDoVrijeme() {
        return doVrijeme;
    }

    public void setDoVrijeme(String doVrijeme) {
        this.doVrijeme = doVrijeme;
    }

    public String getAdresaZahtjeva() {
        return adresaZahtjeva;
    }

    public void setAdresaZahtjeva(String adresaZahtjeva) {
        this.adresaZahtjeva = adresaZahtjeva;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public List<Dnevnik> getListaDnevnik() {
        return listaDnevnik;
    }

    public void setListaDnevnik(List<Dnevnik> listaDnevnik) {
        this.listaDnevnik = listaDnevnik;
    }   
}
