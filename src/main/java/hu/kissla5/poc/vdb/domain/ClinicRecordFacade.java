package hu.kissla5.poc.vdb.domain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author kissla
 */
@Stateless
public class ClinicRecordFacade extends AbstractFacade<ClinicRecord> {

    private final EntityManager em;

    public ClinicRecordFacade(EntityManager em) {
        super(ClinicRecord.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
