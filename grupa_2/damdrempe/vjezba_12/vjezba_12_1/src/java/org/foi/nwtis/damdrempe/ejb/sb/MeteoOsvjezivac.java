/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ejb.sb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.xml.ws.WebServiceRef;
import org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS_Service;

/**
 *
 * @author grupa_2
 */
@Singleton
@LocalBean
public class MeteoOsvjezivac {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8084/damdrempe_zadaca_3_1/GeoMeteoWS.wsdl")
    private GeoMeteoWS_Service service;

    @Resource(mappedName = "jms/NWTiS_vjezba_12")
    private Queue nWTiS_vjezba_12;

    @Inject
    @JMSConnectionFactory("jms/NWTiS_QF_vjezba_12")
    private JMSContext context;
    
    private List<MeteoPrognosticar> meteoprognosticari = new ArrayList<>();

    @Schedule(hour = "*", minute = "*/2")
    
    public void myTimer() {
        System.out.println("Timer event: " + new Date());
        
        for(MeteoPrognosticar meteoPrognosticar : meteoprognosticari){
        
        }
        
        List<org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci> mps = new ArrayList<>();        
        List<String> parkiralista = meteoPrognosticar.dajParkiralista();
        
        for (String id : parkiralista) {
            List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> wsmps = dajSveMeteoPodatkeZaParkiraliste(Integer.parseInt(id));
            
            for (org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci wsmp:wsmps) {
                org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci mp = new org.foi.nwtis.damdrempe.web.podaci.MeteoPodaci();
                mp.setTemperatureValue(wsmp.getTemperatureValue());
                mp.setPressureValue(wsmp.getPressureValue());
                mp.setWindSpeedValue(wsmp.getWindSpeedValue());
                
                mps.add(mp);
            }
        }
        //meteoPrognosticar.setMeteoPodaci(mps); //TODO popraviti/provjeriti
    }
    
    
    
    //TODOkorak 11 napraviti poziv operacije 
    //dajSveMeteoPodatkeZaParkiraliste na web servisu GeoMeteoWS (Insert Code/Call Web Service Operation...)

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void sendJMSMessageToNWTiS_vjezba_12(String messageData) {
        context.createProducer().send(nWTiS_vjezba_12, messageData);
    }

    //TODO dali to treba biti tu
    private java.util.List<org.foi.nwtis.damdrempe.ws.serveri.MeteoPodaci> dajSveMeteoPodatkeZaParkiraliste(int arg0) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.foi.nwtis.damdrempe.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatkeZaParkiraliste(arg0);
    }

    public void setMeteoprognosticari(MeteoPrognosticar meteoprognosticar) {        
        this.meteoprognosticari.add(meteoprognosticar);
    }
    
    
}
