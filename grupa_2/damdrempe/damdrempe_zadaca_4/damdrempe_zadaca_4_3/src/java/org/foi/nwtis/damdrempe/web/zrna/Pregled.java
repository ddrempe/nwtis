/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.foi.nwtis.damdrempe.ejb.sb.ParkiralistaFacade;
import org.foi.nwtis.damdrempe.ejb.eb.Parkiralista;

/**
 *
 * @author grupa_2
 */
@Named(value = "pregled")
@RequestScoped
public class Pregled{

    @EJB
    private ParkiralistaFacade parkiralistaFacade;

    private Integer id;
    private String adresa;
    private String naziv;
    
    public Pregled() {
    }
    
    public String dodajParkiraliste(){
        Parkiralista p = new Parkiralista();
        p.setId(id);
        p.setNaziv(naziv);
        p.setAdresa(adresa);
        return "";        
    }
    
    

    public String azurirajParkiraliste(){
        return "";        
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
    
    
}
