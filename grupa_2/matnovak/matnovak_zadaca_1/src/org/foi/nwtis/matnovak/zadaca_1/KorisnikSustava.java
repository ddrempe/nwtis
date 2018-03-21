package org.foi.nwtis.matnovak.zadaca_1;

public class KorisnikSustava {
    
    String korisnik;
    String lozinka;
    String adresa;
    int port;
    String[] args;
    boolean administrator = false;
    
    public static void main(String[] args) {
        //TODO provjeri upisane argumente
        KorisnikSustava ks = new KorisnikSustava();
        ks.preuzmiPostavke(args);
        ks.args = args;
        
        if(ks.administrator){
            //TODO kreiraj objekt AdministratorSustava i predaj mu kontrolu
            AdministratorSustava as = new AdministratorSustava();
            as.preuzmiKontrolu();
        } else {
            //TODO kreiraj objekt KorisnikSustava i predaj mu kontrolu
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
        port = 8000;
        
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
