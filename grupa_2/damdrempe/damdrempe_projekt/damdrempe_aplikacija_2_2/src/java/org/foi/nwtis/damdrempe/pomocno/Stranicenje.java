package org.foi.nwtis.damdrempe.pomocno;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.damdrempe.web.podaci.Korisnik;

/**
 *
 * @author ddrempetic
 */
public class Stranicenje {
    
    private List<Korisnik> listaCijela = new ArrayList<>();
    private List<Korisnik> listaZaPrikaz = new ArrayList<>();
    private int ukupnoZapisa;    
    private int brojZapisaPoStraniciZaPrikaz;    
    private int pomak;
    private int maksPomak; 
    
    private int indeksPrvogZapisa;
    private int indeksZadnjegZapisa;

    public Stranicenje(List<Korisnik> lista, int brojZapisaPoStranici) {
        this.pomak = 0;
        this.listaCijela = lista;
        this.brojZapisaPoStraniciZaPrikaz = brojZapisaPoStranici;
        
        this.indeksPrvogZapisa = 0;
        this.indeksZadnjegZapisa = indeksPrvogZapisa + brojZapisaPoStranici;
        
        osvjeziListuZaPrikaz();
    }   
    
    /**
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.
     *
     */
    public boolean prethodniZapisi() {
        if(pomak == 0){
            return false;
        }
        
        pomak--;        
        osvjeziListuZaPrikaz();
        
        return true;
    }
    
    /**
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.
     *
     */
    public boolean sljedeciZapisi() {
        if(pomak == maksPomak){
            return false;
        }
        
        pomak++;        
        osvjeziListuZaPrikaz();
        
        return true;
    }
    
    private void osvjeziListuZaPrikaz(){
        ukupnoZapisa = listaCijela.size();
        indeksPrvogZapisa = pomak * brojZapisaPoStraniciZaPrikaz;
        indeksZadnjegZapisa = indeksPrvogZapisa + brojZapisaPoStraniciZaPrikaz;
        
        maksPomak = ukupnoZapisa / brojZapisaPoStraniciZaPrikaz;
        if (ukupnoZapisa % brojZapisaPoStraniciZaPrikaz == 0) {
            maksPomak--;
        }

        if (indeksZadnjegZapisa >= listaCijela.size()) {
            indeksZadnjegZapisa = listaCijela.size();
        }
        
        listaZaPrikaz.clear();
        listaZaPrikaz = new ArrayList<>(listaCijela.subList(indeksPrvogZapisa, indeksZadnjegZapisa));
    }

    public List<Korisnik> dajZapiseZaPrikaz() {
        return listaZaPrikaz;
    }
    
    
}
