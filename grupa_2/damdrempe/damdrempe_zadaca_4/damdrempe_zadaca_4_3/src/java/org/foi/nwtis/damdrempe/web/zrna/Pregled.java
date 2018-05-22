
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.foi.nwtis.damdrempe.ejb.eb.Meteo;
import org.foi.nwtis.damdrempe.ejb.sb.ParkiralistaFacade;
import org.foi.nwtis.damdrempe.ejb.eb.Parkiralista;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoFacade;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoKlijentZrno;
import org.foi.nwtis.damdrempe.web.podaci.Izbornik;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;

@Named(value = "pregled")
@SessionScoped
public class Pregled implements Serializable {

    @EJB
    private MeteoKlijentZrno meteoKlijentZrno;

    @EJB
    private MeteoFacade meteoFacade;

    @EJB
    private ParkiralistaFacade parkiralistaFacade;   

    private Integer id;
    private String adresa;
    private String naziv;
    private List<Izbornik> popisParking = new ArrayList<>();
    private List<String> popisParkingOdabrano = new ArrayList<>();
    private List<Izbornik> popisParkingMeteo = new ArrayList<>();
    private List<String> popisParkingMeteoOdabrana = new ArrayList<>();
    private List<Meteo> popisMeteoPodaci = new ArrayList<>();
    
    private boolean pokrenutoAzuriranje = false;
    
    public Pregled() { 
    }
    
    @PostConstruct
    public void init(){
        dohvatiPopisParking();
    }
    
    private Lokacija dohvatiLokacijuPrekoZrna(){
        String apikey = "eeab428a2e33536c5bb6deb266b37fcd";
        String gmapikey = "AIzaSyB1My2HHb8rRuQ35EUnPbwM2LOM1D5eItg";
        meteoKlijentZrno = new MeteoKlijentZrno();
        meteoKlijentZrno.postaviKorisnickePodatke(apikey, gmapikey);
        Lokacija lokacija = meteoKlijentZrno.dajLokaciju(adresa);
        
        return lokacija;
    }
    
    public String dodajParkiraliste(){ 
        //TODO ako je id null nađi sljedeci broj u bazi
        
        Parkiralista p = new Parkiralista();
        p.setId(id);
        p.setNaziv(naziv);
        p.setAdresa(adresa);
        
        Lokacija lokacija = dohvatiLokacijuPrekoZrna();
        p.setLatitude(Float.parseFloat(lokacija.getLatitude()));
        p.setLongitude(Float.parseFloat(lokacija.getLongitude()));
        parkiralistaFacade.create(p);
        
        setPokrenutoAzuriranje(true);
        dohvatiPopisParking();
        
        return "";
    }
    
    public String upisiParkiraliste(){ 
        //TODO ako je id null nađi sljedeci broj u bazi
        
        Parkiralista p = new Parkiralista();
        p.setId(id);
        p.setNaziv(naziv);
        p.setAdresa(adresa);
        
        Lokacija lokacija = dohvatiLokacijuPrekoZrna();
        p.setLatitude(Float.parseFloat(lokacija.getLatitude()));
        p.setLongitude(Float.parseFloat(lokacija.getLongitude()));
        parkiralistaFacade.edit(p);
        
        setPokrenutoAzuriranje(false);
        dohvatiPopisParking();
        
        return "";
    }
    
    public String azurirajParkiraliste(){        
        if (popisParkingOdabrano.size() == 1) {
            Parkiralista p = parkiralistaFacade.find(popisParkingOdabrano);
            id = p.getId();
            naziv = p.getNaziv();
            adresa = p.getAdresa();
            
            setPokrenutoAzuriranje(true);
        }       
        
        return "";
    }

    public String preuzmiParkiralista(){
        for(Izbornik i : popisParking){
            if(popisParkingOdabrano.contains(i.getVrijednost())
                    && !popisParkingMeteo.contains(i)){
              popisParkingMeteo.add(i);
            }
        }
        
        for(Izbornik i : popisParkingMeteo){
            if(popisParking.contains(i)){
                popisParking.remove(i);
            }
        }
        
        return "";
    }
    
    public String vratiParkiralista(){
        for(Izbornik i : popisParkingMeteo){
            if(popisParkingMeteoOdabrana.contains(i.getVrijednost())){
                popisParking.add(i);
            }
        }
        
        for(Izbornik i : popisParking){
            if(popisParkingMeteo.contains(i)){
                popisParkingMeteo.remove(i);
            }
        }
        
        return "";
    }
    
    public void dohvatiPopisParking(){
        popisParking = new ArrayList<>();
        
        for (Parkiralista parkiralista : parkiralistaFacade.findAll()) {
            Izbornik i = new Izbornik(parkiralista.getNaziv(), 
                    Integer.toString(parkiralista.getId()));
            popisParking.add(i);
        }       
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

    public List<Izbornik> getPopisParkingMeteo() {
        return popisParkingMeteo;
    }

    public void setPopisParkingMeteo(List<Izbornik> popisParkingMeteo) {
        this.popisParkingMeteo = popisParkingMeteo;
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

    public boolean isPokrenutoAzuriranje() {
        return pokrenutoAzuriranje;
    }

    public void setPokrenutoAzuriranje(boolean pokrenutoAzuriranje) {
        this.pokrenutoAzuriranje = pokrenutoAzuriranje;
    }
    
    
}
