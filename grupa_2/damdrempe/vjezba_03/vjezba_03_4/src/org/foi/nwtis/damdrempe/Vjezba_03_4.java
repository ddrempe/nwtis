package org.foi.nwtis.damdrempe;

import com.sun.istack.internal.logging.Logger;
import java.util.Properties;
import java.util.logging.Level;
import org.foi.nwtis.damdrempe.konfiguracije.Konfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;

public class Vjezba_03_4 {

    public static void main(String[] args) {
        if(args.length >3 || args.length == 0){
            System.out.println("Broj argumenata ne valja!");
            return;
        }
        
        if(args.length == 1){
            try {
                Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
                Properties p = konf.dajSvePostavke();
                for (Object k : p.keySet()) {
                    String v = konf.dajPostavku((String) k);
                    System.out.println(k + " = " + v);
                }
            } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                Logger.getLogger(Vjezba_03_4.class.getName()).log(Level.SEVERE);
            }
        }
        
        if(args.length == 2){
            try {
                Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
                String p = konf.dajPostavku(args[0]);
                for (Object k : p.keySet()) {
                    String v = konf.dajPostavku((String) k);
                    System.out.println(k + " = " + v);
                }
            } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                Logger.getLogger(Vjezba_03_4.class.getName()).log(Level.SEVERE);
            }
        }
    }
    
}
