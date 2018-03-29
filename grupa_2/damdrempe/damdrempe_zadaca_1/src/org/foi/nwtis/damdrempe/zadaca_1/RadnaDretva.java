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

/**
 * Radna dretva koja se poziva prilikom svakog spajanja na server i slanja komande.
 * @author ddrempetic
 */
class RadnaDretva extends Thread {

    private String nazivDretve;
    private Socket socket;
    private Konfiguracija konf;
    private long radnoVrijemeDretve;
    
    /**
     * status kod za utvrdivanje statusa zahtjeva
     * 1 - neispravni
     * 2 - nedozvoljeni
     * 3 - uspjesni
     * 4 - prekinuti
     */
    private int statusKod = 0;

    /**
     * Konstruktor za spremanje mrezne uticnice, naziva dretve i postavki
     * @param socket mrezna uticnica
     * @param nazivDretve naziv dretve
     * @param konf postavke procitane iz datoteke
     */
    public RadnaDretva(Socket socket, String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.socket = socket;
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    /**
     * Koristi se prilikom prekida izvodenja dretve
     * Utvrduje da je zahtjev prekinut.
     * Azurira evidenciju rada servera.
     */
    @Override
    public void interrupt() {
        super.interrupt();
        statusKod = 4;
        azurirajEvidencijuRadaServera();
    }

    /**
     * Pokreće čitanje komande i njezinu obradu.
     * Mjeri svoje vrijeme izvođenja te na kraju umanjuje ukupan broj dretvi koje se izvode na sustavu.
     */
    @Override
    public void run() {
        long pocetakRada = System.currentTimeMillis(); 
        
        String komanda = procitajKomandu();
        obradiKomandu(komanda);
            
        long krajRada = System.currentTimeMillis();
        radnoVrijemeDretve = krajRada - pocetakRada;
        ServerSustava.brojDretvi--;
        azurirajEvidencijuRadaServera();       
    }
    
    /**
     * Provjerava da li je komanda od strane klijenta ili administratora.
     * Sukladno tome pokrece odgovarajucu akciju i vraca odgovor korisniku.
     * @param komanda znakovni niz koji predstavlja komandu od strane korisnika
     */
    private void obradiKomandu(String komanda){
        String regexAdminKomande = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); (PAUZA|KRENI|ZAUSTAVI|STANJE);$";
        String regexKlijentKomande = "CEKAJ(.*)";
        System.out.println("Dretva: " + nazivDretve + "Komanda: " + komanda);

        Matcher provjeraAdmin = provjeriIspravnostKomande(komanda, regexAdminKomande);
        Matcher provjeraKlijent = provjeriIspravnostKomande(komanda, regexKlijentKomande);
        
        String odgovor;
        if(provjeraAdmin.matches() == true){
            odgovor = odrediAdminAkcijuIOdgovor(provjeraAdmin.group(1), provjeraAdmin.group(2), provjeraAdmin.group(3));               
            statusKod = 3;
        } 
        else if(provjeraKlijent.matches() == true){
            odgovor = odrediKlijentAkcijuIOdgovor(komanda);
        }
        else{
            odgovor = "ERROR 02; sintaksa nije ispravna ili komanda nije dozvoljena";                
            statusKod = 1;        
        }
        posaljiOdgovor(odgovor);         
    }
    
    /**
     * Određuje koju akciju klijent želi pokrenuti prema njegovoj komandi
     * @param komanda znakovni niz koji predstavlja komandu od strane korisnika
     * @return tekstualni odgovor na zahtjev korisnika pomocu komande
     */
    private String odrediKlijentAkcijuIOdgovor(String komanda){
        String parsiraniBroj = komanda.replace("CEKAJ ", "");
        parsiraniBroj = parsiraniBroj.replace(";", "");
        long n = Integer.parseInt(parsiraniBroj);
        
        String odgovor = "";
        if(ServerSustava.stanje == ServerSustava.StanjeServera.POKRENUT){
            odgovor = akcijaKlijentCekaj(n);
            statusKod = 3;
        }
        else{
            odgovor = "Server je pauziran ili zaustavljen i klijent ne moze izvrsavati naredbe";
            statusKod = 2;
        }
        
        return odgovor;
    }

