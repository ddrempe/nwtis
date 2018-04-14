/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dkermek.web;

import java.beans.PropertyChangeEvent;

/**
 *
 * @author grupa_2
 */
public class SlusacPromjena implements java.beans.PropertyChangeListener {
    int brojPromjena = 0;
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        brojPromjena++;
        String varijabla = evt.getPropertyName();
        Object staraVrijednost = evt.getOldValue();
        Object novaVrijednost = evt.getNewValue();
        System.out.println("Broj: " + brojPromjena + 
                " varijabla: " + varijabla + 
                " stara vr: " + staraVrijednost + 
                " nova vr: " + novaVrijednost);
    }
    
}
