/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.xml.ws.WebServiceRef;
import org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci;
import org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service;
import org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste;

/**
 *
 * @author grupa_2
 */
@Stateful
@LocalBean
public class MeteoPrognosticar {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8084/damdrempe_zadaca_3_1/GeoMeteoWS.wsdl")
    private GeoMeteoWS_Service service;
    private List<String> parkiralista;
    private List<MeteoPodaci> meteoPodaci;   

    public List<String> dajParkiralista() {
        return getParkiralista();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public List<String> getParkiralista() {
        parkiralista = new ArrayList<>();
        for(Parkiraliste p: dajSvaParkiralista()){
            parkiralista.add(Integer.toString(p.getId()));
        }
        
        return parkiralista;
    }

    public void setParkiralista(List<String> parkiralista) {
        this.parkiralista = parkiralista;
    }

    public List<MeteoPodaci> getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(List<MeteoPodaci> meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

    private java.util.List<org.foi.nwtis.damdrempe.ws.serveri.Parkiraliste> dajSvaParkiralista() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSvaParkiralista();
    }
    
    
}
