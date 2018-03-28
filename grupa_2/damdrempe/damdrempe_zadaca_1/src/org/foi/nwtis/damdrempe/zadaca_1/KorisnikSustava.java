package org.foi.nwtis.damdrempe.zadaca_1;

public class KorisnikSustava {
    
    String nacinRada;
    String korisnik;
    String lozinka;
    String adresa;
    int port;
    String operacija;
    
    boolean administrator = false;    
    String[] args;
    
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

    public KorisnikSustava(String[] argumenti) {
        this.args = argumenti;
        preuzmiPostavke(this.args);
    }   

    private void preuzmiPostavke(String[] argumenti) {
        nacinRada = argumenti[1];       
        
        if(nacinRada.equals("-k")){
            korisnik = argumenti[2];
            lozinka = argumenti[4];
            adresa = argumenti[6];
            port = Integer.parseInt(argumenti[8]);
            operacija = argumenti[9].replace("--","").toUpperCase();    
            
            administrator = true;
        }
        else if(nacinRada.equals("-s")){
            adresa = argumenti[2];
            port = Integer.parseInt(argumenti[4]);
        }
    }    
}
