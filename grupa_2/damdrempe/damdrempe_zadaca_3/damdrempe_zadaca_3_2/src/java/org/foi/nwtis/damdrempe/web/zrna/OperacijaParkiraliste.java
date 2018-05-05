/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ValueChangeEvent;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.foi.nwtis.damdrempe.rest.klijenti.MeteoRESTKlijent;
import org.foi.nwtis.damdrempe.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste;

/**
 *
 * @author grupa_2
 */
@Named(value = "operacijaParkiraliste")
@RequestScoped  //TODO vidjeti da li request ili session, korak 65
public class OperacijaParkiraliste {
    
    private String naziv;
    private String adresa;
    //private List<Parkiraliste> parkiralista;
    private List<String> odabranaParkiralista;
    private List<MeteoPodaci> meteo;
    private String poruka = "";
    
    private Map<String, Object> popisParkiralistaPrikaz; //todo rename    
    private int brojParkiralistaPrikaz;

    /**
     * Creates a new instance of OperacijaParkiraliste
     */
    public OperacijaParkiraliste() {
        dohvatiSvaParkiralistaSOAP();
    }
    
    public String upisiSOAP() {        
        if (MeteoWSKlijent.dodajParkiraliste(naziv, adresa)){
            poruka = "Uspjesno dodano parkiraliste putem SOAP zahtjeva!";
        } else {
            poruka = "Greska kod dodavanja parkiralista putem SOAP zahtjeva!";
        }
        
        return "";
    }
    
    public String upisiREST() {
        MeteoRESTKlijent klijent = new MeteoRESTKlijent();
        String novoParkiraliste = "{\"naziv\": \"" + naziv + "\","
            + "\"adresa\": \"" + adresa + "\"}";
        String odgovor = klijent.postJson(novoParkiraliste, String.class);
        System.out.println(odgovor);
        //TODO moglo bi se sa gsonom napraviti objekt parkiraliste i onda prebaciti u string
        
        //TODO tu smo stali sa klijentom
        return "";
    }
    
    private void dohvatiSvaParkiralistaSOAP(){
        List<Parkiraliste> svaParkiralista = MeteoWSKlijent.dajSvaParkiralista();
        popisParkiralistaPrikaz = new LinkedHashMap<>();

        for (Parkiraliste parkiraliste : svaParkiralista) {
            popisParkiralistaPrikaz.put(parkiraliste.getNaziv(), parkiraliste.getId());
        }
    }
    
    public void promjena(ValueChangeEvent e) {
        odabranaParkiralista = (List<String>) e.getNewValue();
        brojParkiralistaPrikaz = odabranaParkiralista.size();
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

    public List<String> getOdabranaParkiralista() {
        return odabranaParkiralista;
    }

    public void setOdabranaParkiralista(List<String> odabranaParkiralista) {
        this.odabranaParkiralista = odabranaParkiralista;
    }

    public List<MeteoPodaci> getMeteo() {
        return meteo;
    }

    public void setMeteo(List<MeteoPodaci> meteo) {
        this.meteo = meteo;
    }

    public Map<String, Object> getPopisParkiralistaPrikaz() {
        return popisParkiralistaPrikaz;
    }

    public void setPopisParkiralistaPrikaz(Map<String, Object> popisParkiralistaPrikaz) {
        this.popisParkiralistaPrikaz = popisParkiralistaPrikaz;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public int getBrojParkiralistaPrikaz() {
        return brojParkiralistaPrikaz;
    }

    public void setBrojParkiralistaPrikaz(int brojParkiralistaPrikaz) {
        this.brojParkiralistaPrikaz = brojParkiralistaPrikaz;
    }
    
    
    
    

    
}
    
    
    

