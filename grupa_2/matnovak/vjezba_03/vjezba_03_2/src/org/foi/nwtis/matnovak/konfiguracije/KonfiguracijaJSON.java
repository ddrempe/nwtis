package org.foi.nwtis.matnovak.konfiguracije;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class KonfiguracijaJSON extends KonfiguracijaApstraktna {

    public KonfiguracijaJSON(String datoteka) {
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

        //TODO dovršiti
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
        
        //TODO dovršiti
    }

}
