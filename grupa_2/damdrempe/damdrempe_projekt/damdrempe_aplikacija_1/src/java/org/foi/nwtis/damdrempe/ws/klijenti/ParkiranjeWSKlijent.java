/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ws.klijenti;

/**
 *
 * @author ddrempetic
 */
public class ParkiranjeWSKlijent {

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static Boolean aktivirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.aktivirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param odabranaParkiralista
     * @return
     */
    public static boolean aktivirajOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.aktivirajOdabranaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, odabranaParkiralista);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param idParkiraliste
     * @return
     */
    public static boolean aktivirajParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.aktivirajParkiralisteGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static Boolean autenticirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.autenticirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static Boolean blokirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.blokirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param odabranaParkiralista
     * @return
     */
    public static boolean blokirajOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.blokirajOdabranaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, odabranaParkiralista);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param idParkiraliste
     * @return
     */
    public static boolean blokirajParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.blokirajParkiralisteGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static int dajBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajBrojPoruka(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static StatusKorisnika dajStatusGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajStatusGrupe(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param idParkiraliste
     * @return
     */
    public static StatusParkiralista dajStatusParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajStatusParkiralistaGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static java.util.List<org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste> dajSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajSvaParkiralistaGrupe(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static int dajTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajTrajanjeCiklusa(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static Boolean deregistrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.deregistrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param idParkiraliste
     * @param nazivParkiraliste
     * @param adresaParkiraliste
     * @param kapacitetParkiraliste
     * @return
     */
    public static Boolean dodajNovoParkiralisteGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste, java.lang.String nazivParkiraliste, java.lang.String adresaParkiraliste, int kapacitetParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dodajNovoParkiralisteGrupi(korisnickoIme, korisnickaLozinka, idParkiraliste, nazivParkiraliste, adresaParkiraliste, kapacitetParkiraliste);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param idParkiraliste
     * @return
     */
    public static Boolean dodajParkiralisteGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dodajParkiralisteGrupi(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param odabranaParkiralista
     * @return
     */
    public static boolean obrisiOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.obrisiOdabranaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, odabranaParkiralista);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param idParkiraliste
     * @return
     */
    public static boolean obrisiParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.obrisiParkiralisteGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static Boolean obrisiSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.obrisiSvaParkiralistaGrupe(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param brojPoruka
     */
    public static void promjeniBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int brojPoruka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        port.promjeniBrojPoruka(korisnickoIme, korisnickaLozinka, brojPoruka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param trajanjeCiklusa
     */
    public static void promjeniTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int trajanjeCiklusa) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        port.promjeniTrajanjeCiklusa(korisnickoIme, korisnickaLozinka, trajanjeCiklusa);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static Boolean registrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.registrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @return
     */
    public static boolean ucitajSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.ucitajSvaParkiralistaGrupe(korisnickoIme, korisnickaLozinka);
    }

    /**
     *
     * @param korisnickoIme
     * @param korisnickaLozinka
     * @param idParkiraliste
     * @return
     */
    public static java.util.List<org.foi.nwtis.damdrempe.ws.klijenti.Vozilo> dajSvaVozilaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajSvaVozilaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }   
}
