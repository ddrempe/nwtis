/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.ejb.sb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.damdrempe.ejb.eb.Dnevnik;

/**
 *
 * @author grupa_2
 */
@Stateless
public class DnevnikFacade extends AbstractFacade<Dnevnik> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DnevnikFacade() {
        super(Dnevnik.class);
    }
    
    public List<Dnevnik> findFiltered(String ipAdresa, Integer trajanje, String url, Date odVrijeme, Date doVrijeme) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Dnevnik> cq = cb.createQuery(Dnevnik.class);
        Root<Dnevnik> root = cq.from(Dnevnik.class);

        List<Predicate> predicates = new ArrayList<>();
        
        if (ipAdresa != null && !ipAdresa.isEmpty()) {
            predicates.add(cb.equal(root.get("ipadresa"), ipAdresa));
        }
        if (trajanje != null && trajanje != 0) {
            predicates.add(cb.equal(root.get("trajanje"), trajanje));
        }        
        if (url != null && !url.isEmpty()) {
            predicates.add(cb.equal(root.get("url"), url));
        }
        
        if (odVrijeme != null) {
            predicates.add(cb.greaterThan(root.get("vrijeme").as(Date.class), odVrijeme));
        }
        
        if (doVrijeme != null) {
            predicates.add(cb.lessThan(root.get("vrijeme").as(Date.class), doVrijeme));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        List<Dnevnik> dnevnici = em.createQuery(cq).getResultList();
        return dnevnici;
    }
    
}
