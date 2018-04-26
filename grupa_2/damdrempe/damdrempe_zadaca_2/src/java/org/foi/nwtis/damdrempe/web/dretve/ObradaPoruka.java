/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.web.kontrole.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.web.kontrole.Komanda;
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
    private String trazeniNazivPrivitka;
    private BazaPodatakaOperacije bpo;

    private void ObradaNwtisPoruke(Poruka poruka) throws SQLException {        
        String tekstPrivitka = poruka.getPrivitak();
        Komanda komanda = PomocnaKlasa.ParsirajJsonKomande(tekstPrivitka);
        if(komanda.komanda.equalsIgnoreCase("dodaj")){
            if(!bpo.UredajiInsert(komanda, tekstPrivitka)){
                System.out.println("Nije moguc INSERT. Vec postoji uredaj s id: " + komanda.id);
            }
        }
        else if(komanda.komanda.equalsIgnoreCase("azuriraj")){
            if(!bpo.UredajiUpdate(komanda, tekstPrivitka)){
                System.out.println("Nije moguc UPDATE. Ne postoji uredaj s id: " + komanda.id);
            }
        } 
        else {
            System.out.println("Neispravna komanda: " + komanda.komanda);
        }
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
                bpo = new BazaPodatakaOperacije();
                
                String defaultFrom;
                Session session;
                Store store;
                Folder folderInbox;

                // Start the session
                java.util.Properties properties = System.getProperties();
                properties.put("mail.smtp.host", posluzitelj);
                session = Session.getInstance(properties, null);

                // Connect to the store
                store = session.getStore("imap");
                store.connect(posluzitelj, korIme, lozinka);

                // Open the INBOX folderInbox
                folderInbox = store.getFolder("INBOX");
                folderInbox.open(Folder.READ_ONLY);

                Folder folderNwtis = store.getFolder(posebnaMapa);
                if (!folderNwtis.exists()) {
                    folderNwtis.create(Folder.HOLDS_MESSAGES);
                }

                int ukupnoPoruka = folderInbox.getMessageCount();
                System.out.println(brojacObrada + " | Imamo trenutno " + ukupnoPoruka + " poruka u sanducicu!");

                int pocetnaPoruka = 1;
                int zavrsnaPoruka = pocetnaPoruka + brojPorukaZaCitanje - 1;

                ArrayList<Message> porukeZaPrebacivanje = new ArrayList<>();
                
                while (true) {
                    if (zavrsnaPoruka > ukupnoPoruka) {
                        zavrsnaPoruka = ukupnoPoruka;
                    }

                    System.out.println(brojacObrada + " | Obrada poruka od broja " + pocetnaPoruka + " do broja " + zavrsnaPoruka + ".");
                    Message[] messages = folderInbox.getMessages(pocetnaPoruka, zavrsnaPoruka);                   
                    
                    for (Message message : messages) {
                        Poruka poruka = PomocnaKlasa.ProcitajPoruku(message, trazeniNazivPrivitka);
                        if (poruka.getVrsta() == Poruka.VrstaPoruka.NWTiS_poruka) {
                            ObradaNwtisPoruke(poruka);
                            porukeZaPrebacivanje.add(message);
                                    
                            //TODO dodaj u dnevnik                          
                        }
                    }
                    
                    pocetnaPoruka = zavrsnaPoruka + 1;
                    zavrsnaPoruka = pocetnaPoruka + brojPorukaZaCitanje - 1;

                    if (pocetnaPoruka >= ukupnoPoruka) {
                        System.out.println(brojacObrada + " | Obradene su sve poruke!");
                        break;
                    }
                }
                
//                for (Message message : porukeZaPrebacivanje) {
//                        //TODO baca exception kada postoje neobradene poruke kod prvog deploya tj. pokretanja?
//                      System.out.println(brojacObrada + " | Poruka premjestena u posebnu mapu.");
//
//                      Message[] poruke = new Message[1];
//                      poruke[0] = message;
//                      folderInbox.copyMessages(poruke, folderNwtis);
//                      message.setFlag(Flags.Flag.DELETED, true);
//                      folderInbox.expunge();  
//                }

                folderInbox.close(false);
                store.close();
                
                bpo.ZatvoriVezu();

                System.out.println("KRAJ! Završila obrada: " + (brojacObrada++) + ".");

                sleep(spavanje);
            } catch (MessagingException | InterruptedException | IOException | SQLException | ClassNotFoundException ex) {
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
        trazeniNazivPrivitka = k.dajPostavku("mail.attachmentFilename");        

        super.start();
    }
}
