package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;

/**
 * Klasa za pokretanje serverskog dijela sustava.
 * @author ddrempetic
 */
public class ServerSustava {
    
    /**
     * Pobrojenje koje definira moguca stanja servera
     */
    public enum StanjeServera {
        NISTA,
        POKRENUT,
        PAUZIRAN,
        ZAUSTAVLJEN,
    }
    
    public static Evidencija evidencijaRada;
    public static int brojDretvi = 0;
    private static int redniBrojZadnjeDretve = 0;
    private boolean radiDok = true;
    private static Konfiguracija konf;
    public static StanjeServera stanje = StanjeServera.NISTA;
    
    /**
     * Provjerava postoji li datoteka konfiguracije i prekida rad ako ne postoji.
     * Pokreće metodu pokreniPosluzitelj().
     * 
     * @param args  argument proslijeđen programu, mora biti točno jedan   
     */
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Krivi broj argumenata!");
            return;
        }        

        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[1]);
            ServerSustava ss = new ServerSustava();
            ss.pokreniPosluzitelj(konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
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
        String nazivDatEvidencije = konf.dajPostavku("datoteka.evidencije.rada");
        
        try {
            if(ucitajEvidencijuRadaIzDatoteke(nazivDatEvidencije) == false){
                System.out.println(nazivDatEvidencije + " ne postoji! Datoteka evidencije rada nije ucitana.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Greska u citanju datoteke! Datoteka: " + nazivDatEvidencije);
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        IOT iot = new IOT();        
        SerijalizatorEvidencije se = new SerijalizatorEvidencije("damdrempe - Serijalizator", konf);
        se.start();        
        stanje = StanjeServera.POKRENUT;

        try {
            primajZahtjeveKorisnika();
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * Provjerava da li datoteka evidencije s traženim nazivom postoji.
     * Ako postoji čita vrijednosti iz nje i zapisuje u statični objekt klase Evidencija unutar ServerSustava.
     * @param nazivDatoteke naziv datoteke iz koje treba učitati evidenciju rada
     * @return vraća true ako datoteka s traženim nazivom postoji, inače vraća false
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private boolean ucitajEvidencijuRadaIzDatoteke(String nazivDatoteke) throws FileNotFoundException, IOException, ClassNotFoundException{        
        File objektDatoteke = new File(nazivDatoteke);        
        if(!objektDatoteke.exists()){
            return false;
        } 
        
        FileInputStream fis = new FileInputStream(objektDatoteke);
        ObjectInputStream ois = new ObjectInputStream(fis);
        evidencijaRada = (Evidencija) ois.readObject();

        return true;
    }
    
    /**
     * Inkrementira statičku varijablu redniBrojZadnjeDretve unutar ServerSustava.
     * Ako redni broj premaši vrijednost 63, vraća se na nulu i broji ispčetka.
     */
    private void povecajRedniBrojZadnjeDretve(){
        redniBrojZadnjeDretve++;
        if(redniBrojZadnjeDretve > 63) redniBrojZadnjeDretve = 0;              
    }
    
    /**
     * Sluša na određenom portu i čeka spajanje korisnika.
     * Ako postoji slobodna radna dretva pokreće ju.
     * @throws IOException 
     */
    private void primajZahtjeveKorisnika() throws IOException{
        int port = Integer.parseInt(konf.dajPostavku("port"));
        int maksCekanje = Integer.parseInt(konf.dajPostavku("maks.broj.zahtjeva.cekanje"));
        int maksDretvi = Integer.parseInt(konf.dajPostavku("maks.broj.radnih.dretvi"));
        
        ServerSocket serverSocket = new ServerSocket(port, maksCekanje);
        while (radiDok) { 
            Socket socket = serverSocket.accept();
            System.out.println("Korisnik se spojio");
            if(brojDretvi >= maksDretvi){
                OutputStream os = socket.getOutputStream();
                String odgovor = "ERROR 01; nema raspolozive radne dretve";
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
}
