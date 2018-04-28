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
import javax.mail.NoSuchProviderException;
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
 * Dretva koja provodi obradu poruka.
 *
 * @author ddrempetic
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
    private int ukupnoPoruka;
    private int brojacObrada = 1;

    /**
     * Prekida se rad dretve.
     */
    @Override
    public void interrupt() {
        radi = false;
        super.interrupt();
    }

    /**
     * Prilikom početka rada dretve se pročitaju sve potrene postavke iz
     * datoteke konfiguracije.
     */
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

    /**
     * Pokretanje rada dretve.
     *
     */
    @Override
    public void run() {
        super.run();
        try {
            PomocnaKlasa.ObrisiSadrzajDatoteke(datotekaEvidencijeRada);
        } catch (IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (radi) {
            try {
                Evidencija evidencija = stvoriEvidenciju();
                bpo = new BazaPodatakaOperacije();
                Store store = vratiStoreInstancu();
                Folder folderInbox = vratiMapuInbox(store);
                Folder folderNwtis = kreirajNwtisMapu(store);
                ArrayList<Message> porukeZaPrebacivanje = citajPorukeIzMape(folderInbox);
                prebaciPoruke(porukeZaPrebacivanje, folderInbox, folderNwtis);

                folderInbox.close(false);
                store.close();
                bpo.ZatvoriVezu();                
                spremiEvidenciju(evidencija);
                System.out.println("KRAJ! Završila obrada: " + (brojacObrada++) + ".");
                sleep(spavanje);
            } catch (MessagingException | IOException | SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                System.out.println("Dretva je zaustavljena prilikom spavanja");
            } catch (IllegalStateException ex) {
            }
        }
    }

    /**
     * Obrada poruke koja je vrste NWTIS poruke.
     *
     * @param poruka
     * @throws SQLException
     */
    private void ObradaNwtisPoruke(Poruka poruka) throws SQLException {
        String tekstPrivitka = poruka.getPrivitak();
        Komanda komanda = PomocnaKlasa.ParsirajJsonKomande(tekstPrivitka);
        boolean ispravnostSintakse = PomocnaKlasa.ProvjeriSintaksuPrivitka(tekstPrivitka);

        if (komanda.komanda == null || ispravnostSintakse == false) {
            System.out.println("Neispravna komanda ili sintaksa: " + komanda.komanda);
            brojNeispravnihPoruka++;
            return;
        }

        if (komanda.komanda.equalsIgnoreCase("dodaj")) {
            if (bpo.UredajiInsert(komanda, tekstPrivitka) == true) brojDodanihIot++;
            else System.out.println("Nije moguc INSERT. Vec postoji uredaj s id: " + komanda.id);
            bpo.DnevnikInsert(tekstPrivitka, komanda.id);
        } else if (komanda.komanda.equalsIgnoreCase("azuriraj")) {
            if (bpo.UredajiUpdate(komanda, tekstPrivitka) == true) brojAzuriranihIot++;
            else System.out.println("Nije moguc UPDATE. Ne postoji uredaj s id: " + komanda.id);
            bpo.DnevnikInsert(tekstPrivitka, komanda.id);
        } else {
            System.out.println("Neispravna komanda: " + komanda.komanda);
            brojNeispravnihPoruka++;
        }
    }
   
    /**
     * Vraća objekt za mapu inbox.
     * @param store
     * @return objekt mape
     * @throws NoSuchProviderException
     * @throws MessagingException 
     */
    private Folder vratiMapuInbox(Store store) throws NoSuchProviderException, MessagingException {
        store.connect(posluzitelj, korIme, lozinka);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        return folder;
    }

    /**
     * Kreiranje Store instance
     * @return instancu store
     * @throws NoSuchProviderException 
     */
    private Store vratiStoreInstancu() throws NoSuchProviderException {
        java.util.Properties postavke = System.getProperties();
        postavke.put("mail.smtp.host", posluzitelj);
        Session sesija = Session.getInstance(postavke, null);
        Store store = sesija.getStore("imap");

        return store;
    }

    /**
     * Provjerava da li postoji nwtis folder i stvara ga ako ne postoji
     * @param store
     * @return objekt mape za nwtis folder
     * @throws MessagingException 
     */
    private Folder kreirajNwtisMapu(Store store) throws MessagingException {
        Folder folderNwtis = store.getFolder(posebnaMapa);
        if (!folderNwtis.exists()) {
            folderNwtis.create(Folder.HOLDS_MESSAGES);
        }

        return folderNwtis;
    }

    /**
     * Čita sve poruke iz mape, obrađuje nwtis poruke i bilježi ih za prebacivanje.
     * @param folder
     * @return lista nwtis poruka koje treba prebaciti
     * @throws SQLException
     * @throws MessagingException
     * @throws IOException 
     */
    private ArrayList<Message> citajPorukeIzMape(Folder folder) throws SQLException, MessagingException, IOException {
        ukupnoPoruka = folder.getMessageCount();
        System.out.println(brojacObrada + " | Imamo trenutno " + ukupnoPoruka + " poruka u sanducicu!");
        int pocetnaPoruka = 1;
        int zavrsnaPoruka = pocetnaPoruka + brojPorukaZaCitanje - 1;
        ArrayList<Message> porukeZaPrebacivanje = new ArrayList<>();

        while (true) {
            if (zavrsnaPoruka > ukupnoPoruka) {
                zavrsnaPoruka = ukupnoPoruka;
            }
            System.out.println(brojacObrada + " | Obrada poruka od broja " + pocetnaPoruka + " do broja " + zavrsnaPoruka + ".");
            Message[] messages = folder.getMessages(pocetnaPoruka, zavrsnaPoruka);
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
        return porukeZaPrebacivanje;
    }

    /**
     * Prebacuje nwtis poruke u posebnu mapu
     * @param porukeZaPrebacivanje
     * @param folderInbox
     * @param folderNwtis
     * @throws MessagingException 
     */
    private void prebaciPoruke(ArrayList<Message> porukeZaPrebacivanje, Folder folderInbox, Folder folderNwtis) throws MessagingException {
        for (Message message : porukeZaPrebacivanje) {
            Message[] poruke = new Message[1];
            poruke[0] = message;
            folderInbox.copyMessages(poruke, folderNwtis);
            message.setFlag(Flags.Flag.DELETED, true);
            folderInbox.expunge();

            System.out.println(brojacObrada + " | Poruka premjestena u posebnu mapu.");
        }
    }

    /**
     * Stvara novu evidenciju rada 
     * @return 
     */
    private Evidencija stvoriEvidenciju() {
        brojDodanihIot = 0;
        brojAzuriranihIot = 0;
        brojNeispravnihPoruka = 0;
        Evidencija evidencija = new Evidencija();
        evidencija.setPocetakObrade();
        evidencija.redniBrojObrade = brojacObrada;

        return evidencija;
    }

    /**
     * Prema podacima evidencije zapisuje nove zapise u datoteku
     * @param evidencija
     * @throws IOException 
     */
    private void spremiEvidenciju(Evidencija evidencija) throws IOException {
        evidencija.setZavrsetakObrade();
        evidencija.setTrajanjeObrade();
        evidencija.brojPoruka = ukupnoPoruka;
        evidencija.brojDodanihIot = brojDodanihIot;
        evidencija.brojAzuriranihIot = brojAzuriranihIot;
        evidencija.brojNeispravnihPoruka = brojNeispravnihPoruka;

        PomocnaKlasa.ZapisiEvidencijuUDatoteku(evidencija, datotekaEvidencijeRada);
    }
}
