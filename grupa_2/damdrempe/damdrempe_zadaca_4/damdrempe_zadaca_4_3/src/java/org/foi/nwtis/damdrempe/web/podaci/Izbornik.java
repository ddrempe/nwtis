package org.foi.nwtis.damdrempe.web.podaci;

import java.util.Objects;

/**
 * Preuzeta klasa.
 * @author ddrempetic
 */
public class Izbornik {
    private String labela;
    private String vrijednost;

    public Izbornik(String labela, String vrijednost) {
        this.labela = labela;
        this.vrijednost = vrijednost;
    }

    public String getLabela() {
        return labela;
    }

    public void setLabela(String labela) {
        this.labela = labela;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }   

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Izbornik) {
            Izbornik obj2 = (Izbornik) obj;
            if(this.vrijednost.equals(obj2.getVrijednost())
                    && 
                    this.labela.equals(obj2.getLabela())){
                return true;
            }
        }
        
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "Izbornik{" + "labela=" + labela + ", vrijednost=" + vrijednost + '}';
    }
}
