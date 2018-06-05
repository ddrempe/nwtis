/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.pomocno;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

/**
 *
 * @author ddrempetic
 */
public class PomocnaKlasa {
    
    private static String dohvatiPostavku(String naziv){ 
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Konfig");        
        String vrijednost = konf.dajPostavku(naziv);
        
        return vrijednost;
    }
    
    public static String posaljiKomanduPosluzitelju(String komanda){
        String adresa = PomocnaKlasa.dohvatiPostavku("adresa");
        int port = Integer.parseInt(PomocnaKlasa.dohvatiPostavku("port"));
        String odgovor = "";           
        
        try {
            Socket socket = new Socket(adresa, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            StringBuffer buffer = new StringBuffer();
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
            is.close();
            odgovor = buffer.toString();            
        } catch (IOException ex) {
            Logger.getLogger(PomocnaKlasa.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return odgovor;
    }
    
}
