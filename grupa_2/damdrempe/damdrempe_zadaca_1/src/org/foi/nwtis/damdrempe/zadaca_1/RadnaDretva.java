package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;

class RadnaDretva extends Thread {

    private Socket socket;
    private String nazivDretve;
    private Konfiguracija konf;

    public RadnaDretva(Socket socket, String nazivDretve, Konfiguracija konf) {
        super(nazivDretve);
        this.socket = socket;
        this.nazivDretve = nazivDretve;
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            StringBuffer buffer = new StringBuffer();
            
            while (true){
                int znak = is.read();
                if(znak == -1){
                    break;
                }
                buffer.append((char) znak);
            }
            
            System.out.println("Dretva: "+ nazivDretve + "Komanda: " + buffer.toString());
            //TODO provjeri ispravnost primljene komande i vrati odgovarajući odgovor
            os.write("OK".getBytes());
            os.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TODO smanji broj aktivnih radnih dretvi kod servera sustava
        //TODO zasto ne radi prvi zahtjev?
    }

    @Override
    public synchronized void start() {
        super.start();
    }    
}