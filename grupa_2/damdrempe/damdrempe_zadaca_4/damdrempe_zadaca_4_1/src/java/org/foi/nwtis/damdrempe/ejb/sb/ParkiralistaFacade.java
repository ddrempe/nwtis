package org.foi.nwtis.damdrempe.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.damdrempe.ejb.eb.Parkiralista;

/**
 * Parkiraliste fasada.
 * @author ddrempetic
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
