/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.rest.serveri;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.damdrempe.pomocno.BazaPodatakaOperacije;
import org.foi.nwtis.damdrempe.pomocno.KorisnikPodaci;
import org.foi.nwtis.damdrempe.pomocno.PomocnaKlasa;
import org.foi.nwtis.damdrempe.pomocno.Dnevnik;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.Parkiraliste;
import org.foi.nwtis.damdrempe.ws.klijenti.ParkiranjeWSKlijent;
import org.foi.nwtis.damdrempe.ws.klijenti.StatusParkiralista;
import org.foi.nwtis.damdrempe.ws.klijenti.Vozilo;

/**
 * REST Web Service
 *
 * @author ddrempetic
 */
@Path("parkiranje")
public class ParkiranjeREST {

    @Context
    private UriInfo context;
    
    @Context 
    HttpServletRequest request;

    /**
     * Creates a new instance of ParkiranjeREST
     */
    public ParkiranjeREST() {
    }

    /**
     * Metoda za dohvat svih parkirališta preko servisa.
     *
     * @param korisnickoIme
     * @param lozinka
     * @return popis svih parkirališta, njihovih adresa i geo lokacija kao
     * strukturirani odgovor u application/json formatu
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";
        List<Parkiraliste> svaParkiralista = new ArrayList<>();

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            svaParkiralista = bpo.parkiralistaSelectSvaParkiralista();
            bpo.zatvoriVezu();
            dnevnik.postaviUspjesanStatus();
        } catch (SQLException | ClassNotFoundException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(ParkiranjeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        JsonArray parkiralistaJsonDio = jsonOdgovor.postaviSvaParkiralistaJsonDio(svaParkiralista);
        return jsonOdgovor.vratiKompletanJsonOdgovor(parkiralistaJsonDio);
    }

    /**
     * Metoda za dohvat podataka pojedinog parkirališta.
     *
     * @param id identifikator parkirališta po kojem se dohvaća
     * @param korisnickoIme
     * @param lozinka
     * @return vraća podatke o parkiralištu s zadanim id
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getJson(@PathParam("id") String id, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";
        int idParkiralista = Integer.parseInt(id);
        Parkiraliste p = new Parkiraliste();

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (!bpo.parkiralistaSelectIdPostoji(idParkiralista)) {
                uspjesno = false;
                poruka = "Parkiraliste s id " + id + " ne postoji!";
            } else {
                p = bpo.parkiralistaSelectIdJednoParkiraliste(idParkiralista);
                dnevnik.postaviUspjesanStatus();
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(ParkiranjeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        if (uspjesno) {
            JsonArray meteoJsonDio = jsonOdgovor.postaviParkiralisteJsonDio(p);
            return jsonOdgovor.vratiKompletanJsonOdgovor(meteoJsonDio);
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }
    }
    
    /**
     * Metoda za dohvat vozila pojedinog parkirališta.
     * @param id identifikator parkirališta.
     * @param korisnickoIme
     * @param lozinka
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/vozila")
    public String getJsonVozila(@PathParam("id") String id, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();        
        boolean uspjesno = true;
        String poruka = "";
        int idParkiralista = Integer.parseInt(id);
        
        java.util.List<org.foi.nwtis.damdrempe.ws.klijenti.Vozilo> svaVozila = new ArrayList<>();
        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();      
            svaVozila = ParkiranjeWSKlijent.dajSvaVozilaParkiralistaGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka(), idParkiralista);
            dnevnik.postaviUspjesanStatus();
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());
        
        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        if (uspjesno) {
            JsonArray vozilaJsonDio = jsonOdgovor.postaviSvaVozilaJsonDio(svaVozila);
            return jsonOdgovor.vratiKompletanJsonOdgovor(vozilaJsonDio);
        } else {
            return jsonOdgovor.vratiKompletanJsonOdgovor();
        }
    }

    /**
     * Metoda za dodavanje novog parkirališta u bazu podataka.
     *
     * @param podaci podaci parkirališta u application/json formatu
     * @param korisnickoIme
     * @param lozinka
     * @return strukturirani odgovor u application/json formatu
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String postJson(String podaci, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";

        Parkiraliste parkiraliste;
        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            Gson gson = new Gson();
            parkiraliste = gson.fromJson(podaci, Parkiraliste.class);

            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (bpo.parkiralistaSelectNazivPostoji(parkiraliste.getNaziv())) {
                uspjesno = false;
                poruka = "Parkiraliste s nazivom " + parkiraliste.getNaziv() + " vec postoji!";
            } else {
                Lokacija lokacija = PomocnaKlasa.dohvatiGMLokaciju(parkiraliste.getAdresa());
                parkiraliste.setGeoloc(lokacija);
                bpo.parkiralistaInsert(parkiraliste);
                sinkronizirajParkiralista();
                dnevnik.postaviUspjesanStatus();
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(ParkiranjeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }

    /**
     * Metoda za POST na bazi putanje {id} nije dozvoljena.
     *
     * @param id identifikator parkirališta po kojem se dohvaća
     * @param podaci podaci parkirališta u application/json formatu
     * @return grešku HTTP statusa 405: Nedozvoljena operacija
     */
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postJson(@PathParam("id") String id, String podaci) {
        return dajOdgovorZaNedozvoljenPristup();
    }   

