
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import org.foi.nwtis.damdrempe.ejb.eb.Meteo;
import org.foi.nwtis.damdrempe.ejb.sb.ParkiralistaFacade;
import org.foi.nwtis.damdrempe.ejb.eb.Parkiralista;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoFacade;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoKlijentZrno;
import org.foi.nwtis.damdrempe.web.podaci.Izbornik;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;

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
    
    private List<MeteoPrognoza> popisMeteoPrognoza = new ArrayList<>();
    
    private boolean pokrenutoAzuriranje = false;
    private boolean prikaziGumbAzuriraj = false;
    
    private boolean prikaziMeteopodatke = false;
    private String gumbPrognozeVrijednost;
    
    private String poruka = "";
    
    private Comparator<Izbornik> c = Comparator.comparing(Izbornik::getLabela, String.CASE_INSENSITIVE_ORDER);
    
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
        try {
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
        } catch (Exception e) {
            poruka = e.toString();
        }
        
        return "";
    }
    
    public String upisiParkiraliste(){ 
        if(id == null){
            poruka = "Niste upisali ID!";
            return "";
        }
        
        //TODO ako ne postoji ID javi poruku
        boolean postoji = false;
        List<Parkiralista> parkiralista = parkiralistaFacade.findAll();
        for (Parkiralista p : parkiralista) {
            if(p.getId() == id){
                postoji = true;
                break;
            }
        }
        
        if(postoji == false){
            poruka = "Ne postoji parkiraliste s tim ID!";
            return "";
        }
        
        try {
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
            poruka="";
        } catch (Exception e) {
            poruka = e.toString();
        }
        
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
        
        popisParkingMeteo.sort(c);
        
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
        
        popisParking.sort(c);
        
        return "";
    }
    
    public void dohvatiPopisParking(){
        popisParking = new ArrayList<>();
        
        for (Parkiralista parkiralista : parkiralistaFacade.findAll()) {
            Izbornik i = new Izbornik(parkiralista.getNaziv(), 
                    Integer.toString(parkiralista.getId()));
            popisParking.add(i);
        }  
        
        popisParking.sort(c);
    }
    
    private MeteoPrognoza[] dohvatiMeteoPrekoZrna(int idMeteo, String adresaMeteo){
        String apikey = "eeab428a2e33536c5bb6deb266b37fcd";
        String gmapikey = "AIzaSyB1My2HHb8rRuQ35EUnPbwM2LOM1D5eItg";
        meteoKlijentZrno = new MeteoKlijentZrno();
        meteoKlijentZrno.postaviKorisnickePodatke(apikey, gmapikey);
        
        MeteoPrognoza[] meteoPrognoze = meteoKlijentZrno.dajMeteoPrognoze(idMeteo, adresaMeteo);
        
        return meteoPrognoze;
    }
    
    public String preuzmiMeteoPodatke(){        
        popisMeteoPrognoza.clear();        
        
        for (Parkiralista parkiralista : parkiralistaFacade.findAll()) {
            for (Izbornik izbornik : popisParkingMeteo) {
                if(Integer.parseInt(izbornik.getVrijednost()) == parkiralista.getId()){
                    MeteoPrognoza[] mp = dohvatiMeteoPrekoZrna(parkiralista.getId(), parkiralista.getAdresa());
                    popisMeteoPrognoza.addAll(Arrays.asList(mp));
                }
            }
        }
        
        prikaziMeteopodatke = !prikaziMeteopodatke;
                
        return "";
    }
    
    public void promjena(AjaxBehaviorEvent event){
        prikaziGumbAzuriraj = popisParkingOdabrano.size() == 1;
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

    public List<MeteoPrognoza> getPopisMeteoPrognoza() {
        return popisMeteoPrognoza;
    }

    public void setPopisMeteoPrognoza(List<MeteoPrognoza> popisMeteoPrognoza) {
        this.popisMeteoPrognoza = popisMeteoPrognoza;
    }

    public boolean isPrikaziGumbAzuriraj() {
        return prikaziGumbAzuriraj;
    }

    public void setPrikaziGumbAzuriraj(boolean prikaziGumbAzuriraj) {
        this.prikaziGumbAzuriraj = prikaziGumbAzuriraj;
    }

    public String getGumbPrognozeVrijednost() {
        this.gumbPrognozeVrijednost = prikaziMeteopodatke ? "Zatvori prognoze" : "Prognoze";
        return gumbPrognozeVrijednost;
    }

    public void setGumbPrognozeVrijednost(String gumbPrognozeVrijednost) {
        this.gumbPrognozeVrijednost = gumbPrognozeVrijednost;
    }

    public boolean isPrikaziMeteopodatke() {
        return prikaziMeteopodatke;
    }

    public void setPrikaziMeteopodatke(boolean prikaziMeteopodatke) {
        this.prikaziMeteopodatke = prikaziMeteopodatke;
    }    

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }   
}
