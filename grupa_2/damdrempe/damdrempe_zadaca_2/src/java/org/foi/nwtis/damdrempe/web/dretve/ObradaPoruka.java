/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import org.foi.nwtis.damdrempe.web.kontrole.Evidencija;
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
    private String datotekaEvidencijeRada;
    private int brojDodanihIot;
    private int brojAzuriranihIot;
    private int brojNeispravnihPoruka;

    private void ObradaNwtisPoruke(Poruka poruka) throws SQLException {        
        String tekstPrivitka = poruka.getPrivitak();
        Komanda komanda = PomocnaKlasa.ParsirajJsonKomande(tekstPrivitka);
        boolean ispravnostSintakse = PomocnaKlasa.ProvjeriSintaksuPrivitka(tekstPrivitka);
                
        if(komanda.komanda == null || ispravnostSintakse == false){
            System.out.println("Neispravna komanda ili sintaksa: " + komanda.komanda);
            brojNeispravnihPoruka++;
            return;
        }
        
        if(komanda.komanda.equalsIgnoreCase("dodaj")){
            if(bpo.UredajiInsert(komanda, tekstPrivitka) == true){
                brojDodanihIot++;
            }
            else{
                System.out.println("Nije moguc INSERT. Vec postoji uredaj s id: " + komanda.id);      
            }
            bpo.DnevnikInsert(tekstPrivitka, komanda.id);                    
        }
        else if(komanda.komanda.equalsIgnoreCase("azuriraj")){
            if(bpo.UredajiUpdate(komanda, tekstPrivitka) == true){
                brojAzuriranihIot++;
            }
            else{
                System.out.println("Nije moguc UPDATE. Ne postoji uredaj s id: " + komanda.id);
            }
            bpo.DnevnikInsert(tekstPrivitka, komanda.id);                 
        } 
        else {
            System.out.println("Neispravna komanda: " + komanda.komanda);
            brojNeispravnihPoruka++;
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
        try {
            PomocnaKlasa.ObrisiSadrzajDatoteke(datotekaEvidencijeRada);
        } catch (IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (radi) {
            try {
                brojDodanihIot = 0;
                brojAzuriranihIot = 0;
                brojNeispravnihPoruka = 0;
                Evidencija evidencija = new Evidencija();
                evidencija.setPocetakObrade();
                evidencija.redniBrojObrade = brojacObrada;
                
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
                                    
                        }
                    }
                    
                    pocetnaPoruka = zavrsnaPoruka + 1;
                    zavrsnaPoruka = pocetnaPoruka + brojPorukaZaCitanje - 1;

                    if (pocetnaPoruka >= ukupnoPoruka) {
                        System.out.println(brojacObrada + " | Obradene su sve poruke!");
                        break;
                    }
                }
                
                for (Message message : porukeZaPrebacivanje) {
                      Message[] poruke = new Message[1];
                      poruke[0] = message;
                      folderInbox.copyMessages(poruke, folderNwtis);
                      message.setFlag(Flags.Flag.DELETED, true);
                      folderInbox.expunge();  
                      
                      System.out.println(brojacObrada + " | Poruka premjestena u posebnu mapu.");
                }

                folderInbox.close(false);
                store.close();
                
                bpo.ZatvoriVezu();

                System.out.println("KRAJ! Završila obrada: " + (brojacObrada++) + ".");
                
                evidencija.setZavrsetakObrade();
                evidencija.setTrajanjeObrade();
                evidencija.brojPoruka = ukupnoPoruka;
                evidencija.brojDodanihIot = brojDodanihIot;
                evidencija.brojAzuriranihIot = brojAzuriranihIot;
                evidencija.brojNeispravnihPoruka = brojNeispravnihPoruka;
                
                PomocnaKlasa.ZapisiEvidencijuUDatoteku(evidencija, datotekaEvidencijeRada);

                sleep(spavanje);
            } catch (MessagingException | IOException | SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                System.out.println("Dretva je zaustavljena prilikom spavanja");
            } catch (IllegalStateException ex) {
                
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
        datotekaEvidencijeRada = k.dajPostavku("mail.threadCycleLogFilename"); 

        super.start();
    }
}
