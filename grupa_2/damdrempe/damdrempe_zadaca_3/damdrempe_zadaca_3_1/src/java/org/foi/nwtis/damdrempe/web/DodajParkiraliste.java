/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.damdrempe.rest.klijenti.GMKlijent;
import org.foi.nwtis.damdrempe.rest.klijenti.OWMKlijent;
import org.foi.nwtis.damdrempe.web.podaci.Lokacija;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;

/**
 *
 * @author grupa_2
 */
@WebServlet(name = "DodajParkiraliste", urlPatterns = {"/DodajParkiraliste"})
public class DodajParkiraliste extends HttpServlet {

    private String naziv = "";
    private String adresa = "";
    private Lokacija lokacija;

    private String geolokacija = "";
    private String meteo = "";
    private String obavijest = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //čitanje iz requesta
        naziv = request.getParameter("naziv");
        adresa = request.getParameter("adresa");

        String gumbGeolokacija = request.getParameter("geolokacija");
        String gumbSpremi = request.getParameter("spremi");
        String gumbMeteo = request.getParameter("meteo");

        //obrada akcija
        if (gumbGeolokacija != null && !gumbGeolokacija.isEmpty()) {
            postaviGeoLokaciju();
        } else if (gumbSpremi != null && !gumbSpremi.isEmpty()) {
            spremi();           
        } else if (gumbMeteo != null && !gumbMeteo.isEmpty()) {
            meteo();
        }

        //postavljanje podatka za vracanje odgovora
        if (!naziv.isEmpty() && !adresa.isEmpty()) {
            request.setAttribute("naziv", naziv);
            request.setAttribute("adresa", adresa);
        }

        if (!geolokacija.isEmpty()) {
            request.setAttribute("geolokacija", geolokacija);
        }
        
        if (!meteo.isEmpty()) {
            request.setAttribute("meteo", meteo);
        }

        request.setAttribute("obavijest", obavijest);

        RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
        rd.forward(request, response);
    }

    private void postaviGeoLokaciju() {
        if(adresa == null || adresa.isEmpty()){
            obavijest = "Niste upisali adresu!";
            return;
        }
        
        GMKlijent gmk = new GMKlijent("AIzaSyB1My2HHb8rRuQ35EUnPbwM2LOM1D5eItg");   //TODO u konfig
        lokacija = gmk.getGeoLocation(adresa);

        geolokacija = lokacija.getLatitude() + "," + lokacija.getLongitude();

        obavijest = "Geolokacija je uspjesno dohvacena i prikazana u polju!";
    }

    private void spremi() {
        if(lokacija == null){
            obavijest = "Nije moguce spremanje! Niste dohvatili lokaciju!";
            return;
        }
        
        BP_Konfiguracija bpk = (BP_Konfiguracija) this.getServletContext().getAttribute("BP_Konfig");

        if (bpk == null) {
            System.err.println("Problem s konfiguracijom!");
            return;
        }

        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String korisnik = bpk.getUserUsername();
        String lozinka = bpk.getUserPassword();
        String upit = "INSERT INTO parkiralista(naziv, adresa, latitude, longitude)"
                + "VALUES('" + naziv + "','" + adresa + "',"
                + lokacija.getLatitude() + "," + lokacija.getLongitude() + ")";
        try {
            Class.forName(bpk.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        try (
                Connection con = DriverManager.getConnection(url, korisnik, lozinka);
                Statement stmt = con.createStatement();) {
            stmt.execute(upit);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

        obavijest = "Podaci su spremljeni u bazu podataka";
    }

    private void meteo() {
        if(lokacija == null){
            obavijest = "Nije moguce dohvacanje meteo podataka! Niste dohvatili lokaciju!";
            return;
        }
        
        OWMKlijent owmk = new OWMKlijent("eeab428a2e33536c5bb6deb266b37fcd"); //TODO iz konfiga
        MeteoPodaci mp = owmk.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());
        
        meteo = "Temp: " + mp.getTemperatureValue().toString() + mp.getTemperatureUnit() + "<br/>" +
                "Vlaga: " + mp.getHumidityValue().toString() + mp.getHumidityUnit() + "<br/>" +
                "Tlak: " + mp.getPressureValue().toString() + mp.getPressureUnit();
        
        obavijest = "Dohvaceni su meteo podaci";
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
