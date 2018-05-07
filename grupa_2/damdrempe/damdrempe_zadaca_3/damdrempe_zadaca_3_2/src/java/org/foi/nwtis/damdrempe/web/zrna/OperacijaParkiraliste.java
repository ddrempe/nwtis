/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.foi.nwtis.damdrempe.rest.klijenti.MeteoRESTKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.MeteoRESTKlijentId;
import org.foi.nwtis.damdrempe.web.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.ProcitaniJsonOdgovor;
import org.foi.nwtis.damdrempe.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste;

/**
 *
 * @author grupa_2
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
     * Creates a new instance of OperacijaParkiraliste
     */
    public OperacijaParkiraliste() {
        preuzmiSvaParkiralistaSOAP();
    }

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

    private void preuzmiSvaParkiralistaSOAP() {
        List<Parkiraliste> svaParkiralista = MeteoWSKlijent.dajSvaParkiralista();
        popisParkiralistaPrikaz = new LinkedHashMap<>();

        for (Parkiraliste parkiraliste : svaParkiralista) {
            popisParkiralistaPrikaz.put(parkiraliste.getNaziv(), parkiraliste.getId());
        }
    }

    public void promjena(ValueChangeEvent e) {
        odabranaParkiralista = (List<String>) e.getNewValue();
        brojOdabranihParkiralista = odabranaParkiralista.size();
    }

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
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.
     *
     * @return odredište za navigaciju
     */
    public String prethodniZapisi() {
        pomak--;
        azurirajPrikazMeteo();

        return "";
    }

    /**
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.
     *
     * @return odredište za navigaciju
     */
    public String sljedeciZapisi() {
        pomak++;
        azurirajPrikazMeteo();

        return "";
    }

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

    public boolean isViseOdabrano() {
        return viseOdabrano;
    }

    public void setViseOdabrano(boolean viseOdabrano) {
        this.viseOdabrano = viseOdabrano;
    }

    public boolean isJedanOdabran() {
        return jedanOdabran;
    }

    public void setJedanOdabran(boolean jedanOdabran) {
        this.jedanOdabran = jedanOdabran;
    }

    public int getBrojOdabranihParkiralista() {
        return brojOdabranihParkiralista;
    }

    public void setBrojOdabranihParkiralista(int brojOdabranihParkiralista) {
        this.brojOdabranihParkiralista = brojOdabranihParkiralista;
    }

    public List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> getMeteopodaciSvih() {
        return meteopodaciSvih;
    }

    public void setMeteopodaciSvih(List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> meteopodaciSvih) {
        this.meteopodaciSvih = meteopodaciSvih;
    }

    public List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> getMeteopodaciPrikaz() {
        return meteopodaciPrikaz;
    }

    public void setMeteopodaciPrikaz(List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> meteopodaciPrikaz) {
        this.meteopodaciPrikaz = meteopodaciPrikaz;
    }

    public int getPomak() {
        return pomak;
    }

    public void setPomak(int pomak) {
        this.pomak = pomak;
    }

    public int getMaksPomak() {
        return maksPomak;
    }

    public void setMaksPomak(int maksPomak) {
        this.maksPomak = maksPomak;
    }

    public int getBrojZapisaZaPrikaz() {
        return brojZapisaZaPrikaz;
    }

    public void setBrojZapisaZaPrikaz(int brojZapisaZaPrikaz) {
        this.brojZapisaZaPrikaz = brojZapisaZaPrikaz;
    }

    public int getUkupnoZapisa() {
        return ukupnoZapisa;
    }

    public void setUkupnoZapisa(int ukupnoZapisa) {
        this.ukupnoZapisa = ukupnoZapisa;
    }

    public int getOdZapisa() {
        return odZapisa;
    }

    public void setOdZapisa(int odZapisa) {
        this.odZapisa = odZapisa;
    }

    public int getDoZapisa() {
        return doZapisa;
    }

    public void setDoZapisa(int doZapisa) {
        this.doZapisa = doZapisa;
    }
    
    

}
