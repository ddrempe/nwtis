package org.foi.nwtis.damdrempe.web.dretve;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.rest.serveri.JsonOdgovor;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 * Klasa koja izvršava potrebne akcije za komande poslužitelja.
 *
 * @author ddrempetic
 */
public class AkcijePosluzitelj {

    /**
     * Dodaje korisnika u bazu podataka.
     *
     * @param ime
     * @param prezime
     * @return odgovor propisane strukture
     */
    public static String dodaj(String ime, String prezime) {
        String odgovor = "";
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (bpo.korisniciSelectImePrezimePostoji(ime, prezime) == true) {
                odgovor = OdgovoriKomandi.POSLUZITELJ_DODAJ_ERR;
            } else {
                Korisnik korisnik = stvoriKorisnika(ime, prezime);
                bpo.korisniciInsert(korisnik);
                odgovor = OdgovoriKomandi.POSLUZITELJ_DODAJ_OK;
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }

        return odgovor;
    }

    /**
     * Ažurira korisnika s danim korisničkim imenom u bazi podataka. Moguće je
     * ažurirati samo ime i prezime.
     *
     * @param korisnickoIme
     * @param ime
     * @param prezime
     * @return odgovor propisane strukture
     */
    public static String azuriraj(String korisnickoIme, String ime, String prezime) {
        String odgovor = "";
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (bpo.korisniciSelectKorimePostoji(korisnickoIme) == false) {
                odgovor = OdgovoriKomandi.POSLUZITELJ_AZURIRAJ_ERR;
            } else {
                bpo.korisniciUpdatePrezimeIme(korisnickoIme, ime, prezime);
                odgovor = OdgovoriKomandi.POSLUZITELJ_AZURIRAJ_OK;
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }

        return odgovor;
    }

    /**
     * Pomoćna metoda koja generira preostale podatke za korisnika temeljem
     * imena i prezimena.
     *
     * @param ime
     * @param prezime
     * @return odgovor propisane strukture
     */
    private static Korisnik stvoriKorisnika(String ime, String prezime) {
        Korisnik korisnik = new Korisnik();
        korisnik.setIme(ime);
        korisnik.setPrezime(prezime);
        if (prezime.length() >= 9) {
            prezime = prezime.substring(0, 9);
        }

        String korisnickoIme = ime.substring(0, 1) + prezime;
        korisnickoIme = korisnickoIme.toLowerCase();
        korisnik.setKor_ime(korisnickoIme);
        korisnik.setLozinka(korisnickoIme);
        korisnik.setEmail_adresa(korisnickoIme + "@foi.hr");

        return korisnik;
    }

    /**
     * Vraća sve korisnike iz baze podataka.
     *
     * @return odgovor propisane strukture
     */
    public static String listaj() {
        List<Korisnik> korisnici = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            korisnici = bpo.korisniciSelectSviKorisnici();
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (korisnici.isEmpty()) {
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_ERR;
        } else {
            JsonOdgovor jsonOdgovor = new JsonOdgovor(true, "");
            JsonArray korisniciJsonDio = jsonOdgovor.postaviSviKorisniciJsonDio(korisnici);
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_OK + korisniciJsonDio.toString();
        }
    }

    /**
     * Vraća podatke jednog korisnika iz baze podataka.
     *
     * @param korisnickoIme
     * @return odgovor propisane strukture
     */
    public static String preuzmi(String korisnickoIme) {
        boolean uspjesno = true;
        
        Korisnik korisnik;
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            korisnik = bpo.korisniciSelectKorimeKorisnik(korisnickoIme);
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            uspjesno = false;
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_ERR;
        }

        JsonOdgovor jsonOdgovor = new JsonOdgovor(true, "");
        JsonArray korisnikJsonDio = jsonOdgovor.postaviKorisnikJsonDio(korisnik);
        return OdgovoriKomandi.POSLUZITELJ_LISTAJ_OK + korisnikJsonDio.toString();
    }

    /**
     * Pauzira zaprimanje komandi posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String pauza() {
        if (ServerSustava.serverKomandePokrenut == false) {
            return OdgovoriKomandi.POSLUZITELJ_PAUZA_ERR;
        }

        ServerSustava.serverKomandePokrenut = false;
        return OdgovoriKomandi.POSLUZITELJ_PAUZA_OK;
    }

    /**
     * Pokrece zaprimanje komandi posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String kreni() {
        if (ServerSustava.serverKomandePokrenut == true) {
            return OdgovoriKomandi.POSLUZITELJ_KRENI_ERR;
        }

        ServerSustava.serverKomandePokrenut = true;
        return OdgovoriKomandi.POSLUZITELJ_KRENI_OK;
    }

    /**
     * Pauzira dretvu za pokretanje meteopodataka.
     *
     * @return odgovor propisane strukture
     */
    public static String pasivno() {
        if (ServerSustava.serverMeteoAktivan == false) {
            return OdgovoriKomandi.POSLUZITELJ_PASIVNO_ERR;
        }

        ServerSustava.serverMeteoAktivan = false;
        return OdgovoriKomandi.POSLUZITELJ_PASIVNO_OK;
    }

    /**
     * Ponovno pokrece dretvu za pokretanje meteopodataka.
     *
     * @return odgovor propisane strukture
     */
    public static String aktivno() {
        if (ServerSustava.serverMeteoAktivan == true) {
            return OdgovoriKomandi.POSLUZITELJ_AKTIVNO_ERR;
        }

        ServerSustava.serverMeteoAktivan = true;
        return OdgovoriKomandi.POSLUZITELJ_AKTIVNO_OK;
    }

    /**
     * Potpuno zaustavlja rad posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String stani() {
        if (ServerSustava.serverPotpunoPokrenut == false) {
            return OdgovoriKomandi.POSLUZITELJ_STANI_ERR;
        }

        ServerSustava.serverPotpunoPokrenut = false;
        return OdgovoriKomandi.POSLUZITELJ_STANI_OK;
    }

    /**
     * Vraca trenutno stanje posluzitelja.
     *
     * @return odgovor propisane strukture
     */
    public static String stanje() {
        String odgovor = "";
        if (ServerSustava.serverKomandePokrenut && ServerSustava.serverMeteoAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK;
        } else if (ServerSustava.serverKomandePokrenut && !ServerSustava.serverMeteoAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK2;
        } else if (!ServerSustava.serverKomandePokrenut && ServerSustava.serverMeteoAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK3;
        } else if (!ServerSustava.serverKomandePokrenut && !ServerSustava.serverMeteoAktivan) {
            odgovor = OdgovoriKomandi.POSLUZITELJ_STANJE_OK4;
        }

        return odgovor;
    }
}
