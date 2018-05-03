/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.foi.nwtis.damdrempe.rest.klijenti.MeteoRESTKlijent;
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
    private List<Parkiraliste> parkiralista;
    private List<Integer> odabranaParkiralista;
    private List<MeteoPodaci> meteo;

    /**
     * Creates a new instance of OperacijaParkiraliste
     */
    public OperacijaParkiraliste() {
    }
    
    public String upisiSOAP() {
        // TODO preuzmi geo lokaciju na bazi adrese
        // TODO upiši parkiralište u bazu podataka  
        
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
    
    
    

