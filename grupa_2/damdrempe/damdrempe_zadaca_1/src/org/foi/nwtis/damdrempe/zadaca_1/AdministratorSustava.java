package org.foi.nwtis.damdrempe.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;


public class AdministratorSustava extends KorisnikSustava {
    Konfiguracija konf;

    public AdministratorSustava(Konfiguracija konf) {
        super();
        this.konf = konf;
    }
    
    public void preuzmiKontrolu(){  
        try {
            Socket socket = new Socket(adresa, port);
            
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            StringBuffer buffer = new StringBuffer();
            
            String komanda = "KORISNIK " + korisnik + "; LOZINKA "+lozinka+"; PAUZA; ";
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
            
            System.out.println(adresa); //TODO nešto
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
