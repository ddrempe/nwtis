package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;

class SerijalizatorEvidencije extends Thread{

    private String nazivDretve;
    private Konfiguracija konf;
    private boolean radiDok = true;

    SerijalizatorEvidencije(String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        String nazivDatEvidencije = konf.dajPostavku("datoteka.evidencije.rada");
        int intervalSerijalizacije = Integer.parseInt(konf.dajPostavku("interval.za.serijalizaciju"));
        
        while(true){
            long pocetak = System.currentTimeMillis();
            System.out.println("Dretva: " + nazivDretve + " Početak: " + pocetak);
            
            File datEvidencije = new File(nazivDatEvidencije);
            ObjectOutputStream oos = null;
            
            try {
                oos = new ObjectOutputStream(new FileOutputStream(datEvidencije));
                //TODO dodaj objekt evidencija rada iz serversustava i serijaliziraj ga
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    oos.close();
                } catch (IOException ex) {
                    Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
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

    @Override
    public synchronized void start() {
        super.start();
    }
    
    
}
