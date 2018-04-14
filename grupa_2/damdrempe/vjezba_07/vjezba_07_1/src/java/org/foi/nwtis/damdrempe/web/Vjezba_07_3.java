package org.foi.nwtis.damdrempe.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author grupa_2
 */
public class Vjezba_07_3 extends HttpServlet {
    DretvaPromjena dp;
    
    @Override
    public void init() throws ServletException {
        SlusacPromjena sp = new SlusacPromjena();
        int broj = Integer.parseInt(getInitParameter("interval"));
        String klasa = getInitParameter("klasa");
        
        dp = new DretvaPromjena(sp, klasa, broj);
        dp.start();
        
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        dp.interrupt();
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
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
