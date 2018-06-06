/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;

/**
 *
 * @author ddrempetic
 */
public class ServerSustava extends Thread{    
    
    /**
     * Pobrojenje koje definira moguca stanja servera
     */
    public enum StanjeServera {
        NISTA,
        POKRENUT,
        PAUZIRAN,
        ZAUSTAVLJEN,
    }
    
    private ServletContext sc;
    private Konfiguracija konf;
    private boolean radi = false;    
    private int redniBrojZadnjeDretve = 0;
    ServerSocket serverSocket;
    
    public static int brojDretvi = 0;
    public static StanjeServera stanje = StanjeServera.NISTA;

    public ServerSustava(ServletContext sc) {
        this.sc = sc;
    }
    
    /**
     * Metoda koja se poziva prilikom prekida rada dretve.
     */
    @Override
    public void interrupt() {
        radi = false;
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }
    
    /**
     * Metoda kojom se pokreće dretva.
     */
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.       
    }
    
    /**
     * Metoda koja se izvrši kada je dretva pokrenuta.
     */
    @Override
    public void run() {
        konf = (Konfiguracija) sc.getAttribute("Konfig");       
        radi = true;
        stanje = StanjeServera.POKRENUT;
        try {
            primajZahtjeve();
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sluša na određenom portu i čeka spajanje korisnika.
     * Ako postoji slobodna radna dretva pokreće ju.
     * @throws IOException 
     */
    private void primajZahtjeve() throws IOException{
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maksCekanje = Integer.parseInt(konf.dajPostavku("maks.broj.zahtjeva.cekanje"));
        int maksDretvi = Integer.parseInt(konf.dajPostavku("maks.broj.radnih.dretvi"));
        System.out.println("SERVER | Primam zahtjeve na portu " + port);
        
        serverSocket = new ServerSocket(port, maksCekanje);
        while (radi) {
            Socket socket = serverSocket.accept();  //TODO javlja exception kod deploya
            System.out.println("SERVER | Stigao zahtjev");
            //TODO autentikacija korisnika prema bazi podataka?
            if(brojDretvi >= maksDretvi){
                OutputStream os = socket.getOutputStream();
                String odgovor = OdgovoriKomandi.OPCENITO_ERR_BROJDRETVI;
                os.write(odgovor.getBytes());
                os.flush();
                socket.shutdownOutput();
            } else {
                brojDretvi++;
                povecajRedniBrojZadnjeDretve();
                RadnaDretva radnaDretva = new RadnaDretva(socket, "damdrempe - dretva " + redniBrojZadnjeDretve + " ", konf);
                radnaDretva.start();
            }            
        }
    }
    
    /**
     * Inkrementira statičku varijablu redniBrojZadnjeDretve unutar ServerSustava.
     * Ako redni broj premaši vrijednost 63, vraća se na nulu i broji ispčetka.
     */
    private void povecajRedniBrojZadnjeDretve(){
        redniBrojZadnjeDretve++;
        if(redniBrojZadnjeDretve > 63) redniBrojZadnjeDretve = 0;              
    }
}
