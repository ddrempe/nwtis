/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.damdrempe.ejb.eb.Parkiralista;

/**
 *
 * @author grupa_2
 */
@Stateless
public class ParkiralistaFacade extends AbstractFacade<Parkiralista> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParkiralistaFacade() {
        super(Parkiralista.class);
    }
    
}
