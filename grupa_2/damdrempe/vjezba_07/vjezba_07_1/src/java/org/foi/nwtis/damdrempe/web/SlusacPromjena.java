package org.foi.nwtis.damdrempe.web;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author grupa_2
 */
public class SlusacPromjena implements PropertyChangeListener {
    
    int brojPromjena = 0;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        brojPromjena++;
        String varijabla = evt.getPropertyName();
        Object staraVrijednost = evt.getOldValue();
        Object novaVrijednost = evt.getNewValue();
        System.out.println("Broj: " + brojPromjena + 
                " varijabla: " + varijabla +
                "stara vr: " + staraVrijednost + 
                "nova vr: " + novaVrijednost);
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
