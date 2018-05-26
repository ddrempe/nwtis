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
import org.foi.nwtis.damdrempe.web.podaci.Izbornik;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPrognoza;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 * Zrno za rad s pregledom/ažuriranjem parkirališta i preuzimanjem meteopodataka.
 * @author ddrempetic
 */
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
    
    private Comparator<Izbornik> comparator = Comparator.comparing(Izbornik::getLabela, String.CASE_INSENSITIVE_ORDER);
    
    /**
     * Konstruktor klase.
     */
    public Pregled() { 
    }
    
    /**
     * Dohvaća popis parkirališta.
     */
    @PostConstruct
    public void init(){
        dohvatiPopisParking();
    }
    
    /**
     * Služi za dohvaćanje lokacije za trenutno upisanu adresu na sučelju.
     * @return objekt s podacima lokacije.
     */
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
    
    /**
     * Dodaje parkiralište s trenutno upisanim podacima na sučelju u bazu podataka.
     * @return
     */
    public String dodajParkiraliste(){         
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
    
    /**
     * Provjerava da li parkiralište s upisanim ID postoji i zapisuje ga u tablicu u bazi.
     * @return
     */
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
        
        upisiParkiralisteUBazu();        
        dnevnikPisi("upisiParkiraliste");        
        return "";
    }

    /**
     * Za podatke upisane na sučelju stvara novi objekt parkirališta i upisuje u bazu podataka.
     */
    private void upisiParkiralisteUBazu() {
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
    }
    
    /**
     * Učitava podatke o parkiralištu iz tablice u bazi podataka i prikazuje ih na sučelju za kasnije ažuriranje.
     * @return
     */
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

    /**
     * Prebacuje selektirana parkirališta iz inicijalnog popisa u popis odabranih.
     * @return
     */
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
        
        popisParkingMeteo.sort(comparator);
        
        return "";
    }
    
    /**
     * Prebacuje selektirana parkirališta iz popisa odabranih u inicijalni popis.
     * @return
     */
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
        
        popisParking.sort(comparator);
        
        return "";
    }
    
    /**
     * Dohvaća popis svih parkirališta iz tablice i sortira abecedno.
     */
    public void dohvatiPopisParking(){
        popisParking = new ArrayList<>();
        
        for (Parkiralista parkiralista : parkiralistaFacade.findAll()) {
            Izbornik i = new Izbornik(parkiralista.getNaziv(), 
                    Integer.toString(parkiralista.getId()));
            popisParking.add(i);
        }  
        
        popisParking.sort(comparator);
    }
    
    /**
     * Preko meteoKlijentZrna dohvaća meteopodatke.
     * @param idMeteo
     * @param adresaMeteo
     * @return niz meteoprognoza
     */
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
    
    /**
     * Za odabrana parkirališta preuzima meteopodatke i sprema u odgovarajući niz.
     * @return
     */
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
    
    /**
     * Ovisno o broju odabranih parkirališta na popisu mijenja vidljivost gumba Azuriraj.
     * @param event
     */
    public void promjena(AjaxBehaviorEvent event){
        prikaziGumbAzuriraj = popisParkingOdabrano.size() == 1;
    }
    
    /**
     * Metoda za početak rada s dnevnikom.
     */
    private void dnevnikPostavi(){
        dnevnikPocetak = System.currentTimeMillis();
        dnevnikStatus = 0;
    }
    
    /**
     * Metoda za postavljanje uspješnog statusa rada.
     */
    private void dnevnikUspjesanStatus(){
        dnevnikStatus = 1;
    }
    
    /**
     * Zapisuje informacije o korisničkim akcijama u tablicu dnevnik.
     * @param akcija 
     */
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
    
    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     *
     * @param adresa
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     *
     * @return
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     *
     * @param naziv
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**
     *
     * @return
     */
    public List<Izbornik> getPopisParking() {
        return popisParking;
    }

    /**
     *
     * @param popisParking
     */
    public void setPopisParking(List<Izbornik> popisParking) {
        this.popisParking = popisParking;
    }

    /**
     *
     * @return
     */
    public List<String> getPopisParkingOdabrano() {
        return popisParkingOdabrano;
    }

    /**
     *
     * @param popisParkingOdabrano
     */
    public void setPopisParkingOdabrano(List<String> popisParkingOdabrano) {
        this.popisParkingOdabrano = popisParkingOdabrano;
    }

    /**
     *
     * @return
     */
    public List<Izbornik> getPopisParkingMeteo() {
        return popisParkingMeteo;
    }

    /**
     *
     * @param popisParkingMeteo
     */
    public void setPopisParkingMeteo(List<Izbornik> popisParkingMeteo) {
        this.popisParkingMeteo = popisParkingMeteo;
    }

    /**
     *
     * @return
     */
    public List<String> getPopisParkingMeteoOdabrana() {
        return popisParkingMeteoOdabrana;
    }

    /**
     *
     * @param popisParkingMeteoOdabrana
     */
    public void setPopisParkingMeteoOdabrana(List<String> popisParkingMeteoOdabrana) {
        this.popisParkingMeteoOdabrana = popisParkingMeteoOdabrana;
    }

    /**
     *
     * @return
     */
    public boolean isPrikaziGumbUpisi() {
        return prikaziGumbUpisi;
    }

    /**
     *
     * @param prikaziGumbUpisi
     */
    public void setPrikaziGumbUpisi(boolean prikaziGumbUpisi) {
        this.prikaziGumbUpisi = prikaziGumbUpisi;
    }

    /**
     *
     * @return
     */
    public List<MeteoPrognoza> getPopisMeteoPrognoza() {
        return popisMeteoPrognoza;
    }

    /**
     *
     * @param popisMeteoPrognoza
     */
    public void setPopisMeteoPrognoza(List<MeteoPrognoza> popisMeteoPrognoza) {
        this.popisMeteoPrognoza = popisMeteoPrognoza;
    }

    /**
     *
     * @return
     */
    public boolean isPrikaziGumbAzuriraj() {
        return prikaziGumbAzuriraj;
    }

    /**
     *
     * @param prikaziGumbAzuriraj
     */
    public void setPrikaziGumbAzuriraj(boolean prikaziGumbAzuriraj) {
        this.prikaziGumbAzuriraj = prikaziGumbAzuriraj;
    }

    /**
     * 
     * @return
     */
    public String getGumbPrognozeVrijednost() {
        this.gumbPrognozeVrijednost = prikaziMeteopodatke ? "Zatvori prognoze" : "Prognoze";
        return gumbPrognozeVrijednost;
    }

    /**
     *
     * @param gumbPrognozeVrijednost
     */
    public void setGumbPrognozeVrijednost(String gumbPrognozeVrijednost) {
        this.gumbPrognozeVrijednost = gumbPrognozeVrijednost;
    }

    /**
     *
     * @return
     */
    public boolean isPrikaziMeteopodatke() {
        return prikaziMeteopodatke;
    }

    /**
     *
     * @param prikaziMeteopodatke
     */
    public void setPrikaziMeteopodatke(boolean prikaziMeteopodatke) {
        this.prikaziMeteopodatke = prikaziMeteopodatke;
    }    

    /**
     *
     * @return
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     *
     * @param poruka
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }   
}
