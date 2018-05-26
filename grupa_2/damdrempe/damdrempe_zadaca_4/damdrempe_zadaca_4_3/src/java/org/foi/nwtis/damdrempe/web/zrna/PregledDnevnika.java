package org.foi.nwtis.damdrempe.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.foi.nwtis.damdrempe.ejb.eb.Dnevnik;
import org.foi.nwtis.damdrempe.ejb.sb.DnevnikFacade;

/**
 * Zrno za rad s pregledom dnevnika.
 * @author ddrempetic
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    private String ipAdresa;
    private Date odVrijeme;
    private Date doVrijeme;
    private String adresaZahtjeva;
    private String trajanje;

    private List<Dnevnik> listaDnevnik = new ArrayList<>();

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {

    }

    /**
     * Prilikom otvaranja pregleda preuzima sve zapise.
     */
    @PostConstruct
    public void init() {
        if (listaDnevnik.isEmpty()) {
            preuzmiSve();
        }
    }

    /**
     * Metoda za preuzimanje svih zapisa iz tablcie dnevnika pomoću fasade.
     */
    public void preuzmiSve() {
        listaDnevnik = dnevnikFacade.findAll();
    }

    /**
     * Metoda za preuzimanje zapisa iz tablice dnevnika po odabranim kriterijima.
     * Kriterije korisnik unosi na sučelju.
     */
    public void preuzmiFiltrirano() {
        Integer trajanjeInt = null;
        if(trajanje !=null && !trajanje.isEmpty()){
            trajanjeInt = Integer.parseInt(trajanje);
        }
        listaDnevnik = dnevnikFacade.findFiltered(ipAdresa, trajanjeInt, adresaZahtjeva, odVrijeme, doVrijeme);
    }

    /**
     *
     * @return
     */
    public String getIpAdresa() {
        return ipAdresa;
    }

    /**
     *
     * @param ipAdresa
     */
    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    /**
     *
     * @return
     */
    public Date getOdVrijeme() {
        return odVrijeme;
    }

    /**
     *
     * @param odVrijeme
     */
    public void setOdVrijeme(Date odVrijeme) {
        this.odVrijeme = odVrijeme;
    }

    /**
     *
     * @return
     */
    public Date getDoVrijeme() {
        return doVrijeme;
    }

    /**
     *
     * @param doVrijeme
     */
    public void setDoVrijeme(Date doVrijeme) {
        this.doVrijeme = doVrijeme;
    }   

    /**
     *
     * @return
     */
    public String getAdresaZahtjeva() {
        return adresaZahtjeva;
    }

    /**
     *
     * @param adresaZahtjeva
     */
    public void setAdresaZahtjeva(String adresaZahtjeva) {
        this.adresaZahtjeva = adresaZahtjeva;
    }

    /**
     *
     * @return
     */
    public String getTrajanje() {
        return trajanje;
    }

    /**
     *
     * @param trajanje
     */
    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    /**
     *
     * @return
     */
    public List<Dnevnik> getListaDnevnik() {
        return listaDnevnik;
    }

    /**
     *
     * @param listaDnevnik
     */
    public void setListaDnevnik(List<Dnevnik> listaDnevnik) {
        this.listaDnevnik = listaDnevnik;
    }
}
