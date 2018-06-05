/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;

/**
 *
 * @author ddrempetic
 */
public class RadnaDretva extends Thread {

    private String nazivDretve;
    private Socket socket;
    private Konfiguracija konf;

    /**
     * Konstruktor za spremanje mrezne uticnice, naziva dretve i postavki
     *
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
     */
    @Override
    public void interrupt() {
        super.interrupt();
    }
    
    /**
     * Pokreće se startanjem dretve
     */
    @Override
    public synchronized void start() {
        super.start();
    }
    

    /**
     * Pokreće čitanje komande i njezinu obradu.
     */
    @Override
    public void run() {
        String komanda = procitajKomandu();
        System.out.println("RADNA | Primljena je komanda: " + komanda);
        //obradiKomandu(komanda); //TODO obraditi komandu
        String odgovor = "Ovo je test odgovor u vrijeme " + LocalDateTime.now().toString();
        System.out.println("RADNA | Saljem odgovor: " + odgovor);
        posaljiOdgovor(odgovor);
        ServerSustava.brojDretvi--;       
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
}
