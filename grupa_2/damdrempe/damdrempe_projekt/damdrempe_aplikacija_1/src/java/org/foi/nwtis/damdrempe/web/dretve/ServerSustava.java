package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.pomocno.Dnevnik;

/**
 * Posluzitelj koji zaprima komande na socketu.
 *
 * @author ddrempetic
 */
public class ServerSustava extends Thread {

    private ServletContext sc;
    private Konfiguracija konf;
    private boolean radi = false;
    private int redniBrojZadnjeDretve = 0;
    ServerSocket serverSocket;
    String odgovor;
    boolean nastavi;

    public static int brojDretvi = 0;
    public static boolean serverKomandePokrenut;
    public static boolean serverMeteoAktivan;
    public static boolean serverPotpunoPokrenut;

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
        serverMeteoAktivan = true;
        serverKomandePokrenut = true;
        serverPotpunoPokrenut = true;
        try {
            primajZahtjeve();
        } catch (SocketException ex) {
            System.out.println("SERVER | Zaustavljeno primanje zahtjeva " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("SERVER | IOException: Zaustavljeno primanje zahtjeva " + ex.getMessage());
        }
    }

    /**
     * Sluša na određenom portu i čeka spajanje korisnika. Ako postoji slobodna
     * radna dretva pokreće ju.
     *
     * @throws IOException
     */
    private void primajZahtjeve() throws IOException {
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maksCekanje = Integer.parseInt(konf.dajPostavku("maks.broj.zahtjeva.cekanje"));
        int maksDretvi = Integer.parseInt(konf.dajPostavku("maks.broj.radnih.dretvi"));
        System.out.println("SERVER | Primam zahtjeve na portu " + port);

        serverSocket = new ServerSocket(port, maksCekanje);
        while (radi) {
            Socket socket = serverSocket.accept();
            System.out.println("SERVER | Stigao zahtjev");
            Dnevnik dnevnik = new Dnevnik();
            nastavi = true;
            odgovor = "";

            if (ServerSustava.serverPotpunoPokrenut == false) {
                zavrsiZaustavljanjeServera(socket);
                continue;
            }

            String komanda = PomocnaKlasa.procitajKomandu(socket);
            String regex = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10});(.*)";
            Matcher matcher = PomocnaKlasa.provjeriIspravnostKomande(komanda, regex);

            izvrsiAutentikaciju(matcher, komanda, dnevnik);
            obradiKomandu(socket, dnevnik, maksDretvi, komanda);
            dnevnik.zavrsiISpremiDnevnik(matcher.group(1), komanda);
        }
    }

    /**
     * Prvo provjerava da li se radilo samo o autentikaciji. Ako nije, tada
     * kreira dretvu za obradu komande.
     *
     * @param socket
     * @param dnevnik
     * @param maksDretvi
     * @param komanda
     */
    private void obradiKomandu(Socket socket, Dnevnik dnevnik, int maksDretvi, String komanda) {
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
    }

    /**
     * Obavlja autentikaciju komande. Provjerava prvo samu ispravnost sintakse.
     *
     * @param matcher
     * @param komanda
     * @param dnevnik
     */
    private void izvrsiAutentikaciju(Matcher matcher, String komanda, Dnevnik dnevnik) {
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
    }

    /**
     * Ako je server u postupku zaustavljanja, potrebno je vratiti poruku da je
     * sada potpuno zaustavljen. Prestaje se s slušanjem na socketu.
     *
     * @param socket
     */
    private void zavrsiZaustavljanjeServera(Socket socket) {
        System.out.println("SERVER | Server je POTPUNO ZAUSTAVLJEN - vise ne prima komande. Zavrsavam rad dretve...");
        odgovor = OdgovoriKomandi.POSLUZITELJ_STANI_ERR;
        PomocnaKlasa.posaljiOdgovor(odgovor, socket);
        System.out.println("SERVER | Saljem odgovor: " + odgovor);
        radi = false;
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
