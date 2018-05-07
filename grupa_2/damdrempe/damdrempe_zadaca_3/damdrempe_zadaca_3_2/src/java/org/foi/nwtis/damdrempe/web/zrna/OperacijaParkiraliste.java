package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.foi.nwtis.damdrempe.rest.klijenti.MeteoRESTKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.MeteoRESTKlijentId;
import org.foi.nwtis.damdrempe.web.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.ProcitaniJsonOdgovor;
import org.foi.nwtis.damdrempe.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste;

/**
 * Web zrno za operacije nad parkiralištima.
 * @author ddrempetic
 */
@Named(value = "operacijaParkiraliste")
@SessionScoped
public class OperacijaParkiraliste implements Serializable {

    private String naziv;
    private String adresa;
    private List<String> odabranaParkiralista;
    private List<MeteoPodaci> meteo;
    private String poruka = "";

    private Map<String, Object> popisParkiralistaPrikaz;   
    private boolean viseOdabrano = false;
    private boolean jedanOdabran = false;

    private int brojOdabranihParkiralista;

    private List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> meteopodaciSvih;
    private List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> meteopodaciPrikaz;

    private int pomak = 0;
    private int maksPomak = 0;
    private int odZapisa = 0;
    private int doZapisa;
    private int brojZapisaZaPrikaz = 20;
    private int ukupnoZapisa;

    /**
     * Konstruktor klase.
     * Preuzima popis svih parkirališta preko SOAP servisa.
     */
    public OperacijaParkiraliste() {
        preuzmiSvaParkiralistaSOAP();
    }

    /**
     * Akcija koja se poziva sa gumba Upiši SOAP.
     * Koristi SOAP operaciju za dodavanje novog parkirališta.
     * Ponovno dohvaća listu svih parkirališta.
     * @return
     */
    public String upisiSOAP() {
        Parkiraliste parkiraliste = new Parkiraliste();
        parkiraliste.setAdresa(adresa);
        parkiraliste.setNaziv(naziv);
        if (MeteoWSKlijent.dodajParkiraliste(parkiraliste)) {
            poruka = "Uspjesno dodano parkiraliste putem SOAP zahtjeva!";
        } else {
            poruka = "Greska kod dodavanja parkiralista putem SOAP zahtjeva!";
        }

        preuzmiSvaParkiralistaSOAP();

        return "";
    }

    /**
     * Pomoćna metoda za dohvat svih parkirališta.
     * Stavlja ih u mapu kako bi se mogli prikazati u select listi.
     */
    private void preuzmiSvaParkiralistaSOAP() {
        List<Parkiraliste> svaParkiralista = MeteoWSKlijent.dajSvaParkiralista();
        popisParkiralistaPrikaz = new LinkedHashMap<>();

        for (Parkiraliste parkiraliste : svaParkiralista) {
            popisParkiralistaPrikaz.put(parkiraliste.getNaziv(), parkiraliste.getId());
        }
    }

    /**
     * Ažurira vrijednosti odabira za parkirališta nakon događaja.
     * @param e događaj promjene
     */
    public void promjena(ValueChangeEvent e) {
        odabranaParkiralista = (List<String>) e.getNewValue();
        brojOdabranihParkiralista = odabranaParkiralista.size();
    }

    /**
     * Akcija koja se poziva na gumb Preuzmi SOAP.
     * Dohvaća podatke o pojedinom parkiralištu putem SOAP servisa i ažurira polja na sučelju.
     * @return
     */
    public String preuzmiParkiralisteSOAP() {
        List<Parkiraliste> svaParkiralista = MeteoWSKlijent.dajSvaParkiralista();
        int odabranoParkiralisteId = Integer.parseInt(odabranaParkiralista.get(0));
        Parkiraliste odabranoParkiraliste = new Parkiraliste();

        for (Parkiraliste parkiraliste : svaParkiralista) {
            if (parkiraliste.getId() == odabranoParkiralisteId) {
                odabranoParkiraliste = parkiraliste;
            }
        }

        naziv = odabranoParkiraliste.getNaziv();
        adresa = odabranoParkiraliste.getAdresa();

        return "";
    }

    /**
     * Akcija koja se poziva na gumb Preuzmi meteo.
     * Dohvaća meteopodatke putem SOAP servisa i priikazuje ih u tablici.
     * @return
     */
    public String preuzmiMeteoSOAP() {
        long odVrijeme = 0;
        long doVrijeme = System.currentTimeMillis();

        meteopodaciSvih = new ArrayList<>();

        for (String id : odabranaParkiralista) {
            int trenutnoParkiralisteId = Integer.parseInt(id);
            List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> meteopodaciJednog = new ArrayList<>();
            meteopodaciJednog = MeteoWSKlijent.dajSveMeteoPodatke(trenutnoParkiralisteId, odVrijeme, doVrijeme);

            for (org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci meteoPodaci : meteopodaciJednog) {
                meteopodaciSvih.add(meteoPodaci);
            }
        }

        azurirajPrikazMeteo();

        return "";
    }

