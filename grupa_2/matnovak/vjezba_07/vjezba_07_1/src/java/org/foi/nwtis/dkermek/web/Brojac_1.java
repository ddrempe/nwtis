/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dkermek.web;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

/**
 *
 * @author grupa_2
 */
public class Brojac_1 implements Brojaci {
    static int brojInstanci = 0;
    int redniBroj = 0;
    PropertyChangeSupport podrskaZaPromjenu;
    
    public Brojac_1() {
        brojInstanci++;
        podrskaZaPromjenu = new PropertyChangeSupport(this);
    }

    @Override
    public void dodajSlusaca(PropertyChangeListener slusac) {
        podrskaZaPromjenu.addPropertyChangeListener(slusac);
    }

    @Override
    public void obrisiSlusaca(PropertyChangeListener slusac) {
        podrskaZaPromjenu.removePropertyChangeListener(slusac);
    }

    @Override
    public void setRedniBroj(int redniBroj) {
        int staraVr = this.redniBroj;
        this.redniBroj = redniBroj;
        podrskaZaPromjenu.firePropertyChange("redniBroj", staraVr, this.redniBroj);
    }

    @Override
    public void run() {
        Class klasa = getClass();
        Random r = new Random(System.nanoTime());
        int noviBroj = r.nextInt(100);
        setRedniBroj(noviBroj);
    }
    
}
