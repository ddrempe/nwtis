package org.foi.nwtis.damdrempe.web.podaci;

/**
 * Preuzeta klasa.
 * @author ddrempetic
 */
public class MeteoPrognoza {

    private int id;
    private int sat;
    private MeteoPodaci prognoza;

    public MeteoPrognoza() {
    }

    public MeteoPrognoza(int id, int dan, MeteoPodaci prognoza) {
        this.id = id;
        this.sat = dan;
        this.prognoza = prognoza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public MeteoPodaci getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(MeteoPodaci prognoza) {
        this.prognoza = prognoza;
    }


}