    /**
     * Pomoćna operacija za paginaciju.
     * Ažuriraju se potrebne vrijednosti kako bi se osvježio prikaz.
     */
    public void azurirajPrikazMeteo() {
        ukupnoZapisa = meteopodaciSvih.size();
        odZapisa = pomak * brojZapisaZaPrikaz;
        doZapisa = odZapisa + brojZapisaZaPrikaz;

        maksPomak = ukupnoZapisa / brojZapisaZaPrikaz;
        if (ukupnoZapisa % brojZapisaZaPrikaz == 0) {
            maksPomak--;
        }

        if (doZapisa >= meteopodaciSvih.size()) {
            doZapisa = meteopodaciSvih.size();
        }

        meteopodaciPrikaz = new ArrayList<>();
        meteopodaciPrikaz.addAll(meteopodaciSvih.subList(odZapisa, doZapisa));
    }

    /**
     * Pomoćna operacija za paginaciju.
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.     
     * @return
     */
    public String prethodniZapisi() {
        pomak--;
        azurirajPrikazMeteo();

        return "";
    }

    /**
     * Pomoćna operacija za paginaciju.
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.     
     * @return
     */
    public String sljedeciZapisi() {
        pomak++;
        azurirajPrikazMeteo();

        return "";
    }
    
    /**
     * Akcija koja se poziva sa gumba Upiši REST.
     * Koristi REST operaciju za dodavanje novog parkirališta.
     * Ponovno dohvaća listu svih parkirališta.
     * @return
     */
    public String upisiREST() {
        MeteoRESTKlijent klijent = new MeteoRESTKlijent();
        String novoParkiraliste = PomocnaKlasa.napraviJsonZaSlanjeParkiraliste(naziv, adresa);
        String odgovorJsonTekst = klijent.postJson(novoParkiraliste, String.class);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        poruka = "Status: " + procitaniJsonOdgovor.getStatus();
        if(procitaniJsonOdgovor.getStatus().equals("ERR")){
            poruka = poruka + ". Poruka: " + procitaniJsonOdgovor.getPoruka();
        }

        preuzmiSvaParkiralistaSOAP();

        return "";
    }

    /**
     * Akcija koja se poziva na gumb Preuzmi REST.
     * Dohvaća podatke o pojedinom parkiralištu putem REST servisa i ažurira polja na sučelju.
     * @return
     */
    public String preuzmiParkiralisteREST() {
        String odabranoParkiralisteId = odabranaParkiralista.get(0);
        MeteoRESTKlijentId klijent = new MeteoRESTKlijentId(odabranoParkiralisteId);
        String odgovorJsonTekst = klijent.getJson(String.class);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        adresa = procitaniJsonOdgovor.vratiVrijednostAtributaIzOdgovora("adresa");
        naziv = procitaniJsonOdgovor.vratiVrijednostAtributaIzOdgovora("naziv");
        poruka = "Status: " + procitaniJsonOdgovor.getStatus();
        if(procitaniJsonOdgovor.getStatus().equals("ERR")){
            poruka = poruka + ". Poruka: " + procitaniJsonOdgovor.getPoruka();
        }

        return "";
    }

    /**
     * Akcija koja se poziva na gumb Ažuriraj REST.
     * Ažurira podatke o pojedinom parkiralištu putem REST servisa.
     * @return
     */
    public String azurirajREST() {
        String odabranoParkiralisteId = odabranaParkiralista.get(0);
        MeteoRESTKlijentId klijent = new MeteoRESTKlijentId(odabranoParkiralisteId);
        String novoParkiraliste = PomocnaKlasa.napraviJsonZaSlanjeParkiraliste(naziv, adresa);
        String odgovorJsonTekst = klijent.putJson(novoParkiraliste, String.class);
        
        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        poruka = "Status: " + procitaniJsonOdgovor.getStatus();
        if(procitaniJsonOdgovor.getStatus().equals("ERR")){
            poruka = poruka + ". Poruka: " + procitaniJsonOdgovor.getPoruka();
        }

        preuzmiSvaParkiralistaSOAP();

        return "";
    }

