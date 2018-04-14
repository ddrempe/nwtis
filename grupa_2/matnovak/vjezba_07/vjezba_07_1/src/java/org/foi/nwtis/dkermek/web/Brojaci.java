/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dkermek.web;

import java.beans.PropertyChangeListener;

/**
 *
 * @author grupa_2
 */
public interface Brojaci {

    void dodajSlusaca(PropertyChangeListener slusac);

    void obrisiSlusaca(PropertyChangeListener slusac);

    void setRedniBroj(int redniBroj);

    void run();
}
