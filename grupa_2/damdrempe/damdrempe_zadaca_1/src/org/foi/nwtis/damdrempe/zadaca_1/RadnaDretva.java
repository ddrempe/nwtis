package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
        try {                       
            String komanda = procitajKomandu();
            String regexAdminKomande = "^KORISNIK ([A-Za-z0-9_,-]{3,10}); LOZINKA ([A-Za-z0-9_,#,!,-]{3,10}); (PAUZA|KRENI|ZAUSTAVI|STANJE);$";         
            OutputStream os = socket.getOutputStream();            
            System.out.println("Dretva: "+ nazivDretve + "Komanda: " + komanda);

            if(provjeriIspravnostKomande(komanda, regexAdminKomande) == false){
                String odgovor = "ERROR 02; sintaksa nije ispravna ili komanda nije dozvoljena";
                os.write(odgovor.getBytes());
                statusKod = 1;
            }
            else{
                os.write("OK".getBytes());
                statusKod = 3;
            }
            os.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private String procitajKomandu() throws IOException{
        InputStream is = socket.getInputStream();
        StringBuffer buffer = new StringBuffer();

        while (true){
            int znak = is.read();
            if(znak == -1){
                break;
            }
            buffer.append((char) znak);
        }
        
        return buffer.toString();
    }

    private synchronized void azurirajEvidencijuRadaServera(){
        long brojZahtjeva = ServerSustava.evidencijaRada.getUkupanBrojZahtjeva();
        long vrijemeRadaRadnihDretvi = ServerSustava.evidencijaRada.getUkupnoVrijemeRadaRadnihDretvi();
        long brojObavljenihSerijalizacija = ServerSustava.evidencijaRada.getBrojObavljenihSerijalizacija();
        
        ServerSustava.evidencijaRada.setUkupanBrojZahtjeva(brojZahtjeva + 1);
        ServerSustava.evidencijaRada.setUkupnoVrijemeRadaRadnihDretvi(vrijemeRadaRadnihDretvi + radnoVrijemeDretve);
        ServerSustava.evidencijaRada.setBrojObavljenihSerijalizacija(brojObavljenihSerijalizacija + SerijalizatorEvidencije.brojObavljenihSerijalizacija);
        
        switch(statusKod){
            //TODO skratiti ove linije
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
    
    private boolean provjeriIspravnostKomande(String komanda, String regularniIzraz){
        Pattern pattern = Pattern.compile(regularniIzraz);
        Matcher m = pattern.matcher(komanda);
        
        return m.matches();
    }
}