    /**
     * Metoda za PUT na bazi osnovne adrese nije dozvoljena.
     *
     * @return grešku HTTP statusa 405: Nedozvoljena operacija
     */
    @PUT
    public Response putJson() {
        return dajOdgovorZaNedozvoljenPristup();
    }

    /**
     * Metoda za ažuriranje novog parkirališta u bazi podataka.
     *
     * @param id identifikator parkirališta po kojem se ažurira
     * @param podaci podaci parkirališta u application/json formatu
     * @param korisnickoIme
     * @param lozinka
     * @return strukturirani odgovor u application/json formatu
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String putJson(@PathParam("id") String id, String podaci, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";
        int idParkiralista = Integer.parseInt(id);

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            if (!bpo.parkiralistaSelectIdPostoji(idParkiralista)) {
                uspjesno = false;
                poruka = "Parkiraliste s id " + id + " ne postoji!";
            } else {
                Gson gson = new Gson();
                Parkiraliste parkiraliste = gson.fromJson(podaci, Parkiraliste.class);
                Lokacija lokacija = PomocnaKlasa.dohvatiGMLokaciju(parkiraliste.getAdresa());
                parkiraliste.setId(idParkiralista);
                parkiraliste.setGeoloc(lokacija);
                bpo.parkiralistaUpdate(parkiraliste);
                sinkronizirajParkiralista();
                dnevnik.postaviUspjesanStatus();
            }
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(ParkiranjeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }

    /**
     * Metoda za DELETE na bazi osnovne adrese nije dozvoljena.
     *
     * @return grešku HTTP statusa 405: Nedozvoljena operacija
     */
    @DELETE
    public Response deleteJson() {
        return dajOdgovorZaNedozvoljenPristup();
    }

    /**
     * Metoda za brisanje pojedinog parkirališta.
     *
     * @param id identifikator parkirališta po kojem se dohvaća
     * @param korisnickoIme
     * @param lozinka
     * @return strukturirani odgovor u application/json formatu
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String deleteJson(@PathParam("id") String id, @HeaderParam("korisnickoIme") String korisnickoIme, @HeaderParam("lozinka") String lozinka) {
        Dnevnik dnevnik = new Dnevnik();
        boolean uspjesno = true;
        String poruka = "";
        int idParkiralista = Integer.parseInt(id);

        try {
            autentificirajKorisnika(korisnickoIme, lozinka);
            
            if(parkiralisteImaVozila(idParkiralista)){
                uspjesno = false;
                poruka = "Parkiraliste ima vozila!";
            } else {
                BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
                if (!bpo.parkiralistaSelectIdPostoji(idParkiralista)) {
                    uspjesno = false;
                    poruka = "Parkiraliste s id " + id + " ne postoji!";
                } else {
                    if (bpo.meteoSelectIdParkiralistaPostoje(idParkiralista)) {
                        uspjesno = false;
                        poruka = "Postoje meteopodaci za parkiraliste s id " + id + " !";
                    } else {
                        bpo.parkiralistaDelete(idParkiralista);
                        obrisiParkiralisteNaServisu(idParkiralista);
                        dnevnik.postaviUspjesanStatus();
                    }
                }
                bpo.zatvoriVezu();            
            }            
        } catch (SQLException | ClassNotFoundException | JsonSyntaxException ex) {
            uspjesno = false;
            poruka = ex.toString();
            Logger.getLogger(ParkiranjeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            uspjesno = false;
            poruka = ex.getMessage();
        }
        
        dnevnik.zavrsiISpremiDnevnik(korisnickoIme, vratiTrenutnuAdresuZahtjeva());

        JsonOdgovor jsonOdgovor = new JsonOdgovor(uspjesno, poruka);
        return jsonOdgovor.vratiKompletanJsonOdgovor();
    }
    
    /**
     * Briše parkiralište na web servisu
     * @param idParkiralista 
     */
    private void obrisiParkiralisteNaServisu(int idParkiralista) throws Exception{
        KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        boolean rezultat = ParkiranjeWSKlijent.obrisiParkiralisteGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka(), idParkiralista);
        
