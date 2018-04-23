/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.kontrole.PomocnaKlasa;
import org.foi.nwtis.damdrempe.web.kontrole.Poruka;
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
    private String posebnaMapa;
    private int brojPorukaZaCitanje;

    private void ObradaPoruke(Poruka poruka) {
        //TODO obraditi poruku
    }

    @Override
    public void interrupt() {
        radi = false;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        int brojacObrada = 1;
        while (radi) {
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
                if (!nwtisFolder.exists()) {
                    nwtisFolder.create(Folder.HOLDS_MESSAGES);
                }

                int ukupnoPoruka = folder.getMessageCount();
                System.out.println(brojacObrada + " | Imamo trenutno " + ukupnoPoruka + " poruka u sanducicu!");

                int pocetnaPoruka = 1;
                int zavrsnaPoruka = pocetnaPoruka + brojPorukaZaCitanje - 1;

                while (true) {
                    if (zavrsnaPoruka > ukupnoPoruka) {
                        zavrsnaPoruka = ukupnoPoruka;
                    }

                    System.out.println(brojacObrada + " | Obrada poruka od broja " + pocetnaPoruka + " do broja " + zavrsnaPoruka + ".");
                    Message[] messages = folder.getMessages(pocetnaPoruka, zavrsnaPoruka);

                    for (Message message : messages) {
                        //TODO pretraziti tzv. NWTIS poruke i s njima obavi potrebne radnje
                        //Poruka poruka = PomocnaKlasa.ProcitajPoruku(message);
//                        if (poruka.getVrsta() == Poruka.VrstaPoruka.NWTiS_poruka) {
//                            ObradaPoruke(poruka);
//                        }
                    }

                    pocetnaPoruka = zavrsnaPoruka + 1;
                    zavrsnaPoruka = pocetnaPoruka + brojPorukaZaCitanje - 1;

                    if (pocetnaPoruka >= ukupnoPoruka) {
                        System.out.println(brojacObrada + " | Obradene su sve poruke!");
                        break;
                    }
                }

                folder.close(false);
                store.close();

                System.out.println("KRAJ! Završila obrada: " + (brojacObrada++) + ".");

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
        brojPorukaZaCitanje = Integer.parseInt(k.dajPostavku("mail.numMessagesToRead"));
        posebnaMapa = k.dajPostavku("mail.folderNWTiS");

        super.start();
    }
}
