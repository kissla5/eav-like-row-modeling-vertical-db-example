/*
 * Copyright (C) 2012 kissla
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package hu.kissla5.poc.vdb.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

/**
 *
 * @author kissla
 */
public abstract class AbstractParameterFacade<T extends AbstractParameter> extends AbstractFacade<T> {

    private final static Logger logger = Logger.getLogger(AbstractFacade.class);

    public AbstractParameterFacade(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected abstract EntityManager getEntityManager();

    public List<String> getEveryParameters(RestrictionDTO restriction, OrderDTO orderDTO, int[] range, Class<T> entityClass) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root criteriaRoot = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaRoot.get(AbstractParameter_.key));
        criteriaQuery.distinct(true);

        criteriaQuery.where(getRestrictions(null, restriction, criteriaBuilder, criteriaRoot, criteriaQuery, null, orderDTO));

        if (orderDTO != null && !orderDTO.isJoinedColumn()) {
            setOrder(orderDTO, null, criteriaRoot, criteriaBuilder, criteriaQuery);
        }

        TypedQuery emQuery = getEntityManager().createQuery(criteriaQuery);
        setRanges(range, emQuery);

        return emQuery.getResultList();

    }
}
