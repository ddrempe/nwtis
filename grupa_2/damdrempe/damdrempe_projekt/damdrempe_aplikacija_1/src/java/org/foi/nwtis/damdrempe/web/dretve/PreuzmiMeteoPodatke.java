package org.foi.nwtis.damdrempe.web.dretve;

import static java.lang.Thread.sleep;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

/**
 * Dretva koja u pozadini dohvaća sva parkirališta iz baze podataka, dohvaća za
 * svako od njih meteorološke podatke. Dohvaćene meteorološke podatke sprema u
 * bazu podataka. Izvršava se periodički za interval u konfiguraciji.
 *
 * @author ddrempetic
 */
public class PreuzmiMeteoPodatke extends Thread {

    private int spavanje;
    private boolean radi = false;
    private int brojacObrada = 1;
    private BazaPodatakaOperacije bpo;
    private ServletContext sc;

    /**
     * Konstruktor klase koji prima kontekst servleta.
     *
     * @param sc
     */
    public PreuzmiMeteoPodatke(ServletContext sc) {
        this.sc = sc;
    }

    /**
     * Metoda koja se poziva prilikom prekida rada dretve.
     */
    @Override
    public void interrupt() {
        radi = false;
        try {
            bpo.zatvoriVezu();
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }

    /**
     * Metoda kojom se pokreće dretva.
     */
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.       
    }

    /**
     * Metoda koja se izvrši kada je dretva pokrenuta.
     */
    @Override
    public void run() {
        Konfiguracija k = (Konfiguracija) sc.getAttribute("Konfig");
        spavanje = Integer.parseInt(k.dajPostavku("intervalDretveZaMeteoPodatke")) * 1000;

        try {
            bpo = new BazaPodatakaOperacije(sc);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        radi = true;
        radiPosao();
    }

    /**
     * Provjerava da li treba dohvacati meteopodatke.
     * Ako treba dohvati ih i spava, inace spava do sljedece provjere.
     */
    private void radiPosao() {
        while (radi) {
            if(ServerSustava.serverPotpunoPokrenut == false){
                System.out.println("METEO | Server je POTPUNO ZAUSTAVLJEN - vise ne preuzima meteopodatke. Zavrsavam rad dretve...");
                radi = false;
                continue;
            }
            
            try {
                if(ServerSustava.serverMeteoAktivan == true){
                    preuzmiMeteoPodatkeSaServisa();
                }
                else {
                    System.out.println("METEO | Preuzimanje meteopodataka je u statusu PASIVNO");
                }
                
                sleep(spavanje);
            } catch (InterruptedException ex) {
                System.out.println("METEO | Dretva je zaustavljena prilikom spavanja");
            } catch (SQLException ex) {
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Dohvaća listu svih parkirališta iz baze podataka Dohvaća meteopodatke za
     * svakog od njih preko web servisa. Upisuje meteopodatke u bazu podataka.
     */
    private void preuzmiMeteoPodatkeSaServisa() throws SQLException {
        ArrayList<Parkiraliste> dohvacenaParkiralista = bpo.parkiralistaSelectSvaParkiralista();
        for (Parkiraliste parkiraliste : dohvacenaParkiralista) {
            MeteoPodaci meteo = PomocnaKlasa.dohvatiOWMMeteo(parkiraliste.getGeoloc().getLatitude(), parkiraliste.getGeoloc().getLongitude());
            if (meteo != null) {
                bpo.meteoInsert(meteo, parkiraliste);
            } else {
                System.out.println("METEO | Nije moguce dohvatiti trenutne meteopodatke za parkiraliste " + parkiraliste.getNaziv() + " na adresi " + parkiraliste.getAdresa());
            }
        }

        System.out.println("METEO | KRAJ! Završila obrada: " + (brojacObrada++) + ".");
    }
}
