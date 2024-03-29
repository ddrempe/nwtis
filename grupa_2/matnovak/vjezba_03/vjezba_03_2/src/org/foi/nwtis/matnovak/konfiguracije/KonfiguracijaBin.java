package org.foi.nwtis.matnovak.konfiguracije;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class KonfiguracijaBin extends KonfiguracijaApstraktna {

    public KonfiguracijaBin(String datoteka) {
        super(datoteka);
    }

    @Override
    public void ucitajKonfiguraciju() throws NemaKonfiguracije, NeispravnaKonfiguracija {
        ucitajKonfiguraciju(this.datoteka);
    }

    @Override
    public void ucitajKonfiguraciju(String datoteka) throws NemaKonfiguracije, NeispravnaKonfiguracija {
        if (datoteka == null || datoteka.length() == 0) {
            throw new NemaKonfiguracije("Naziv datoteke nedostaje!");
        }

        File datKonf = new File(datoteka);

        if (!datKonf.exists()) {
            throw new NemaKonfiguracije("Datotkeka " + datoteka + " ne postoji!");
        } else if (datKonf.isDirectory()) {
            throw new NemaKonfiguracije(datoteka + " nije datoteka već direktorij!");
        }

        try {
            InputStream is = Files.newInputStream(datKonf.toPath(), StandardOpenOption.READ);
            ObjectInputStream ois = new ObjectInputStream(is);
            this.postavke = (Properties) ois.readObject();
            ois.close();
            is.close();
        } catch (IOException | ClassNotFoundException ex) {
            throw new NeispravnaKonfiguracija("Problem kod učitavanja datoteke: " + datKonf.getAbsolutePath());
        }
    }

    @Override
    public void spremiKonfiguraciju() throws NemaKonfiguracije, NeispravnaKonfiguracija {
        spremiKonfiguraciju(this.datoteka);
    }

    @Override
    public void spremiKonfiguraciju(String datoteka) throws NemaKonfiguracije, NeispravnaKonfiguracija {
        if (datoteka == null || datoteka.length() == 0) {
            throw new NemaKonfiguracije("Naziv datoteke nedostaje!");
        }

        File datKonf = new File(datoteka);

        if (datKonf.exists() && datKonf.isDirectory()) {
            throw new NemaKonfiguracije(datoteka + " nije datoteka već direktorij!");
        }
        
        try {
            OutputStream os = Files.newOutputStream(datKonf.toPath(), StandardOpenOption.CREATE);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(this.postavke);
            oos.close();
            os.close();
        } catch (IOException ex) {
            throw new NeispravnaKonfiguracija("Problem kod spremanja datoteke: " + datKonf.getAbsolutePath());
        }

    }

}
