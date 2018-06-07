/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import org.foi.nwtis.damdrempe.pomocno.KorisnikPodaci;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.ws.klijenti.ParkiranjeWSKlijent;

/**
 *
 * @author ddrempetic
 */
public class AkcijeGrupa {

    public static String dodaj() {
        //ako grupa već postoji greška
        //TODO ne znam da li je dobro i da li je ikad u stanju NEPOSTOJI
        if (dajStanjeGrupe() != org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.NEPOSTOJI) {
            return OdgovoriKomandi.GRUPA_DODAJ_ERR;
        }

        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.registrirajGrupu(k.getKorisnickoIme(), k.getLozinka());

        return OdgovoriKomandi.GRUPA_DODAJ_OK;
    }

    public static String prekid() {
        //ako grupa još ne postoji greška
        //TODO ne znam da li je dobro i da li je ikad u stanju NEPOSTOJI
        if (dajStanjeGrupe() == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.NEPOSTOJI) {
            return OdgovoriKomandi.GRUPA_PREKID_ERR;
        }

        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.deregistrirajGrupu(k.getKorisnickoIme(), k.getLozinka());

        return OdgovoriKomandi.GRUPA_PREKID_OK;
    }

    public static String kreni() {
        //ako grupa još ne postoji greška
        if (dajStanjeGrupe() == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.NEPOSTOJI) {
            return OdgovoriKomandi.GRUPA_KRENI_ERR2;
        }
        //ako je grupa već aktivna greška
        else if (dajStanjeGrupe() == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.AKTIVAN) {
            return OdgovoriKomandi.GRUPA_KRENI_ERR;
        }         
        
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.aktivirajGrupu(k.getKorisnickoIme(), k.getLozinka());

        return OdgovoriKomandi.GRUPA_KRENI_OK;
    }

    public static String pauza() {
        //ako grupa još ne postoji greška
        if (dajStanjeGrupe() == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.NEPOSTOJI) {
            return OdgovoriKomandi.GRUPA_PAUZA_ERR2;
        } 
        //ako grupa još nije aktivna
        else if (dajStanjeGrupe() != org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.AKTIVAN) {
            return OdgovoriKomandi.GRUPA_PAUZA_ERR;
        }        
        
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.blokirajGrupu(k.getKorisnickoIme(), k.getLozinka());

        return OdgovoriKomandi.GRUPA_PAUZA_OK;
    }

    public static String stanje() {
        org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika status = dajStanjeGrupe();

        String odgovor = "";
        if (null != status) {
            switch (status) {
                case AKTIVAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK;
                    break;
                case BLOKIRAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK2;
                    break;
                case NEPOSTOJI:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_ERR;
                    break;
                default:
                    break;
            }
        }

        return odgovor;
    }

    private static org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika dajStanjeGrupe() {
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika status = ParkiranjeWSKlijent.dajStatusGrupe(k.getKorisnickoIme(), k.getLozinka());

        return status;
    }
}
