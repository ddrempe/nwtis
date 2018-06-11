package org.foi.nwtis.damdrempe.web.podaci;

/**
 * Klasa za rad s vozilima koja se dohvaćaju sa web servisa.
 * @author ddrempetic
 */
public class Vozilo {

    /**
     *
     */
    public enum StatusVozila {

        /**
         *
         */
        ULAZ,

        /**
         *
         */
        IZLAZ;
    }

    private StatusVozila akcija;
    private int parkiraliste;
    private String registracija;

    /**
     *
     */
    public Vozilo() {
    }

    /**
     *
     * @param akcija
     * @param parkiraliste
     * @param registracija
     */
    public Vozilo(StatusVozila akcija, int parkiraliste, String registracija) {
        this.akcija = akcija;
        this.parkiraliste = parkiraliste;
        this.registracija = registracija;
    }

    /**
     *
     * @return
     */
    public StatusVozila getAkcija() {
        return akcija;
    }

    /**
     *
     * @param akcija
     */
    public void setAkcija(StatusVozila akcija) {
        this.akcija = akcija;
    }

    /**
     *
     * @return
     */
    public int getParkiraliste() {
        return parkiraliste;
    }

    /**
     *
     * @param parkiraliste
     */
    public void setParkiraliste(int parkiraliste) {
        this.parkiraliste = parkiraliste;
    }

    /**
     *
     * @return
     */
    public String getRegistracija() {
        return registracija;
    }

    /**
     *
     * @param registracija
     */
    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

}
