package org.foi.nwtis.matnovak.zadaca_1;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.matnovak.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;

public class ServerSustava {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Krivi broj argumenata!");
            return;
        }

        try {
            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
            ServerSustava ss = new ServerSustava();
            ss.pokreniPosluzitelj(konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }

    private void pokreniPosluzitelj(Konfiguracija konf) { 
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maksCekanje = Integer.parseInt(konf.dajPostavku("maks.broj.zahtjeva.cekanje"));
        int maksDretvi = Integer.parseInt(konf.dajPostavku("maks.broj.radnih.dretvi"));
        String datEvidencije = konf.dajPostavku("datoteka.evidencije.rada");
        int brojDretvi = 0;
        boolean radiDok = true;
        
        //TODO Provjeri i ako postoji učitaj evidenciju rada
        SerijalizatorEvidencije se = new SerijalizatorEvidencije("matnovak - Serijalizator",konf);
        se.start();
        
        try {
            ServerSocket serverSocket = new ServerSocket(port, maksCekanje);   
            while (radiDok) {                
                Socket socket = serverSocket.accept();
                
//                try {//TODO samo za privremeno testiranje
//                    sleep(60000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
//                }
                
                System.out.println("Korisnik se spojio!");
                if(brojDretvi == maksDretvi){
                    //TODO vrati ispravan ERROR odgovor 
                } else {
                    RadnaDretva radnaDretva = new RadnaDretva(socket,"matnovak - dretva "+brojDretvi, konf);
                    brojDretvi++;
                    radnaDretva.start();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
