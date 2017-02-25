package hu.kissla5.poc.vdb.domain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author kissla
 */
@Stateless
public class ClinicRecordParameterFacade extends AbstractParameterFacade<ClinicRecordParameter> {

    private final EntityManager em;

    public ClinicRecordParameterFacade(EntityManager em) {
        super(ClinicRecordParameter.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
