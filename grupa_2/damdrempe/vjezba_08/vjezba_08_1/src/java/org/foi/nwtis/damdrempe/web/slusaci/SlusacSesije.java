package org.foi.nwtis.damdrempe.web.slusaci;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.foi.nwtis.damdrempe.web.kontrole.Korisnik;

public class SlusacSesije implements HttpSessionListener, HttpSessionAttributeListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {  
        if(event.getName().compareTo("NWTiS_korisnik") == 0){
            ServletContext sc = event.getSession().getServletContext();
            
            List <Korisnik> lista = null;
            Object o = sc.getAttribute("PRIJAVLJENI_KORISNICI");
            if(o == null){
                lista = new ArrayList<>();
            }else if(o instanceof List){
                lista = (List<Korisnik>) o;
            }
            
            if(event.getValue() instanceof Korisnik){
                Korisnik k = (Korisnik) event.getValue();
                lista.add(k);
                sc.setAttribute("PRIJAVLJENI_KORISNICI", lista);
                System.out.println("Korisnik prijavljen: "+ k.getKorime());
            } else{
                System.err.println("Nepoznata klasa u sesiji korisnika!");
            }
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if(event.getName().compareTo("NWTiS_korisnik") == 0){
            ServletContext sc = event.getSession().getServletContext();
            
            List<Korisnik> lista = null;
            Object o = sc.getAttribute("PRIJAVLJENI_KORISNICI");
            if(o == null){
                lista = new ArrayList<>();
            } else if(o instanceof List) {
                lista = (List<Korisnik>) o;
            }
            
            if(event.getValue() instanceof Korisnik){
                Korisnik k = (Korisnik) event.getValue();
                
                lista.remove(k);
                sc.setAttribute("PRIJAVLJENI_KORISNICI", lista);
                System.out.println("Korisnik odjavljen:" + k.getKorime());
            } else {
                System.err.println("Nepoznata klasa u sesiji korisnika!");
            }      
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
    }
}
