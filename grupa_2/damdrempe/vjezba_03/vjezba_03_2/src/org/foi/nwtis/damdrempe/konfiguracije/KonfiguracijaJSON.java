package org.foi.nwtis.damdrempe.konfiguracije;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            throw new NemaKonfiguracije("Datoteka: " + datoteka + " ne postoji!");
        } else if (datKonf.isDirectory()) {
            throw new NemaKonfiguracije(datoteka + " nije datoteka već direktorij");
        }

        try {
            Gson gson = new Gson();
            this.postavke = gson.fromJson(new FileReader(datoteka), Properties.class);
        } catch (FileNotFoundException ex) {
            throw new NeispravnaKonfiguracija("Problem kod ucitavanja datoteke: " + datKonf.getAbsolutePath());
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
            Writer writer = new FileWriter(datoteka);
            Gson gson = new GsonBuilder().create();
            gson.toJson(this.postavke, writer);
            writer.close();
        } catch (IOException ex) {
            throw new NeispravnaKonfiguracija("Problem kod spremanja datoteke: " + datKonf.getAbsolutePath());
        }
    }
}
