/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.zrna;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.foi.nwtis.damdrempe.web.kontrole.Izbornik;
import org.foi.nwtis.damdrempe.web.kontrole.Poruka;

@Named(value = "pregledPoruka")
@RequestScoped
public class PregledPoruka {
    
    private String posluzitelj;
    private String korIme;
    private String lozinka;
    private List<Izbornik> popisMapa;
    private String odabranaMapa;
    private List<Poruka> popisPoruka;

    public PregledPoruka() {
        // TODO preuzmi adresu poslužitelja, korisničko ime, lozinku, vrijeme spavanja iz postavki
        posluzitelj = "127.0.0.1";
        korIme = "servis@nwtis.nastava.foi.hr";
        lozinka = "123456";

        preuzmiMape();
    }

    private void preuzmiMape() {
        popisMapa = new ArrayList<>();
        popisMapa.add(new Izbornik("INBOX", "INBOX"));
        //TODO provjeri da li postoji trazena mapa u sanducicu prema nazivu iz datoteke postavki
        popisMapa.add(new Izbornik("NWTiS damdrempe poruke", "NWTiS damdrempe poruke"));
    }
    
    private void preuzmiPoruke() {
        popisPoruka = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            popisPoruka.add(new Poruka(Integer.toString(i), new Date(), new Date(), "damdrempe@foi.hr", "Poruka " + i, "{}", Poruka.VrstaPoruka.NWTiS_poruka));
        }
    }

    public String getPosluzitelj() {
        return posluzitelj;
    }

    public void setPosluzitelj(String posluzitelj) {
        this.posluzitelj = posluzitelj;
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public List<Izbornik> getPopisMapa() {
        return popisMapa;
    }

    public void setPopisMapa(List<Izbornik> popisMapa) {
        this.popisMapa = popisMapa;
    }

    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    public List<Poruka> getPopisPoruka() {
        return popisPoruka;
    }

    public void setPopisPoruka(List<Poruka> popisPoruka) {
        this.popisPoruka = popisPoruka;
    }
    
    public String promjeniJezik() {
        return "promjeniJezik";
    }
    
    public String slanjePoruke() {
        return "slanjePoruke";
    }
        
    public String pregledDnevnika() {
        return "pregledDnevnika";
    }
    
}