    /**
     * Akcija koja se poziva na gumb Briši REST.
     * Briše podatke o pojedinom parkiralištu putem REST servisa.
     * @return
     */
    public String brisiREST() {
        String odabranoParkiralisteId = odabranaParkiralista.get(0);
        MeteoRESTKlijentId klijent = new MeteoRESTKlijentId(odabranoParkiralisteId);
        String odgovorJsonTekst = klijent.deleteJson(String.class);

        ProcitaniJsonOdgovor procitaniJsonOdgovor = new ProcitaniJsonOdgovor(odgovorJsonTekst);
        poruka = "Status: " + procitaniJsonOdgovor.getStatus();
        if(procitaniJsonOdgovor.getStatus().equals("ERR")){
            poruka = poruka + ". Poruka: " + procitaniJsonOdgovor.getPoruka();
        }

        preuzmiSvaParkiralistaSOAP();
        if (odgovorJsonTekst.contains("OK")) {
            brojOdabranihParkiralista = 0;
        }

        return "";
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
    public List<String> getOdabranaParkiralista() {
        return odabranaParkiralista;
    }

    /**
     *
     * @param odabranaParkiralista
     */
    public void setOdabranaParkiralista(List<String> odabranaParkiralista) {
        this.odabranaParkiralista = odabranaParkiralista;
    }

    /**
     *
     * @return
     */
    public List<MeteoPodaci> getMeteo() {
        return meteo;
    }

    /**
     *
     * @param meteo
     */
    public void setMeteo(List<MeteoPodaci> meteo) {
        this.meteo = meteo;
    }

    /**
     *
     * @return
     */
    public Map<String, Object> getPopisParkiralistaPrikaz() {
        return popisParkiralistaPrikaz;
    }

    /**
     *
     * @param popisParkiralistaPrikaz
     */
    public void setPopisParkiralistaPrikaz(Map<String, Object> popisParkiralistaPrikaz) {
        this.popisParkiralistaPrikaz = popisParkiralistaPrikaz;
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

    /**
     *
     * @return
     */
    public boolean isViseOdabrano() {
        return viseOdabrano;
    }

    /**
     *
     * @param viseOdabrano
     */
    public void setViseOdabrano(boolean viseOdabrano) {
        this.viseOdabrano = viseOdabrano;
    }

    /**
     *
     * @return
     */
    public boolean isJedanOdabran() {
        return jedanOdabran;
    }

    /**
     *
     * @param jedanOdabran
     */
    public void setJedanOdabran(boolean jedanOdabran) {
        this.jedanOdabran = jedanOdabran;
    }

    /**
     *
     * @return
     */
    public int getBrojOdabranihParkiralista() {
        return brojOdabranihParkiralista;
    }

    /**
     *
     * @param brojOdabranihParkiralista
     */
    public void setBrojOdabranihParkiralista(int brojOdabranihParkiralista) {
        this.brojOdabranihParkiralista = brojOdabranihParkiralista;
    }

    /**
     *
     * @return
     */
    public List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> getMeteopodaciSvih() {
        return meteopodaciSvih;
    }

    /**
     *
     * @param meteopodaciSvih
     */
    public void setMeteopodaciSvih(List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> meteopodaciSvih) {
        this.meteopodaciSvih = meteopodaciSvih;
    }

    /**
     *
     * @return
     */
    public List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> getMeteopodaciPrikaz() {
        return meteopodaciPrikaz;
    }

    /**
     *
     * @param meteopodaciPrikaz
     */
    public void setMeteopodaciPrikaz(List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> meteopodaciPrikaz) {
        this.meteopodaciPrikaz = meteopodaciPrikaz;
    }

    /**
     *
     * @return
     */
    public int getPomak() {
        return pomak;
    }

    /**
     *
     * @param pomak
     */
    public void setPomak(int pomak) {
        this.pomak = pomak;
    }

    /**
     *
     * @return
     */
    public int getMaksPomak() {
        return maksPomak;
    }

    /**
     *
     * @param maksPomak
     */
    public void setMaksPomak(int maksPomak) {
        this.maksPomak = maksPomak;
    }

    /**
     *
     * @return
     */
    public int getBrojZapisaZaPrikaz() {
        return brojZapisaZaPrikaz;
    }

    /**
     *
     * @param brojZapisaZaPrikaz
     */
    public void setBrojZapisaZaPrikaz(int brojZapisaZaPrikaz) {
        this.brojZapisaZaPrikaz = brojZapisaZaPrikaz;
    }

    /**
     *
     * @return
     */
    public int getUkupnoZapisa() {
        return ukupnoZapisa;
    }

    /**
     *
     * @param ukupnoZapisa
     */
    public void setUkupnoZapisa(int ukupnoZapisa) {
        this.ukupnoZapisa = ukupnoZapisa;
    }

    /**
     *
     * @return
     */
    public int getOdZapisa() {
        return odZapisa;
    }

    /**
     *
     * @param odZapisa
     */
    public void setOdZapisa(int odZapisa) {
        this.odZapisa = odZapisa;
    }

    /**
     *
     * @return
     */
    public int getDoZapisa() {
        return doZapisa;
    }

    /**
     *
     * @param doZapisa
     */
    public void setDoZapisa(int doZapisa) {
        this.doZapisa = doZapisa;
    }    
}