        if(rezultat==false){
            throw new Exception("Neuspješno dodavanje na servis");
        }
    }
    
    /**
     * Sinkronizira parkirališta u bazi podataka sa onima na web servisu.
     * 1. Briše sva parkiralište grupe na web servisu
     * 2. Dohvaća sva parkirališta iz baze podataka
     * 3. Svako parkiralište iz baze podataka dodaje na web servis
     */
    private void sinkronizirajParkiralista() {        
        KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn();
        ParkiranjeWSKlijent.obrisiSvaParkiralistaGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka());
        
        List<Parkiraliste> svaParkiralista = new ArrayList<>();
        try {
            BazaPodatakaOperacije bpo = new BazaPodatakaOperacije();
            svaParkiralista = bpo.parkiralistaSelectSvaParkiralista();
            bpo.zatvoriVezu();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ParkiranjeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Parkiraliste parkBP : svaParkiralista) {
            org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste parkWS = preslikajParkiraliste(parkBP);
            ParkiranjeWSKlijent.dodajParkiralisteGrupi(korisnik.getKorisnickoIme(), korisnik.getLozinka(), parkWS);
        }
    }  
    
    /**
     * Prepisuje vrijednosti iz objekta kakav se dohvaća iz baze podataka u objekt koji se koristi na web servisu.
     * @param parkBP parkiralište dohvaćeno iz baze podataka
     * @return parkiralište za web servis
     */
    private org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste preslikajParkiraliste(Parkiraliste parkBP){
        org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste parkWS = new org.foi.nwtis.damdrempe.ws.klijenti.Parkiraliste();
        parkWS.setId(parkBP.getId());
        parkWS.setNaziv(parkBP.getNaziv());
        parkWS.setAdresa(parkBP.getAdresa());        
        parkWS.setKapacitet(parkBP.getKapacitet());

        org.foi.nwtis.damdrempe.ws.klijenti.Lokacija lokacija = new org.foi.nwtis.damdrempe.ws.klijenti.Lokacija();
        lokacija.setLatitude(parkBP.getGeoloc().getLatitude());
        lokacija.setLongitude(parkBP.getGeoloc().getLongitude());
        parkWS.setGeoloc(lokacija);
        
        StatusParkiralista status = prepoznajStatus(parkBP.getStatus());
        parkWS.setStatus(status);
        
        return parkWS;        
    }
    
    /**
     * Pretvara status u obliku stringa iz baze podataka u status iz enumeracije koji se koriste na servisu.
     * @param statusString status u obliku stringa
     * @return status iz enumeracije
     */
    private StatusParkiralista prepoznajStatus(String statusString){
        StatusParkiralista statusEnum = StatusParkiralista.PASIVAN;
        switch (statusString) {
            case "PASIVAN":
                statusEnum = StatusParkiralista.PASIVAN;
                break;
            case "AKTIVAN":
                statusEnum = StatusParkiralista.AKTIVAN;
                break;
            case "BLOKIRAN":
                statusEnum = StatusParkiralista.BLOKIRAN;
                break;
            case "NEPOSTOJI":
                statusEnum = StatusParkiralista.NEPOSTOJI;
                break;
            default:
                break;
        }
        
        return statusEnum;
    }
    
    /**
     * Vraća url adresu zahtjeva.
     * @return url adresa zahtjeva
     */
    private String vratiTrenutnuAdresuZahtjeva(){
        String adresa = context.getRequestUri().toString() + "/" + request.getMethod();
        return adresa;
    }
    
    /**
     * Autentificira korisnika prema bazi podataka
     * @param korisnickoIme
     * @param lozinka
     * @throws Exception 
     */
    private void autentificirajKorisnika(String korisnickoIme, String lozinka) throws Exception{
        if(PomocnaKlasa.autentificirajKorisnika(korisnickoIme, lozinka) == false){
            throw new Exception("Neovlasten pristup! Pogresno korisnicko ime i/ili lozinka.");
        }
    }
    
    /**
     * Vraća HTTP Status 405 - Method Not Allowed odgovor.
     * Zapisuje poziv operacije u dnevnik.
     * @return 
     */
    private Response dajOdgovorZaNedozvoljenPristup(){
        Dnevnik dnevnik = new Dnevnik();        
        dnevnik.zavrsiISpremiDnevnik("gost", vratiTrenutnuAdresuZahtjeva());
        
        Response.ResponseBuilder rb = Response.status(Response.Status.METHOD_NOT_ALLOWED);
        Response response = rb.build();
        return response;
    }
    
    /**
     * Provjerava da li parkiralište ima vozila.
     * @param idParkiraliste
     * @return true ako ima, inace false
     */
    private boolean parkiralisteImaVozila(int idParkiraliste){
        KorisnikPodaci korisnik = PomocnaKlasa.dohvatiKorisnickePodatkeZaSvn(); 
        List<Vozilo> listaVozila = ParkiranjeWSKlijent.dajSvaVozilaParkiralistaGrupe(korisnik.getKorisnickoIme(), korisnik.getLozinka(), idParkiraliste);
        
        if (listaVozila.isEmpty()){
            return false;
        }
        
        return true;
    }
}
