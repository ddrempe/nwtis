package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;

public class ServerSustava {   

    /**
     * Provjerava postoji li datoteka konfiguracije i prekida rad ako ne postoji.
     * Pokreće metodu pokreniPosluzitelj().
     * 
     * @param args  argument proslijeđen programu, mora biti točno jedan   
     */
    public static void main(String[] args) {
        if(args.length != 1){
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

    /**
     * Učitava postavke iz datoteke konfiguracije.
     * Učitava datoteku evidencije rada sa serijaliziranim podacima ako postoji, inače ispisuje poruku.
     * Naziv datoteke evidencije rada zapisana je u konfiguraciji.
     * 
     * @param konf  - konfiguracija
     */
    private void pokreniPosluzitelj(Konfiguracija konf) {
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maksCekanje = Integer.parseInt(konf.dajPostavku("maks.broj.zahtjeva.cekanje"));
        int maksDretvi = Integer.parseInt(konf.dajPostavku("maks.broj.radnih.dretvi"));
        String nazivDatEvidencije = konf.dajPostavku("datoteka.evidencije.rada");
        int brojDretvi = 0;
        boolean radiDok = true;
        
        File datEvidencije = new File(nazivDatEvidencije);
        Evidencija evidencijaRada = new Evidencija();
        if(!datEvidencije.exists()){
            System.out.println(nazivDatEvidencije + " ne postoji! Datoteka evidencije rada nije ucitana.");
        } else {
            try {            
                evidencijaRada = ucitajEvidencijuRadaIzDatoteke(datEvidencije);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Greska u citanju datoteke! Datoteka: " + nazivDatEvidencije);
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        SerijalizatorEvidencije se = new SerijalizatorEvidencije("damdrempe - Serijalizator", konf, evidencijaRada);
        se.start(); 
        
        //TODO kreira objekt za evidenciju rada i objekt za kolekciju IOT uredaja
        
        try {
            ServerSocket serverSocket = new ServerSocket(port, maksCekanje);
            while (radiDok) { 
                Socket socket = serverSocket.accept();
                
//                try {   //TODO samo za privremeno testiranje
//                    sleep(60000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
//                }
                
                System.out.println("Korisnik se spojio");
                if(brojDretvi == maksDretvi){
                //TODO vrati ispravan error odgovor ERROR 01; nema raspolozive radne dretve
                } else {
                    RadnaDretva radnaDretva = new RadnaDretva(socket, "damdrempe - dretva " + brojDretvi + " ", konf);
                    brojDretvi++;
                    radnaDretva.start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private Evidencija ucitajEvidencijuRadaIzDatoteke(File objektDatoteke) throws FileNotFoundException, IOException, ClassNotFoundException{
         
        FileInputStream fis = new FileInputStream(objektDatoteke);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Evidencija ucitanaEvidencija = (Evidencija) ois.readObject();
        
        return ucitanaEvidencija;
    }
}
