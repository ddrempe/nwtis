/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author grupa_2
 */
public class ObradaPoruka extends Thread {
    
    private String posluzitelj;
    private String korIme;
    private String lozinka;
    private int spavanje;
    private boolean radi = true;
    
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        while(radi){
            try {
                String defaultFrom;
                Session session;
                Store store;
                Folder folder;

                // Start the session
                java.util.Properties properties = System.getProperties();
                properties.put("mail.smtp.host", posluzitelj);
                session = Session.getInstance(properties, null);

                // Connect to the store
                store = session.getStore("imap");
                store.connect(posluzitelj, korIme, lozinka);

                // Open the INBOX folder
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                
                Message[] messages = null;
                
                //TODO ne dohvacati sve poruke odjednom nego ih po grupama dohvatiti
                //npr 10 po 10
                messages = folder.getMessages();
                
                for(int i=0; i<messages.length;i++){
                    //TODO pretraziti tzv. NWTIS poruke i s njima obavi potrebne radnje
                }
                
                folder.close(false);
                store.close();
                
                //TODO korigiraj vrijeme spavanja za rad ciklusa
                sleep(spavanje);
            } catch (MessagingException | InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }
    }

    @Override
    public synchronized void start() {
        //TODO preuzmi podatke iz postavki
        posluzitelj = "127.0.0.1";
        korIme = "servis@nwtis.nastava.foi.hr";
        lozinka = "123456";
        spavanje = 30 * 1000;
        super.start();
    }   
}
