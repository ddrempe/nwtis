
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.foi.nwtis.damdrempe.ejb.eb.Meteo;
import org.foi.nwtis.damdrempe.ejb.sb.ParkiralistaFacade;
import org.foi.nwtis.damdrempe.ejb.eb.Parkiralista;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoFacade;
import org.foi.nwtis.damdrempe.web.podaci.Izbornik;

@Named(value = "pregled")
@SessionScoped
public class Pregled implements Serializable {

    @EJB
    private MeteoFacade meteoFacade;

    @EJB
    private ParkiralistaFacade parkiralistaFacade;

    private Integer id;
    private String adresa;
    private String naziv;
    private List<Izbornik> popisParking = new ArrayList<>();
    private List<String> popisParkingOdabrano = new ArrayList<>();
    private List<Izbornik> popisParkingMeto = new ArrayList<>();
    private List<String> popisParkingMeteoOdabrana = new ArrayList<>();
    private List<Meteo> popisMeteoPodaci = new ArrayList<>();
    
    public Pregled() {
    }
    
    public String dodajParkiraliste(){
        Parkiralista p = new Parkiralista();
        p.setId(id);
        p.setNaziv(naziv);
        p.setAdresa(adresa);
        parkiralistaFacade.create(p);
        return "";
    }
    
    public String azurirajParkiraliste(){
        Parkiralista p = new Parkiralista();
        p.setId(id);
        p.setNaziv(naziv);
        p.setAdresa(adresa);
        parkiralistaFacade.edit(p);
        return "";
    }

    public String preuzmiParkiralista(){
        for(Izbornik i : popisParking){
            if(popisParkingOdabrano.contains(i.getVrijednost())
                    && !popisParkingMeto.contains(i)){
              popisParkingMeto.add(i);
            }
        }
        
        for(Izbornik i : popisParkingMeto){
            if(popisParking.contains(i)){
                popisParking.remove(i);
            }
        }
        
        return "";
    }
    
    public String preuzmiMeteoPodatke(){
        
        popisMeteoPodaci = meteoFacade.findByParkiraliste(1);
                
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

    public List<Izbornik> getPopisParking() {
        popisParking = new ArrayList<>();
        
        for (Parkiralista parkiralista : parkiralistaFacade.findAll()) {
            Izbornik i = new Izbornik(parkiralista.getNaziv(), 
                    Integer.toString(parkiralista.getId()));
            popisParking.add(i);
        }

        return popisParking;
    }

    public void setPopisParking(List<Izbornik> popisParking) {
        this.popisParking = popisParking;
    }

    public List<String> getPopisParkingOdabrano() {
        return popisParkingOdabrano;
    }

    public void setPopisParkingOdabrano(List<String> popisParkingOdabrano) {
        this.popisParkingOdabrano = popisParkingOdabrano;
    }

    public List<Izbornik> getPopisParkingMeto() {
        return popisParkingMeto;
    }

    public void setPopisParkingMeto(List<Izbornik> popisParkingMeto) {
        this.popisParkingMeto = popisParkingMeto;
    }

    public List<String> getPopisParkingMeteoOdabrana() {
        return popisParkingMeteoOdabrana;
    }

    public void setPopisParkingMeteoOdabrana(List<String> popisParkingMeteoOdabrana) {
        this.popisParkingMeteoOdabrana = popisParkingMeteoOdabrana;
    }

    public List<Meteo> getPopisMeteoPodaci() {
        return popisMeteoPodaci;
    }

    public void setPopisMeteoPodaci(List<Meteo> popisMeteoPodaci) {
        this.popisMeteoPodaci = popisMeteoPodaci;
    }
    
    
}
