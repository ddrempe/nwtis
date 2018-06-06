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
    
    public static final String OPCENITO_ERR_BROJDRETVI = "ERROR 700; nema raspolozive radne dretve";
    public static final String OPCENITO_ERR_SINTAKSA = "ERROR 800; sintaksa nije ispravna ili komanda nije dozvoljena";
    
    public static final String POSLUZITELJ_OK_AUTENTIFIKACIJA = "OK 10; ";
    public static final String POSLUZITELJ_ERR_AUTENTIFIKACIJA = "ERR 11; korisnik ne postoji ili je neispravna lozinka";
    
    public static final String POSLUZITELJ_LISTAJ_OK = "OK 10; ";
    public static final String POSLUZITELJ_LISTAJ_ERR = "ERR 17; nema korisnika za dohvacanje";
    
    public static final String POSLUZITELJ_PREUZMI_OK = "OK 10; ";
    public static final String POSLUZITELJ_PREUZMI_ERR = "ERR 11; ne postoji korisnik";
    
    public static final String POSLUZITELJ_DODAJ_OK = "OK 10; ";
    public static final String POSLUZITELJ_DODAJ_ERR = "ERR 10; korisnik vec postoji";
    
    public static final String POSLUZITELJ_AZURIRAJ_OK = "OK 10; ";
    public static final String POSLUZITELJ_AZURIRAJ_ERR = "ERR 10; korisnik za azuriranje ne postoji";
    
}
