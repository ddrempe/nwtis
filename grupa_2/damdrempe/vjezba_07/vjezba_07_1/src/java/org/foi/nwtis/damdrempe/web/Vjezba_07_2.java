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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.damdrempe.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.damdrempe.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.damdrempe.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author grupa_2
 */
@WebServlet(name = "Vjezba_07_2", urlPatterns = {"/Vjezba_07_2"}, initParams = {
    @WebInitParam(name = "konfiguracija", value = "NWTiS.db.config_1.xml")})
public class Vjezba_07_2 extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Vjezba_07_2</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Vjezba_07_2 at " + request.getContextPath() + "</h1>");
            
            String datoteka = getInitParameter("konfiguracija");
            String putanja = getServletContext().getRealPath("/WEB-INF") + java.io.File.separator;
            String puniNazivDatoteke = putanja + datoteka;
            
            try {  
            BP_Konfiguracija bpk = new BP_Konfiguracija(puniNazivDatoteke);
            
            String url = bpk.getServerDatabase() + bpk.getUserDatabase();
            String korisnik = bpk.getUserUsername();
            String lozinka = bpk.getUserPassword();
            String upit = "select kor_ime, prezime, ime from POLAZNICI";
            String uprProgram = bpk.getDriverDatabase();
            Class.forName(uprProgram);

                try (   //TODO obrati paznju na try with resources
                    Connection con = DriverManager.getConnection(url, korisnik, lozinka);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(upit);) {

                    out.println("Popis polaznika:");
                    out.println("<table border='1'>"
                            + "<tr><th>Kor.ime</th><th>Prezime</th><th>Ime</th></tr>");

                    while (rs.next()) {
                        String mb = rs.getString("kor_ime");
                        String pr = rs.getString("prezime");
                        String im = rs.getString("ime");
                        out.println("<tr><td>" + mb + "</td><td>" + pr + "</td><td>" + im + "</td></tr>");
                    }
                    out.println("</table>");
                    rs.close();
                    stmt.close();
                    con.close();                
                } catch(SQLException ex) {
                    System.err.println("SQLException: " + ex.getMessage());
                }
            
            } catch(NemaKonfiguracije | NeispravnaKonfiguracija | ClassNotFoundException ex) {
                    Logger.getLogger(Vjezba_07_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            out.println("</body>");
            out.println("</html>");
        }
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
