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

    public static Boolean aktivirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.aktivirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static boolean aktivirajOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.aktivirajOdabranaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, odabranaParkiralista);
    }

    public static boolean aktivirajParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.aktivirajParkiralisteGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    public static Boolean autenticirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.autenticirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean blokirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.blokirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static boolean blokirajOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.blokirajOdabranaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, odabranaParkiralista);
    }

    public static boolean blokirajParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.blokirajParkiralisteGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    public static int dajBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajBrojPoruka(korisnickoIme, korisnickaLozinka);
    }

    public static StatusKorisnika dajStatusGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajStatusGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static StatusParkiralista dajStatusParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajStatusParkiralistaGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    public static java.util.List<org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste> dajSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajSvaParkiralistaGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static int dajTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajTrajanjeCiklusa(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean deregistrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.deregistrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean dodajNovoParkiralisteGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste, java.lang.String nazivParkiraliste, java.lang.String adresaParkiraliste, int kapacitetParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dodajNovoParkiralisteGrupi(korisnickoIme, korisnickaLozinka, idParkiraliste, nazivParkiraliste, adresaParkiraliste, kapacitetParkiraliste);
    }

    public static Boolean dodajParkiralisteGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dodajParkiralisteGrupi(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    public static boolean obrisiOdabranaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabranaParkiralista) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.obrisiOdabranaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, odabranaParkiralista);
    }

    public static boolean obrisiParkiralisteGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.obrisiParkiralisteGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }

    public static Boolean obrisiSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.obrisiSvaParkiralistaGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static void promjeniBrojPoruka(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int brojPoruka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        port.promjeniBrojPoruka(korisnickoIme, korisnickaLozinka, brojPoruka);
    }

    public static void promjeniTrajanjeCiklusa(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int trajanjeCiklusa) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        port.promjeniTrajanjeCiklusa(korisnickoIme, korisnickaLozinka, trajanjeCiklusa);
    }

    public static Boolean registrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.registrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static boolean ucitajSvaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.ucitajSvaParkiralistaGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static java.util.List<org.foi.nwtis.damdrempe.ws.klijenti.Vozilo> dajSvaVozilaParkiralistaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idParkiraliste) {
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service service = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje_Service();
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiranje port = service.getParkiranjePort();
        return port.dajSvaVozilaParkiralistaGrupe(korisnickoIme, korisnickaLozinka, idParkiraliste);
    }   
}
