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
        //samo ako je deregistrirana može se registrirati
        //moze se registrirati samo ako je deregistrirana
        if (dajStatusGrupe() != org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_DODAJ_ERR;
        }

        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.registrirajGrupu(k.getKorisnickoIme(), k.getLozinka());        
        String odgovor = rezultat ? OdgovoriKomandi.GRUPA_DODAJ_OK : OdgovoriKomandi.OPCENITO_ERR_ODGOVORSERVISA;

        return odgovor;
    }

    public static String prekid() {
        //ako grupa još ne postoji greška, nije registrirana, samo ako je u statusu deregistrirana
        //TODO u svim statusima osim deregistriran se moze deregistrirati??
        if (dajStatusGrupe() == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_PREKID_ERR;
        }

        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.deregistrirajGrupu(k.getKorisnickoIme(), k.getLozinka());
        String odgovor = rezultat ? OdgovoriKomandi.GRUPA_PREKID_OK : OdgovoriKomandi.OPCENITO_ERR_ODGOVORSERVISA;

        return odgovor;
    }

    public static String kreni() {
        org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika status = dajStatusGrupe();
        
        //ako grupa još ne postoji greška
        if (status == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_KRENI_ERR2;
        }
        //ako je grupa već aktivna greška
        else if (status == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.AKTIVAN) {
            return OdgovoriKomandi.GRUPA_KRENI_ERR;
        }         
        
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.aktivirajGrupu(k.getKorisnickoIme(), k.getLozinka());
        String odgovor = rezultat ? OdgovoriKomandi.GRUPA_KRENI_OK : OdgovoriKomandi.OPCENITO_ERR_ODGOVORSERVISA;

        return odgovor;
    }

    public static String pauza() {
        org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika status = dajStatusGrupe();
        
        //ako grupa još ne postoji greška
        if (status == org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.DEREGISTRIRAN) {
            return OdgovoriKomandi.GRUPA_PAUZA_ERR2;
        } 
        //ako grupa još nije aktivna
        else if (status != org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika.AKTIVAN) {
            return OdgovoriKomandi.GRUPA_PAUZA_ERR;
        }        
        
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.blokirajGrupu(k.getKorisnickoIme(), k.getLozinka());

        return OdgovoriKomandi.GRUPA_PAUZA_OK;
    }

    public static String stanje() {
        org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika status = dajStatusGrupe();

        String odgovor = "";
        if (null != status) {
            switch (status) {
                case AKTIVAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK;
                    break;
                case BLOKIRAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK2;
                    break;
                case REGISTRIRAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_OK3;
                    break;
                case DEREGISTRIRAN:
                    odgovor = OdgovoriKomandi.GRUPA_STANJE_ERR;
                    break;
                default:
                    break;
            }
        }

        return odgovor;
    }

    private static org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika dajStatusGrupe() {
        KorisnikPodaci k = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        org.foi.nwtis.damdrempe.ws.klijenti.StatusKorisnika status = ParkiranjeWSKlijent.dajStatusGrupe(k.getKorisnickoIme(), k.getLozinka());

        return status;
    }
}
