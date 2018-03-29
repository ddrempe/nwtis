package org.foi.nwtis.damdrempe.zadaca_1;

/**
 * Klasa za rad korisnika unutar sustava.
 * Učitava korisničke parametre te određuje korisničku ulogu.
 * Korisnik može biti administrator ili klijent sustava.
 * @author ddrempetic
 */
public class KorisnikSustava {
    
    String nacinRada;
    String korisnik;
    String lozinka;
    String adresa;
    int port;
    String akcija;
    long n;
    
    boolean administrator = false;    
    String[] args;
    
    /**
     * Određuje ulogu korisnika i predaje mu kontrolu.
     * @param args argumenti proslijeđeni programu
     */
    public static void main(String[] args) {
        KorisnikSustava ks = new KorisnikSustava(args);
        
        if(ks.administrator){
            AdministratorSustava as = new AdministratorSustava(ks.args);
            as.preuzmiKontrolu();
        } else {
            KlijentSustava kls = new KlijentSustava(ks.args);
            kls.preuzmiKontrolu();
        }
    }

    /**
     * Konstruktor za spremanje argumenata u objekt.
     * Poziva i preuzimanje postavki koje su nužne za rad svakog objekta korisnika sustava.
     * @param argumenti argumenti proslijeđeni programu
     */
    public KorisnikSustava(String[] argumenti) {
        this.args = argumenti;
        preuzmiPostavke(this.args);
    }   

    /**
     * Klasa za preuzimanje postavki kod objekta KorisnikaSustava.
     * Poziva u konstruktoru što znači da se koristi kod svake inicijalizacije objekta ove klase.
     * @param argumenti argumenti proslijeđeni programu
     */
    private void preuzmiPostavke(String[] argumenti) {
        nacinRada = argumenti[1];       
        
        if(nacinRada.equals("-k")){
            korisnik = argumenti[2];
            lozinka = argumenti[4];
            adresa = argumenti[6];
            port = Integer.parseInt(argumenti[8]);
            akcija = argumenti[9].replace("--","").toUpperCase();    
            
            administrator = true;
        }
        else if(nacinRada.equals("-s")){
            adresa = argumenti[2];
            port = Integer.parseInt(argumenti[4]);
            n = Integer.parseInt(argumenti[6]);
        }
    }    
}
