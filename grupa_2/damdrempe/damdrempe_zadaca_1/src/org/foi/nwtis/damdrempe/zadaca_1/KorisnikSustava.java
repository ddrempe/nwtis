package org.foi.nwtis.damdrempe.zadaca_1;

public class KorisnikSustava {
    
    String korisnik;
    String lozinka;
    String adresa;
    int port;
    boolean administrator = false;    
    String[] args;
    
    public static void main(String[] args) {
        // TODO provjeri upisane argumente
        KorisnikSustava ks = new KorisnikSustava();
        ks.preuzmiPostavke(args);
        ks.args = args;
    
        if(ks.administrator){
            AdministratorSustava as = new AdministratorSustava();
            as.preuzmiKontrolu();
        } else {
            KlijentSustava kls = new KlijentSustava();
            kls.preuzmiKontrolu();
        }
    }

    public KorisnikSustava() {
        preuzmiPostavke(args);
    }   

    private void preuzmiPostavke(String[] args) {
        //korisnik = "matnovak";
        //lozinka = "123456";
        adresa = "127.0.0.1";
        port=8000;
        
        if(korisnik != null){
            korisnik = korisnik.trim();
            if(!korisnik.isEmpty()){
                administrator = true;
            }
        }
        
        if(lozinka != null) {
            lozinka = lozinka.trim();
            if(!lozinka.isEmpty()){
                administrator = true;
            } else {
                administrator = false;
            }
        } else {
            administrator = false;
        }
        
        //TODO provjeri da li je korisnik kao administrator u postavkama
    }
    
}
