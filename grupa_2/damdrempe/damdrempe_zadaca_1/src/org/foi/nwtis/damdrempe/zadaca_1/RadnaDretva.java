package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;

class RadnaDretva extends Thread {

    private String nazivDretve;
    private Socket socket;
    private Konfiguracija konf;
    private long radnoVrijemeDretve;
    
    /**
     * 1 - neispravni
     * 2 - nedozvoljeni
     * 3 - uspjesni
     * 4 - prekinuti
     */
    private int statusKod = 0;

    public RadnaDretva(Socket socket, String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.socket = socket;
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        statusKod = 4;
        azurirajEvidencijuRadaServera();
    }

    @Override
    public void run() {
        long pocetakRada = System.currentTimeMillis();                     
        String komanda = procitajKomandu();
        String regexAdminKomande = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); (PAUZA|KRENI|ZAUSTAVI|STANJE);$";         
        System.out.println("Dretva: "+ nazivDretve + "Komanda: " + komanda);

        Matcher provjeraAdmin = provjeriIspravnostKomande(komanda, regexAdminKomande);
        if(provjeraAdmin.matches() == true){
            String odgovor = odrediAkcijuIOdgovor(provjeraAdmin.group(1), provjeraAdmin.group(2), provjeraAdmin.group(3));
            posaljiOdgovor(odgovor);                
            statusKod = 3;
        } 
        else{
            posaljiOdgovor("ERROR 02; sintaksa nije ispravna ili komanda nije dozvoljena");                
            statusKod = 1;        
        }
            
        long krajRada = System.currentTimeMillis();
        radnoVrijemeDretve = krajRada - pocetakRada;
        ServerSustava.brojDretvi--;
        azurirajEvidencijuRadaServera();       
    }

    @Override
    public synchronized void start() {
        super.start();
    }
    
    private String procitajKomandu(){
        StringBuffer buffer = new StringBuffer();

        try {
            InputStream is = socket.getInputStream();            

            while (true){
                int znak = is.read();
                if(znak == -1){
                    break;
                }
                buffer.append((char) znak);
            }        
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return buffer.toString();
    }
    
    private String odrediAkcijuIOdgovor(String korisnik, String lozinka, String akcija){
        String odgovor = "PRAZNO";
        
        if(provjeriKorisnickePodatke(korisnik, lozinka) == false){
            return "ERROR 10; Korisnik nije administrator ili lozinka ne odgovara";
        }
        
        if(akcija.equals("PAUZA")){
            odgovor = akcijaAdminPauza();
        } else if(akcija.equals("KRENI")){
            odgovor = akcijaAdminKreni();
        } else if(akcija.equals("ZAUSTAVI")){
            odgovor = akcijaAdminZaustavi();
        } else if(akcija.equals("STANJE")){
            odgovor = akcijaAdminStanje();
        }
        return odgovor;
    }
    
    private boolean provjeriKorisnickePodatke(String korisnik, String lozinka){        
        Properties konfiguracija = konf.dajSvePostavke();
        for (Enumeration e = konfiguracija.propertyNames(); e.hasMoreElements(); ) {
            String kljuc = (String) e.nextElement();
            String vrijednost = konfiguracija.getProperty(kljuc);
            
            if(kljuc.startsWith("admin")){
                String[] korisnickoIme = kljuc.split("\\.");
                if(korisnickoIme[2].equals(korisnik) && vrijednost.equals(lozinka)){
                    return true;
                }
            }
        }

        return false;
    }
    
    private String akcijaAdminPauza(){
        if(ServerSustava.stanje == ServerSustava.StanjeServera.PAUZIRAN){
            return "ERROR 11; Server je vec u stanju pauze";
        }        
        ServerSustava.stanje = ServerSustava.StanjeServera.PAUZIRAN;
        
        return "OK";
    }
    
    private String akcijaAdminKreni(){
        if(ServerSustava.stanje != ServerSustava.StanjeServera.PAUZIRAN){
            return "ERROR 12; Server nije u stanju pauze";
        }        
        ServerSustava.stanje = ServerSustava.StanjeServera.POKRENUT;
        
        return "OK";
    }
    
    private String akcijaAdminZaustavi(){
        try {
            socket.close();
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR 13; Greska kod prekida rada ili serijalizacije";      
        }

        ServerSustava.stanje = ServerSustava.StanjeServera.ZAUSTAVLJEN;
        return "OK";
    }
    
    private String akcijaAdminStanje(){
        String odgovor = "";
        if(ServerSustava.stanje == ServerSustava.StanjeServera.POKRENUT){
            odgovor = "OK; 0";
        } 
        if(ServerSustava.stanje == ServerSustava.StanjeServera.PAUZIRAN){
            odgovor = "OK; 1";
        } 
        else if (ServerSustava.stanje == ServerSustava.StanjeServera.ZAUSTAVLJEN){
            odgovor = "OK; 2";
        }
        
        return odgovor;
    }
    
    private void posaljiOdgovor(String odgovor){
        try {
            OutputStream os;
            os = socket.getOutputStream();
            os.write(odgovor.getBytes());
            os.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void azurirajEvidencijuRadaServera(){
        long bz = ServerSustava.evidencijaRada.getUkupanBrojZahtjeva();                 //brojZahtjeva
        long vrrd = ServerSustava.evidencijaRada.getUkupnoVrijemeRadaRadnihDretvi();    //vrijemeRadaRadnihDretvi
        long bos = ServerSustava.evidencijaRada.getBrojObavljenihSerijalizacija();      //brojObavljenihSerijalizacija
        
        ServerSustava.evidencijaRada.setUkupanBrojZahtjeva(bz + 1);
        ServerSustava.evidencijaRada.setUkupnoVrijemeRadaRadnihDretvi(vrrd + radnoVrijemeDretve);
        ServerSustava.evidencijaRada.setBrojObavljenihSerijalizacija(bos + SerijalizatorEvidencije.brojObavljenihSerijalizacija);
        
        switch(statusKod){
            case 1:
                ServerSustava.evidencijaRada.setBrojNeispravnihZahtjeva(ServerSustava.evidencijaRada.getBrojNeispravnihZahtjeva()+ 1);
                break;
            case 2:
                ServerSustava.evidencijaRada.setBrojNedozvoljenihZahtjeva(ServerSustava.evidencijaRada.getBrojNedozvoljenihZahtjeva()+ 1);
                break;
            case 3:
                ServerSustava.evidencijaRada.setBrojUspjesnihZahtjeva(ServerSustava.evidencijaRada.getBrojUspjesnihZahtjeva()+ 1);
                break;
            case 4:
                ServerSustava.evidencijaRada.setBrojPrekinutihZahtjeva(ServerSustava.evidencijaRada.getBrojPrekinutihZahtjeva()+ 1);
                break;                
        }
    }
    
    private Matcher provjeriIspravnostKomande(String komanda, String regularniIzraz){
        Pattern pattern = Pattern.compile(regularniIzraz);
        Matcher m = pattern.matcher(komanda);
        
        return m;
    }
}