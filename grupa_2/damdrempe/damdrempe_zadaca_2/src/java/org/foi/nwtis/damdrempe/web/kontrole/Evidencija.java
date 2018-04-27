/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.kontrole;

import java.util.Date;

/**
 *
 * @author ddrempetic
 */
public class Evidencija {
    public int redniBrojObrade;
    public Date pocetakObrade;
    public Date zavrsetakObrade;
    public long trajanjeObrade;
    public int brojPoruka;
    public int brojDodanihIot;
    public int brojAzuriranihIot;
    public int brojNeispravnihPoruka;
    
    public void setPocetakObrade() {
        this.pocetakObrade = new Date(System.currentTimeMillis());
    }


    public void setZavrsetakObrade() {
        this.zavrsetakObrade = new Date(System.currentTimeMillis());
    }

    public void setTrajanjeObrade() {
        this.trajanjeObrade = this.zavrsetakObrade.getTime() - this.pocetakObrade.getTime();
    }   
}
