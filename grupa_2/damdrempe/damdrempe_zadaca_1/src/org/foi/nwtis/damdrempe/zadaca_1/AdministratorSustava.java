package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa za korisnika sustava koji ima ulogu administratora.
 * @author ddrempetic
 */
public class AdministratorSustava extends KorisnikSustava {   
    
    /**
     * Koristi konstruktor nadklase za preuzimanje postavki i učitavanje argumenata
     * @param argumenti argumenti proslijeđeni programu
     */
    public AdministratorSustava(String[] argumenti) {
        super(argumenti);
    }
    
    /**
     * Preuzima kontrolu nad serverom sustava.
     * Spaja se na server i šalje mu komandu definiranu pomoću parametara koje je proslijedio korisnik.
     */
    public void preuzmiKontrolu(){  
        try {
            Socket socket = new Socket(adresa, port);            
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            StringBuffer buffer = new StringBuffer();
            
            String komanda = "KORISNIK " + korisnik + "; LOZINKA "+ lozinka + "; " + akcija + ";";
            os.write(komanda.getBytes());
            os.flush();
            socket.shutdownOutput();
            
            while (true){
                int znak = is.read();
                if(znak == -1){
                    break;
                }
                buffer.append((char) znak);
            }            
            System.out.println("Odgovor: " + buffer.toString());
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}