    /**
     * Pokreće se startanjem dretve
     */
    @Override
    public synchronized void start() {
        super.start();
    }
    
    /**
     * Čita niz znakova sa konzole koje je poslao korisnik.
     * @return vraća pročitani niz znakova koji predstavlja komandu serveru
     */
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
    
    /**
     * Provjerava koju akciju korisnik želi pokrenuti te zaprima odgovore tih akcija.
     * Akcije za ovu metodu se odnose samo na komande poslane od administratora.
     * @param korisnik korisničko ime iz komande za akciju
     * @param lozinka lozinka iz komande za akciju
     * @param akcija vrsta akcija iz komande
     * @return vraća odgovor o statusu izvedbe komande ovisno o akciji
     */
    private String odrediAdminAkcijuIOdgovor(String korisnik, String lozinka, String akcija){
        String odgovor = "PRAZNO";
        
        if(provjeriKorisnickePodatke(korisnik, lozinka) == false){
            statusKod = 2;
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
    
    /**
     * Vrši provjeru za traženo korisničko ime i lozinku.
     * Korisničko ime i lozinka moraju postojati u postavkama koje su zapisane u datoteci.
     * Korisnik mora biti definiran kao administrator i lozinka se mora poklapati.
     * @param korisnik korisničko ime za koje se radi provjera
     * @param lozinka lozinka za korisničko ime
     * @return true ako korisnik zadovoljava sve kriterije, inace false
     */
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
    
    /**
     * Akcija kojom administrator može pauzirati rad servera.
     * Ako je server već pauziran, nije ga moguće ponovno pauzirati.
     * @return odgovarajuću poruku koja se vraća korisniku
     */
    private String akcijaAdminPauza(){
        if(ServerSustava.stanje == ServerSustava.StanjeServera.PAUZIRAN){
            return "ERROR 11; Server je vec u stanju pauze";
        }        
        ServerSustava.stanje = ServerSustava.StanjeServera.PAUZIRAN;
        
        return "OK";
    }
    
    /**
     * Akcija kojom administrator može pokrenuti server.
     * Server je moguće pokrenuti samo ako je pauziran.
     * @return odgovarajuću poruku koja se vraća korisniku
     */
    private String akcijaAdminKreni(){
        if(ServerSustava.stanje != ServerSustava.StanjeServera.PAUZIRAN){
            return "ERROR 12; Server nije u stanju pauze";
        }        
        ServerSustava.stanje = ServerSustava.StanjeServera.POKRENUT;
        
        return "OK";
    }
    
    /**
     * Akcija kojom administrator može zaustaviti daljnji rad servera.
     * @return odgovarajuću poruku koja se vraća korisniku
     */
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
    
    /**
     * Akcija kojom administrator može provjeriti trenutno stanje servera.
     * @return odgovarajuću poruku koja se vraća korisniku
     */
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
    
    /**
     * Akcija kojom klijent pokrece čekanje na serveru.
     * Čekanje traje n sekundi koje klijent zadaje.
     * @param n broj sekundi koje je potrebno čekati
     * @return odgovarajuću poruku koja se vraća korisniku
     */
    private String akcijaKlijentCekaj(long n){
        try {
            Thread.sleep(n * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR 22; Cekanje je prekinuto";
        }
        
        return "OK;";
    }
    
    /**
     * Služi za slanje odgovora na konzolu korisnika sustava.
     * @param odgovor tekst odgovora koji se šalje
     */
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
    
    /**
     * Ispituje znakovni niz prema zadanom regularnom izrazu
     * @param komanda znakovni niz koji predstavlja komandu za server
     * @param regularniIzraz regularni izraz koji komanda mora zadovoljiti
     * @return true ako je komanda ispravna, false ako nije
     */
    private Matcher provjeriIspravnostKomande(String komanda, String regularniIzraz){
        Pattern pattern = Pattern.compile(regularniIzraz);
        Matcher m = pattern.matcher(komanda);
        
        return m;
    }

    /**
     * Azurira vrijednosti unutar objekta evidencije rada kod servera sustava.
     * Uvecava ukupan broje zahtjeva i ukupno radno vrijeme radnih dretvi te broj obavljenih serijalizacija.
     * Ovisno o statusu trenutnog zahtjeva uvecava pripadni broj zahtjeva.
     */
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
}