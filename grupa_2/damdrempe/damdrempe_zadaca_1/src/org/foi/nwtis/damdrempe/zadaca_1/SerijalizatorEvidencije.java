package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;

/**
 * Periodicki serijalizira objekt evidencije rada iz ServerSustava i zapisuje ga u objekt za evidenciju rada.
 * @author ddrempetic
 */
class SerijalizatorEvidencije extends Thread{

    private String nazivDretve;
    private Konfiguracija konf;
    private boolean radiDok = true;    
    public static long brojObavljenihSerijalizacija = 0;
    
    /**
     * Konstruktor koji sprema naziv dretve i konfiguraciju u objekt klase
     * @param nazivDretve naziv dretve koja pokrece serijalizator
     * @param konf postavke pročitane iz datoteke kod pokretanja servera
     */
    SerijalizatorEvidencije(String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    /**
     * Metoda koja se okida kod prekida dretve
     */
    @Override
    public void interrupt() {
        super.interrupt();
    }

    /**
     * Metoda koja se pokrece pokretanjem dretve.
     * Za podatke zapisane u objektu evidencije periodicki poziva zapisivanje u datoteku.
     */
    @Override
    public void run() {
        String nazivDatEvidencije = konf.dajPostavku("datoteka.evidencije.rada");
        int intervalSerijalizacije = Integer.parseInt(konf.dajPostavku("interval.za.serijalizaciju"));
        
        while(radiDok){
            brojObavljenihSerijalizacija++;
            long pocetak = System.currentTimeMillis();
            System.out.println("Dretva: " + nazivDretve + " Početak: " + pocetak);
            
            zapisiEvidencijuRadaUDatoteku(nazivDatEvidencije);            
            long kraj = System.currentTimeMillis();
            long odradeno = kraj - pocetak;
            long cekaj = (intervalSerijalizacije * 1000) - odradeno;
            
            try {
                Thread.sleep(cekaj);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda koja se pokrece startanjem dretve.
     */
    @Override
    public synchronized void start() {
        super.start();
    }   
    
    /**
     * Metoda koja sluzi za serijalizaciju i zapisivanje objekta evidencije u datoteku.
     * @param nazivDatoteke naziv datoteke u koji se zapisuje evidencija rada
     */
    private synchronized void zapisiEvidencijuRadaUDatoteku(String nazivDatoteke){
        File datEvidencije = new File(nazivDatoteke);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(datEvidencije);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);             
            oos.writeObject(ServerSustava.evidencijaRada);
            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
