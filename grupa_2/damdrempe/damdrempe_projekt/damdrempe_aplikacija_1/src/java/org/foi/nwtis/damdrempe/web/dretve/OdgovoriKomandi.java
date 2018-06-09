/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

/**
 *
 * @author ddrempetic
 */
public class OdgovoriKomandi {
    
    public static final String OPCENITO_ERR_BROJDRETVI = "ERROR 100; nema raspolozive radne dretve";
    public static final String OPCENITO_ERR_SINTAKSA = "ERROR 200; sintaksa nije ispravna ili komanda nije dozvoljena";
    public static final String OPCENITO_ERR_KOMANDEGRUPE = "ERROR 300; primanje komandi grupe je pauzirano";
    public static final String OPCENITO_ERR_ODGOVORSERVISA = "ERROR 400; odgovor FALSE od operacije servisa";
    
    public static final String OPCENITO_OK_AUTENTIFIKACIJA = "OK 10; ";
    public static final String OPCENITO_ERR_AUTENTIFIKACIJA = "ERR 11; korisnik ne postoji ili je neispravna lozinka";
    
    public static final String POSLUZITELJ_LISTAJ_OK = "OK 10; ";
    public static final String POSLUZITELJ_LISTAJ_ERR = "ERR 17; nema korisnika za dohvacanje";
    
    public static final String POSLUZITELJ_PREUZMI_OK = "OK 10; ";
    public static final String POSLUZITELJ_PREUZMI_ERR = "ERR 11; ne postoji korisnik";
    
    public static final String POSLUZITELJ_DODAJ_OK = "OK 10; ";
    public static final String POSLUZITELJ_DODAJ_ERR = "ERR 10; korisnik vec postoji";
    
    public static final String POSLUZITELJ_AZURIRAJ_OK = "OK 10; ";
    public static final String POSLUZITELJ_AZURIRAJ_ERR = "ERR 10; korisnik za azuriranje ne postoji";
    
    public static final String POSLUZITELJ_PAUZA_OK = "OK 10; ";
    public static final String POSLUZITELJ_PAUZA_ERR = "ERR 12; server sustava je vec pauziran, nije aktiviran";
    
    public static final String POSLUZITELJ_KRENI_OK = "OK 10; ";
    public static final String POSLUZITELJ_KRENI_ERR = "ERR 13; server sustava je vec pokrenut, nije pauziran";
    
    public static final String POSLUZITELJ_PASIVNO_OK = "OK 10; ";
    public static final String POSLUZITELJ_PASIVNO_ERR = "ERR 14; server sustava je vec pasivan, nije aktivan";
    
    public static final String POSLUZITELJ_AKTIVNO_OK = "OK 10; ";
    public static final String POSLUZITELJ_AKTIVNO_ERR = "ERR 15; server sustava je vec aktivan, nije pasivan";
    
    public static final String POSLUZITELJ_STANI_OK = "OK 10; ";
    public static final String POSLUZITELJ_STANI_ERR = "ERR 16; server sustava je vec u postupku prekida";
    
    public static final String POSLUZITELJ_STANJE_OK = "OK 11; preuzima sve komande i preuzima meteopodatke";
    public static final String POSLUZITELJ_STANJE_OK2 = "OK 12; preuzima sve komande i NE preuzima meteopodatke";
    public static final String POSLUZITELJ_STANJE_OK3 = "OK 13; preuzima samo posluziteljske komande i preuzima meteopodatke";
    public static final String POSLUZITELJ_STANJE_OK4 = "OK 14; preuzima samo posluziteljske komande i NE preuzima meteopodatke";

    public static final String GRUPA_DODAJ_OK = "OK 20; ";
    public static final String GRUPA_DODAJ_ERR = "ERR 20; grupa se moze registrirati samo kada je deregistrirana";
    
    public static final String GRUPA_PREKID_OK = "OK 20; ";
    public static final String GRUPA_PREKID_ERR = "ERR 21; grupa je vec deregistrirana";
    
    public static final String GRUPA_KRENI_OK = "OK 20; ";
    public static final String GRUPA_KRENI_ERR = "ERR 22; grupa je bila aktivna";
    public static final String GRUPA_KRENI_ERR2 = "ERR 21; grupa ne postoji, deregistrirana je";
    
    public static final String GRUPA_PAUZA_OK = "OK 20; ";
    public static final String GRUPA_PAUZA_ERR = "ERR 23; grupa nije bila aktivna";
    public static final String GRUPA_PAUZA_ERR2 = "ERR 21; grupa ne postoji, deregistrirana je";
    
    public static final String GRUPA_STANJE_OK = "OK 21; grupa je aktivna";
    public static final String GRUPA_STANJE_OK2 = "OK 22; grupa je blokirana";
    public static final String GRUPA_STANJE_OK3 = "OK 23; grupa je registrirana";
    public static final String GRUPA_STANJE_ERR = "ERR 21; grupa ne postoji, deregistrirana je";    
}
