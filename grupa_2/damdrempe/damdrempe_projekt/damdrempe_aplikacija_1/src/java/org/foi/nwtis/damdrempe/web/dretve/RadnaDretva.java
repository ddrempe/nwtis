/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.net.Socket;
import java.util.regex.Matcher;
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
    private String primljenaKomanda;

    /**
     * Konstruktor za spremanje mrezne uticnice, naziva dretve i postavki
     *
     * @param socket mrezna uticnica
     * @param nazivDretve naziv dretve
     * @param konf postavke procitane iz datoteke
     * @param primljenaKomanda
     */
    public RadnaDretva(Socket socket, String nazivDretve, Konfiguracija konf, String primljenaKomanda) {
        super(nazivDretve);
        this.socket = socket;
        this.nazivDretve = nazivDretve;
        this.konf = konf;
        this.primljenaKomanda = primljenaKomanda;
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
        String komanda = primljenaKomanda;
        System.out.println("RADNA | " + nazivDretve + " | Primljena je komanda: " + komanda);

        String odgovor = obradiKomandu(komanda);
        System.out.println("RADNA | " + nazivDretve + " | Saljem odgovor: " + odgovor);
        PomocnaKlasa.posaljiOdgovor(odgovor, socket);

        ServerSustava.brojDretvi--;
    }   

    /**
     * Provjerava da li je komanda za posluzitelj ili grupu. Sukladno tome
     * pokrece odgovarajucu akciju i vraca odgovor korisniku.
     *
     * @param komanda znakovni niz koji predstavlja komandu od strane korisnika
     */
    private String obradiKomandu(String komanda) {
        String regexPosluzitelj = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); (PAUZA|KRENI|PASIVNO|AKTIVNO|STANI|STANJE|LISTAJ);$";
        String regexPosluziteljDA = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); (DODAJ|AZURIRAJ) ([A-Za-z0-9_,-]{3,10}) ([A-Za-z0-9_,-]{3,10});$";
        String regexPosluziteljPreuzmi = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); PREUZMI ([A-Za-z0-9_,-]{3,10});$";
        String regexGrupa = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); GRUPA (DODAJ|PREKID|KRENI|PAUZA|STANJE);$"; 

        Matcher provjeraPosluzitelj = PomocnaKlasa.provjeriIspravnostKomande(komanda, regexPosluzitelj);        
        Matcher provjeraPosluziteljDA = PomocnaKlasa.provjeriIspravnostKomande(komanda, regexPosluziteljDA);        
        Matcher provjeraPosluziteljPreuzmi = PomocnaKlasa.provjeriIspravnostKomande(komanda, regexPosluziteljPreuzmi);        
        Matcher provjeraGrupa = PomocnaKlasa.provjeriIspravnostKomande(komanda, regexGrupa);

        String odgovor;
        if (provjeraPosluzitelj.matches() == true) {
            odgovor = pozoviOdgovarajucuPosluziteljAkciju(provjeraPosluzitelj.group(3));
        } else if (provjeraPosluziteljDA.matches() == true) {
            odgovor = pozoviOdgovarajucuPosluziteljAkcijuDA(provjeraPosluziteljDA.group(1), provjeraPosluziteljDA.group(3), provjeraPosluziteljDA.group(5), provjeraPosluziteljDA.group(4));
        } else if (provjeraPosluziteljPreuzmi.matches() == true) {
            odgovor = pozoviPreuzmiAkciju(provjeraPosluziteljPreuzmi.group(3));
        } else if (provjeraGrupa.matches() == true) {
            if(ServerSustava.serverKomandePokrenut == false){
                return OdgovoriKomandi.OPCENITO_ERR_KOMANDEGRUPE;
            }

            odgovor = pozoviOdgovarajucuGrupaAkciju(provjeraGrupa.group(3));
        } else {
            odgovor = OdgovoriKomandi.OPCENITO_ERR_SINTAKSA;
        }

        return odgovor;
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
    private String pozoviOdgovarajucuPosluziteljAkciju(String akcija) {
        String odgovor = "";
        switch (akcija) {
            case "PAUZA":
                odgovor = AkcijePosluzitelj.pauza();
                break;
            case "KRENI":
                odgovor = AkcijePosluzitelj.kreni();
                break;
            case "PASIVNO":
                odgovor = AkcijePosluzitelj.pasivno();
                break;
            case "AKTIVNO":
                odgovor = AkcijePosluzitelj.aktivno();
                break;
            case "STANI":
                odgovor = AkcijePosluzitelj.stani();
                break;
            case "STANJE":
                odgovor = AkcijePosluzitelj.stanje();
                break;
            case "LISTAJ":
                odgovor = AkcijePosluzitelj.listaj();
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
    private String pozoviOdgovarajucuPosluziteljAkcijuDA(String korisnickoIme, String akcija, String ime, String prezime) {
        String odgovor = "";
        switch (akcija) {
            case "DODAJ":
                odgovor = AkcijePosluzitelj.dodaj(ime, prezime);
                break;
            case "AZURIRAJ":
                odgovor = AkcijePosluzitelj.azuriraj(korisnickoIme, ime, prezime);
                break;
            default:
                break;
        }
        return odgovor;
    }
    
    private String pozoviPreuzmiAkciju(String korisnik){
        return AkcijePosluzitelj.preuzmi(korisnik);
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
        switch (akcija) {
            case "DODAJ":
                odgovor = AkcijeGrupa.dodaj();
                break;
            case "PREKID":
                odgovor = AkcijeGrupa.prekid();
                break;
            case "KRENI":
                odgovor = AkcijeGrupa.kreni();
                break;
            case "PAUZA":
                odgovor = AkcijeGrupa.pauza();
                break;
            case "STANJE":
                odgovor = AkcijeGrupa.stanje();
                break;
            default:
                break;
        }
        return odgovor;
    }
}