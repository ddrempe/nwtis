package org.foi.nwtis.damdrempe.web;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DretvaPromjena extends Thread {

    SlusacPromjena sp;
    String klasa;
    Brojaci brojac;
    int broj;

    public DretvaPromjena(SlusacPromjena sp, String klasa, int broj) {
        this.sp = sp;
        this.klasa = klasa;
        this.broj = broj;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        try {
            while (true) {
                brojac.run();

                sleep(this.broj * 1000);

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(DretvaPromjena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        try {
            Class klasaUcitana = Class.forName(this.klasa);
            brojac = (Brojaci) klasaUcitana.newInstance();
            brojac.dodajSlusaca(sp);
            super.start();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DretvaPromjena.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
