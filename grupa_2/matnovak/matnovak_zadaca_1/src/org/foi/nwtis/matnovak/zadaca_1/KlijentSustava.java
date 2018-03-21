package org.foi.nwtis.matnovak.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KlijentSustava extends KorisnikSustava{
    public void preuzmiKontrolu() {
        try {
            Socket socekt = new Socket(adresa, port);
            
            InputStream is = socekt.getInputStream();
            OutputStream os = socekt.getOutputStream();
            StringBuffer buffer = new StringBuffer();
            //TODO provjeri upisane argumente
            String komanda = "CEKAJ 60;";
            os.write(komanda.getBytes());
            os.flush();
            socekt.shutdownOutput();
            
            while (true) {
                int znak = is.read();
                if(znak == -1){
                    break;
                }
                buffer.append((char) znak);  
            }
                
            System.out.println("Odgovor: "+buffer.toString());
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
