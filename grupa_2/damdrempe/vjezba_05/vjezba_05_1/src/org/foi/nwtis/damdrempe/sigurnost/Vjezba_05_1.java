package org.foi.nwtis.damdrempe.sigurnost;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;

public class Vjezba_05_1 {

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Broj argumenata ne valja!");
            return;
        }
        
        File datoteka = new File(args[0]);
        
        if(!datoteka.exists() || datoteka.isDirectory()){
            System.out.println("Datoteka ne postoji!");
            return;
        }
        
        try {
            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
            Properties p = konf.dajSvePostavke();
            for (Object k : p.keySet()) {
                String v = konf.dajPostavku((String) k);
                System.out.println(k + " = " + v);
            }
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(Vjezba_05_1.class.getName()).log(Level.SEVERE, ex , null);
        }
    }
    
}
