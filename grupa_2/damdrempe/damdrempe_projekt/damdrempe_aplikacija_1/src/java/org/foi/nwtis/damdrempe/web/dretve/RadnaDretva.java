/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;

/**
 *
 * @author ddrempetic
 */
public class RadnaDretva extends Thread {

    private String nazivDretve;
    private Socket socket;
    private Konfiguracija konf;

    /**
     * Konstruktor za spremanje mrezne uticnice, naziva dretve i postavki
     *
     * @param socket mrezna uticnica
     * @param nazivDretve naziv dretve
     * @param konf postavke procitane iz datoteke
     */
    public RadnaDretva(Socket socket, String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.socket = socket;
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    /**
     * Koristi se prilikom prekida izvodenja dretve
     */
    @Override
    public void interrupt() {
        super.interrupt();
    }

    /**
     * Pokreće se startanjem dretve
     */
    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * Pokreće čitanje komande i njezinu obradu.
     */
    @Override
    public void run() {
        String komanda = procitajKomandu();
        System.out.println("RADNA | " + nazivDretve + " | Primljena je komanda: " + komanda);

        String odgovor = obradiKomandu(komanda); //TODO obraditi komandu
        System.out.println("RADNA | " + nazivDretve + " | Saljem odgovor: " + odgovor);
        posaljiOdgovor(odgovor);

        ServerSustava.brojDretvi--;
    }

    /**
     * Čita niz znakova sa konzole koje je poslao korisnik.
     *
     * @return vraća pročitani niz znakova koji predstavlja komandu serveru
     */
    private String procitajKomandu() {
        StringBuffer buffer = new StringBuffer();

        try {
            InputStream is = socket.getInputStream();

            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                buffer.append((char) znak);
            }
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buffer.toString();
    }

    /**
     * Služi za slanje odgovora na konzolu korisnika sustava.
     *
     * @param odgovor tekst odgovora koji se šalje
     */
    private void posaljiOdgovor(String odgovor) {
        try {
            OutputStream os;
            os = socket.getOutputStream();
            os.write(odgovor.getBytes());
            os.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Provjerava da li je komanda za posluzitelj ili grupu. Sukladno tome
     * pokrece odgovarajucu akciju i vraca odgovor korisniku.
     *
     * @param komanda znakovni niz koji predstavlja komandu od strane korisnika
     */
    private String obradiKomandu(String komanda) {
        String regexPosluzitelj = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); (PAUZA|KRENI|PASIVNO|AKTIVNO|STANI|STANJE|LISTAJ|PREUZMI|AUTENTIKACIJA);$";
        String regexPosluziteljDA = "";
        String regexGrupa = "^.*GRUPA.*$"; //TODO provjeriti, treba sadrzavati GRUPA

        Matcher provjeraPosluzitelj = provjeriIspravnostKomande(komanda, regexPosluzitelj);
        Matcher provjeraPosluziteljDA = provjeriIspravnostKomande(komanda, regexPosluziteljDA);
        Matcher provjeraGrupa = provjeriIspravnostKomande(komanda, regexGrupa);

        String korisnik = "admin";  //TODO provjeraPosluzitelj.group(1)
        String lozinka = "123456";  //TODO provjeraPosluzitelj.group(2)
        String akcija = "PREUZMI";   //TODO provjeraPosluzitelj.group(3)
        
        String ime = "Jasen";
        String prezime = "Jasenic";

        if (PomocnaKlasa.autentificirajKorisnika(korisnik, lozinka) == false) {
            return OdgovoriKomandi.POSLUZITELJ_ERR_AUTENTIFIKACIJA;
        }
        //TODO       if (true){ //ako nema ostatka komande
        //            return OdgovoriKomandi.POSLUZITELJ_OK_AUTENTIFIKACIJA;
        //        }

        String odgovor;
        if (provjeraPosluzitelj.matches() == true) {
            odgovor = pozoviOdgovarajucuPosluziteljAkciju(akcija, korisnik);
        } else if (provjeraPosluziteljDA.matches() == true) {
            odgovor = pozoviOdgovarajucuPosluziteljAkcijuDA(akcija, ime, prezime);
        } else if (provjeraGrupa.matches() == true) {
            odgovor = pozoviOdgovarajucuGrupaAkciju(akcija);
        } else {
            odgovor = OdgovoriKomandi.OPCENITO_ERR_SINTAKSA;
        }

        return odgovor;
    }
    
    /**
     * Ispituje znakovni niz prema zadanom regularnom izrazu
     *
     * @param komanda znakovni niz koji predstavlja komandu za server
     * @param regularniIzraz regularni izraz koji komanda mora zadovoljiti
     * @return true ako je komanda ispravna, false ako nije
     */
    private Matcher provjeriIspravnostKomande(String komanda, String regularniIzraz) {
        Pattern pattern = Pattern.compile(regularniIzraz);
        Matcher m = pattern.matcher(komanda);

        return m;
    }
    
    /**
     * Provjerava koju akciju korisnik želi pokrenuti te zaprima odgovore tih
     * akcija. Akcije za ovu metodu se odnose samo na komande poslane za
     * posluzitelj.
     *
     * @param korisnik korisničko ime iz komande za akciju
     * @param lozinka lozinka iz komande za akciju
     * @param akcija vrsta akcija iz komande
     * @return vraća odgovor o statusu izvedbe komande ovisno o akciji
     */
    private String pozoviOdgovarajucuPosluziteljAkciju(String akcija, String korisnik) {
        String odgovor = "";
        //TODO sloziti akcije DODAJ I AUTENTIKACIJA
        switch (akcija) {
            case "PAUZA":
                break;
            case "KRENI":
                break;
            case "PASIVNO":
                break;
            case "AKTIVNO":
                break;
            case "STANI":
                break;
            case "STANJE":
                break;
            case "LISTAJ":
                odgovor = AkcijePosluzitelj.listaj();
                break;
            case "PREUZMI":
                odgovor = AkcijePosluzitelj.preuzmi(korisnik);
                break;
            default:
                break;
        }
        return odgovor;
    }
    
    /**
     * Provjerava koju akciju korisnik želi pokrenuti te zaprima odgovore tih
     * akcija. Akcije za ovu metodu se odnose samo na komande poslane za
     * posluzitelj.
     *
     * @param akcija vrsta akcija iz komande
     * @return vraća odgovor o statusu izvedbe komande ovisno o akciji
     */
    private String pozoviOdgovarajucuPosluziteljAkcijuDA(String akcija, String ime, String prezime) {
        String odgovor = "";
        switch (akcija) {
            case "DODAJ":
                AkcijePosluzitelj.dodaj(ime, prezime);
                break;
            case "AZURIRAJ":
                AkcijePosluzitelj.azuriraj(ime, prezime);
                break;
            default:
                break;
        }
        return odgovor;
    }

    /**
     * Određuje koju akciju za grupu korisnik želi pokrenuti prema njegovoj
     * komandi
     *
     * @param komanda znakovni niz koji predstavlja komandu od strane korisnika
     * @return tekstualni odgovor na zahtjev korisnika pomocu komande
     */
    private String pozoviOdgovarajucuGrupaAkciju(String akcija) {
        String odgovor = "";

        return odgovor;
    }
}