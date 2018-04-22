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
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.slusaci.SlusacAplikacije;

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
    private int brojMailovaZaCitanje;
    private String posebnaMapa;
    
    @Override
    public void interrupt() {
        radi = false;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        int broj=0;
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
                
                Folder nwtisFolder = store.getFolder(posebnaMapa);
                if(!nwtisFolder.exists()){
                    nwtisFolder.create(Folder.HOLDS_MESSAGES);
                }                
                
                Message[] messages = null;
                
                //TODO ne dohvacati sve poruke odjednom nego ih po grupama dohvatiti
                //npr 10 po 10
                int trenutnaStranica = 0;
                int startniBroj = trenutnaStranica * brojMailovaZaCitanje + 1;
                int zavrsniBroj = startniBroj + brojMailovaZaCitanje - 1;
                messages = folder.getMessages(startniBroj, zavrsniBroj);
                System.out.println("Imamo trenutno "+ messages.length + " poruka u sanducicu!");
                for(int i=0; i<messages.length;i++){
                    //TODO pretraziti tzv. NWTIS poruke i s njima obavi potrebne radnje
                }
                
                folder.close(false);
                store.close();
                
                System.out.println("Završila iteracija: "+ (broj++) + "!");
                
                sleep(spavanje);
            } catch (MessagingException | InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }
    }

    @Override
    public synchronized void start() {
        ServletContext sc = SlusacAplikacije.servletContext;
        Konfiguracija k = (Konfiguracija) sc.getAttribute("MAIL_Konfig");       
        posluzitelj = k.dajPostavku("mail.server");
        korIme = k.dajPostavku("mail.usernameThread");
        lozinka = k.dajPostavku("mail.passwordThread");
        spavanje = Integer.parseInt(k.dajPostavku("mail.timeSecThreadCycle")) * 1000;
        brojMailovaZaCitanje = Integer.parseInt(k.dajPostavku("mail.numMessagesToRead"));
        posebnaMapa = k.dajPostavku("mail.folderNWTiS");
        
        super.start();
    }   
}
