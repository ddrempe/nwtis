/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.dretve;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.rest.serveri.JsonOdgovor;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 *
 * @author ddrempetic
 */
public class AkcijePosluzitelj {
    
    public static String listaj(){
        List<Korisnik> korisnici = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            korisnici = bpo.korisniciSelectSviKorisnici();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AkcijePosluzitelj.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(korisnici.isEmpty()){
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_ERR;          
        } else {
            JsonOdgovor jsonOdgovor = new JsonOdgovor(true, "");
            JsonArray korisniciJsonDio = jsonOdgovor.postaviSviKorisniciJsonDio(korisnici);            
            return OdgovoriKomandi.POSLUZITELJ_LISTAJ_OK + korisniciJsonDio.toString();
        }
        
    }
    
}
