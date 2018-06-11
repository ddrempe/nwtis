package org.foi.nwtis.damdrempe.pomocno;

import javax.json.Json;
import javax.json.JsonObject;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;

/**
 * Klasa za stvaranje JSON objekata koje prima REST servis za oparkirališta u
 * prvoj aplikaciji.
 *
 * @author ddrempetic
 */
public class JsonGraditelj {

    /**
     * Gradi json tekst preko kojeg se šalje naziv i adresa parkirališta
     *
     * @param naziv
     * @param adresa
     * @return json tekst
     */
    public static String napraviJsonZaDodajParkiraliste(String naziv, String adresa, String kapacitet) {
        JsonObject jsonOdgovor;
        jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                .add("naziv", naziv)
                .add("adresa", adresa)
                .add("kapacitet", kapacitet)
                .build());

        return jsonOdgovor.toString();
    }

    /**
     * Gradi json tekst preko kojeg se šalje naziv i adresa parkirališta
     *
     * @param p
     * @return json tekst
     */
    public static String napraviJsonZaAzurirajParkiraliste(Parkiraliste p) {
        JsonObject jsonOdgovor;
        jsonOdgovor = (JsonObject) (Json.createObjectBuilder()
                .add("naziv", p.getNaziv())
                .add("adresa", p.getAdresa())
                .add("kapacitet", String.valueOf(p.getKapacitet()))
                .add("status", p.getStatus())
                .build());

        return jsonOdgovor.toString();
    }

}
