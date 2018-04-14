package org.foi.nwtis.matnovak.web.kontrole;

import java.io.IOException;
import java.rmi.ServerException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Kontroler", urlPatterns = {"/Kontroler", "/PrijavaKorisnika"
                                        ,"/OdjavaKorisnika","/ProvjeraKorisnika"
                                        ,"/IspisPodataka","/IspisAktivnihKorisnika",
                                        "/IspisKorisnika"})
public class Kontroler extends HttpServlet {

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

        String url = request.getServletPath();
        String odlazniUrl = "";

        System.out.println("Ulazni zahtjev" + url);

        switch (url) {
            case "/Kontroler":
                odlazniUrl = "/jsp/index.html";
                break;
            case "/PrijavaKorisnika":
                odlazniUrl = "/jsp/login.jsp";
                break;
            case "/OdjavaKorisnika":
                odlazniUrl = "/Kontroler";
                HttpSession sesija1 = request.getSession();
                sesija1.removeAttribute("NWTiS_korisnik");
                sesija1.invalidate();
                break;
            case "/ProvjeraKorisnika":
                //TODO provjerava u tablici polaznici i ako je uspješno
                String korime = request.getParameter("korime");
                String lozinka = request.getParameter("lozinka");;
                if(korime.equalsIgnoreCase("admin")
                        && lozinka.equalsIgnoreCase("lozinka")){
                    
                    HttpSession sesija = request.getSession();
                    String id=sesija.getId();
                    String ip=request.getRemoteAddr();
                    
                    Korisnik k = new Korisnik(korime, "Matija", "Novak", 
                            id, 0, ip);
                    
                    sesija.setAttribute("NWTiS_korisnik", k);
                    
                    odlazniUrl = "/IspisPodataka";
                } else {
                    throw new NeuspjesnaPrijava("Prijava nije uspješna!");
                }
                break;
            case "/IspisPodataka":
                odlazniUrl = "/privatno/ispisPodataka.jsp";
                break;    
            case "/IspisAktivnihKorisnika":
                odlazniUrl = "/admin/ispisAktvnihKorisnika.jsp";
                break;
            case "/IspisKorisnika":
                odlazniUrl = "/admin/ispisKorisnika.jsp";
                break;
            default : 
                odlazniUrl = null;
        }

        if (odlazniUrl == null) {
            throw new ServerException("Nepoznata adresa preusmjeravanja: '" + url + "'!");
        } else {
            RequestDispatcher rd = this.getServletContext().getRequestDispatcher(odlazniUrl);
            rd.forward(request, response);
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
