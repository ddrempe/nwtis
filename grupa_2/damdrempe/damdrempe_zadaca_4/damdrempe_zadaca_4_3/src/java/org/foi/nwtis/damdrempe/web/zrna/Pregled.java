
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.damdrempe.ejb.eb.Dnevnik;
import org.foi.nwtis.damdrempe.ejb.sb.ParkiralistaFacade;
import org.foi.nwtis.damdrempe.ejb.eb.Parkiralista;
import org.foi.nwtis.damdrempe.ejb.sb.DnevnikFacade;
import org.foi.nwtis.damdrempe.ejb.sb.MeteoKlijentZrno;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.damdrempe.web.podaci.Izbornik;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

@Named(value = "pregled")
@SessionScoped
public class Pregled implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    @EJB
    private MeteoKlijentZrno meteoKlijentZrno;

    @EJB
    private ParkiralistaFacade parkiralistaFacade;   

    private Integer id;
    private String adresa;
    private String naziv;
    private List<Izbornik> popisParking = new ArrayList<>();
    private List<String> popisParkingOdabrano = new ArrayList<>();
    private List<Izbornik> popisParkingMeteo = new ArrayList<>();
    private List<String> popisParkingMeteoOdabrana = new ArrayList<>();
    
    private List<MeteoPrognoza> popisMeteoPrognoza = new ArrayList<>();
    
    private boolean prikaziGumbUpisi = false;
    private boolean prikaziGumbAzuriraj = false;    
    private boolean prikaziMeteopodatke = false;
    private String gumbPrognozeVrijednost;
    
    private String poruka = "";
    
    private long dnevnikPocetak;
    private long dnevnikKraj;
    private int dnevnikTrajanje;
    private int dnevnikStatus;
    
    private Comparator<Izbornik> c = Comparator.comparing(Izbornik::getLabela, String.CASE_INSENSITIVE_ORDER);
    
    public Pregled() { 
    }
    
    @PostConstruct
    public void init(){
        dohvatiPopisParking();
    }
    
    private Lokacija dohvatiLokacijuPrekoZrna(){
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        
        String apikey = k.dajPostavku("apikey");
        String gmapikey = k.dajPostavku("gmapikey");

        meteoKlijentZrno = new MeteoKlijentZrno();
        meteoKlijentZrno.postaviKorisnickePodatke(apikey, gmapikey);
        Lokacija lokacija = meteoKlijentZrno.dajLokaciju(adresa);
        
        return lokacija;
    }
    
    public String dodajParkiraliste(){ 
        //TODO ako je id null nađi sljedeci broj u bazi
        
        dnevnikPostavi();
        try {
            Parkiralista p = new Parkiralista();
            p.setId(id);
            p.setNaziv(naziv);
            p.setAdresa(adresa);

            Lokacija lokacija = dohvatiLokacijuPrekoZrna();
            p.setLatitude(Float.parseFloat(lokacija.getLatitude()));
            p.setLongitude(Float.parseFloat(lokacija.getLongitude()));
            parkiralistaFacade.create(p);

            setPrikaziGumbUpisi(false);
            dohvatiPopisParking(); 
            dnevnikUspjesanStatus();
        } catch (Exception e) {
            poruka = e.toString();
        }        

        dnevnikPisi("dodajParkiraliste");
        
        return "";
    }
    
    public String upisiParkiraliste(){  
        dnevnikPostavi();
        if(id == null){
            poruka = "Niste upisali ID!";
            dnevnikPisi("upisiParkiraliste");
            return "";
        }
        
        boolean postoji = false;
        for (Izbornik p : popisParking) {
            if(Integer.parseInt(p.getVrijednost()) == id){
                postoji = true;
                break;
            }
        }
        
        if(postoji == false){
            poruka = "Ne postoji parkiraliste za azuriranje s tim ID!";
            dnevnikPisi("upisiParkiraliste");
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

            setPrikaziGumbUpisi(false);
            dohvatiPopisParking();
            poruka="";
            dnevnikUspjesanStatus();
        } catch (Exception e) {
            poruka = e.toString();
        }
        
        dnevnikPisi("upisiParkiraliste");
        
        return "";
    }
    
    public String azurirajParkiraliste(){   
        if (popisParkingOdabrano.size() == 1) {
            dnevnikPostavi();
            Parkiralista p = parkiralistaFacade.find(popisParkingOdabrano);
            id = p.getId();
            naziv = p.getNaziv();
            adresa = p.getAdresa();
            
            setPrikaziGumbUpisi(true);
            dnevnikUspjesanStatus();
            dnevnikPisi("azurirajParkiraliste");
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
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        
        String apikey = k.dajPostavku("apikey");
        String gmapikey = k.dajPostavku("gmapikey");
        meteoKlijentZrno = new MeteoKlijentZrno();
        meteoKlijentZrno.postaviKorisnickePodatke(apikey, gmapikey);
        
        MeteoPrognoza[] meteoPrognoze = meteoKlijentZrno.dajMeteoPrognoze(idMeteo, adresaMeteo);
        
        return meteoPrognoze;
    }
    
    public String preuzmiMeteoPodatke(){
        prikaziMeteopodatke = !prikaziMeteopodatke;
        
        if(prikaziMeteopodatke == true){
            dnevnikPostavi();
            popisMeteoPrognoza.clear();        

            for (Parkiralista parkiralista : parkiralistaFacade.findAll()) {
                for (Izbornik izbornik : popisParkingMeteo) {
                    if(Integer.parseInt(izbornik.getVrijednost()) == parkiralista.getId()){
                        MeteoPrognoza[] mp = dohvatiMeteoPrekoZrna(parkiralista.getId(), parkiralista.getAdresa());
                        popisMeteoPrognoza.addAll(Arrays.asList(mp));
                    }
                }
            }
            
            dnevnikUspjesanStatus();
            dnevnikPisi("preuzmiMeteoPodatke");
        }
        
        return "";
    }
    
    public void promjena(AjaxBehaviorEvent event){
        prikaziGumbAzuriraj = popisParkingOdabrano.size() == 1;
    }
    
    private void dnevnikPostavi(){
        dnevnikPocetak = System.currentTimeMillis();
        dnevnikStatus = 0;
    }
    
    private void dnevnikUspjesanStatus(){
        dnevnikStatus = 1;
    }
    
    private void dnevnikPisi(String akcija){
        dnevnikKraj = System.currentTimeMillis();
        dnevnikTrajanje = (int) (dnevnikKraj - dnevnikPocetak);
        
        try {
            HttpServletRequest zahtjev = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String adresaZahtjeva = zahtjev.getRequestURL() + "/" + akcija;
            String ipAdresa = InetAddress.getLocalHost().getHostAddress();
            Date vrijeme = new Date(System.currentTimeMillis());
            
            Dnevnik dnevnik = new Dnevnik(1, "korisnik", adresaZahtjeva, ipAdresa, dnevnikTrajanje, dnevnikStatus);
            dnevnik.setVrijeme(vrijeme);
            dnevnikFacade.create(dnevnik);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Pregled.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public boolean isPrikaziGumbUpisi() {
        return prikaziGumbUpisi;
    }

    public void setPrikaziGumbUpisi(boolean prikaziGumbUpisi) {
        this.prikaziGumbUpisi = prikaziGumbUpisi;
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
