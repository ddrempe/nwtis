/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.podaci.Dnevnik;

/**
 *
 * @author ddrempetic
 */
public class ServerSustava extends Thread {

    /**
     * Pobrojenje koje definira moguca stanja servera
     */
    public enum StanjeServera {
        NISTA,
        POKRENUT,
        PAUZIRAN,
        ZAUSTAVLJEN,
    }

    private ServletContext sc;
    private Konfiguracija konf;
    private boolean radi = false;
    private int redniBrojZadnjeDretve = 0;
    ServerSocket serverSocket;

    public static int brojDretvi = 0;
    public static StanjeServera stanje = StanjeServera.NISTA;

    public ServerSustava(ServletContext sc) {
        this.sc = sc;
    }

    /**
     * Metoda koja se poziva prilikom prekida rada dretve.
     */
    @Override
    public void interrupt() {
        radi = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
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
        konf = (Konfiguracija) sc.getAttribute("Konfig");
        radi = true;
        stanje = StanjeServera.POKRENUT;
        try {
            primajZahtjeve();
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sluša na određenom portu i čeka spajanje korisnika. Ako postoji slobodna
     * radna dretva pokreće ju.
     *
     * @throws IOException
     */
    private void primajZahtjeve() throws IOException {  //TODO refaktorirati radi broja linija
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maksCekanje = Integer.parseInt(konf.dajPostavku("maks.broj.zahtjeva.cekanje"));
        int maksDretvi = Integer.parseInt(konf.dajPostavku("maks.broj.radnih.dretvi"));
        System.out.println("SERVER | Primam zahtjeve na portu " + port);

        serverSocket = new ServerSocket(port, maksCekanje);
        while (radi) {
            Socket socket = serverSocket.accept();  //TODO javlja exception kod deploya
           
            Dnevnik dnevnik = new Dnevnik();            
            boolean nastavi = true;
            System.out.println("SERVER | Stigao zahtjev");

            String komanda = PomocnaKlasa.procitajKomandu(socket);
            String regex = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10});(.*)";
            Matcher matcher = PomocnaKlasa.provjeriIspravnostKomande(komanda, regex);
            String odgovor = "";
            
            if (matcher.matches() == false) {
                System.out.println("SERVER | Neispravna komanda: " + komanda);
                odgovor = OdgovoriKomandi.OPCENITO_ERR_SINTAKSA;
                nastavi = false;
            } else {
                if (PomocnaKlasa.autentificirajKorisnika(matcher.group(1), matcher.group(2)) == false) {
                    System.out.println("SERVER | Nije prosla autentikacija: " + komanda);
                    odgovor = OdgovoriKomandi.OPCENITO_ERR_AUTENTIFIKACIJA;
                    nastavi = false;
                } else {
                    if (matcher.group(3).isEmpty()) {
                        System.out.println("SERVER | Provedena samo autentikacija s uspjehom za komandu: " + komanda);
                        odgovor = OdgovoriKomandi.OPCENITO_OK_AUTENTIFIKACIJA;
                        nastavi = false;
                        dnevnik.postaviUspjesanStatus();
                    }
                }
            } 

            if (nastavi == false) {
                PomocnaKlasa.posaljiOdgovor(odgovor, socket);
                System.out.println("SERVER | Saljem odgovor: " + odgovor);
            } else {
                dnevnik.postaviUspjesanStatus();
                if (brojDretvi >= maksDretvi) {
                    odgovor = OdgovoriKomandi.OPCENITO_ERR_BROJDRETVI;
                    PomocnaKlasa.posaljiOdgovor(odgovor, socket);
                    System.out.println("SERVER | Saljem odgovor: " + odgovor);
                } else {
                    brojDretvi++;
                    povecajRedniBrojZadnjeDretve();
                    RadnaDretva radnaDretva = new RadnaDretva(socket, "damdrempe - dretva " + redniBrojZadnjeDretve + " ", konf, komanda);
                    radnaDretva.start();
                }
            }
            
            dnevnik.zavrsiISpremiDnevnik(matcher.group(1), komanda);
        }
    }

    /**
     * Inkrementira statičku varijablu redniBrojZadnjeDretve unutar
     * ServerSustava. Ako redni broj premaši vrijednost 63, vraća se na nulu i
     * broji ispčetka.
     */
    private void povecajRedniBrojZadnjeDretve() {
        redniBrojZadnjeDretve++;
        if (redniBrojZadnjeDretve > 63) {
            redniBrojZadnjeDretve = 0;
        }
    }
}
