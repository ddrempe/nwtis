package org.foi.nwtis.damdrempe;

public class Vjezba_03_3 {

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Broj argumenata ne valja!");
            return;
        }
        
        KonfiguracijaApstraktna.kreirajKonfiguraciju(args[0]);
    }
    
}
