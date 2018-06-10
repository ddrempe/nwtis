package org.foi.nwtis.damdrempe.pomocno;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ddrempetic
 */
public class Stranicenje<E> {
    
    private List<E> listaCijela = new ArrayList<>();
    private List<E> listaZaPrikaz = new ArrayList<>();
    private int ukupnoZapisa;    
    private int brojZapisaPoStraniciZaPrikaz;    
    private int pomak;
    private int maksPomak; 
    
    private int indeksPrvogZapisa;
    private int indeksZadnjegZapisa;
    
    private boolean prvaStranica;
    private boolean zadnjaStranica;

    public Stranicenje(List<E> lista, int brojZapisaPoStranici) {
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
    public void prethodniZapisi() {
        if(prvaStranica() == true){
            return;
        }
        
        pomak--;        
        osvjeziListuZaPrikaz();
    }
    
    /**
     * Mijenja pomak u odnosu na prvu stranicu i ponovno preuzima zapise.
     *
     */
    public void sljedeciZapisi() {
        if(zadnjaStranica() == true){
            return;
        }        
        
        pomak++;        
        osvjeziListuZaPrikaz();
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
    
    public boolean zadnjaStranica(){
        return pomak == maksPomak;
    }
    
    public boolean prvaStranica(){
        return pomak == 0;
    }

    public List<E> getListaZaPrikaz() {
        return listaZaPrikaz;
    }

    public void setListaZaPrikaz(List<E> listaZaPrikaz) {
        this.listaZaPrikaz = listaZaPrikaz;
    }   

    public boolean isPrvaStranica() {
        return prvaStranica();
    }

    public boolean isZadnjaStranica() {
        return zadnjaStranica();
    }
    
    
}
