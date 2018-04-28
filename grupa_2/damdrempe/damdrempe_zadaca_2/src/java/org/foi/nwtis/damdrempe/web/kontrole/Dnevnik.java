package org.foi.nwtis.damdrempe.web.kontrole;

import java.util.Date;

/**
 * Klasa za rad s zapisima iz dnevnika rada.
 * @author ddrempetic
 */
public class Dnevnik {

    private int id;
    private String sadrzaj;
    private Date vrijemeZapisa;

    /**
     * Konstruktor klase.
     * @param id
     * @param sadrzaj
     * @param vrijemeZapisa 
     */
    public Dnevnik(int id, String sadrzaj, Date vrijemeZapisa) {
        this.id = id;
        this.sadrzaj = sadrzaj;
        this.vrijemeZapisa = vrijemeZapisa;
    }

    public int getId() {
        return id;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public Date getVrijemeZapisa() {
        return vrijemeZapisa;
    }
}
