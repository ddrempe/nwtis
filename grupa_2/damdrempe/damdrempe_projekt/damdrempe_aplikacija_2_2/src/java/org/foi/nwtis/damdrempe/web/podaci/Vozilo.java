package org.foi.nwtis.damdrempe.web.podaci;

public class Vozilo {

    public enum StatusVozila {
        ULAZ,
        IZLAZ;
    }

    private StatusVozila akcija;
    private int parkiraliste;
    private String registracija;

    public Vozilo() {
    }

    public Vozilo(StatusVozila akcija, int parkiraliste, String registracija) {
        this.akcija = akcija;
        this.parkiraliste = parkiraliste;
        this.registracija = registracija;
    }

    public StatusVozila getAkcija() {
        return akcija;
    }

    public void setAkcija(StatusVozila akcija) {
        this.akcija = akcija;
    }

    public int getParkiraliste() {
        return parkiraliste;
    }

    public void setParkiraliste(int parkiraliste) {
        this.parkiraliste = parkiraliste;
    }

    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

}
