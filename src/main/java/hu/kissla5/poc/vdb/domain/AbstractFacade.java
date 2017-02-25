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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.log4j.Logger;

/**
 * Common DB operations.
 * @author kissla
 * @param <T>
 */
public abstract class AbstractFacade<T> {
    private final Class<T> entityClass;
    private final static Logger logger = Logger.getLogger(AbstractFacade.class);

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createAndFlush(T entity) {
        create(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int countLikeParameterByOnClass(RestrictionDTO restriction, Class<T> entityClass) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        
        Root criteriaRoot;
        if (entityClass == null) {
            criteriaRoot = criteriaQuery.from(this.entityClass);
        } else {
            criteriaRoot = criteriaQuery.from(entityClass);
        }
        
        Join join = null;

        criteriaQuery.select(criteriaBuilder.count(criteriaRoot));

        criteriaQuery.where(getRestrictions(null, restriction, criteriaBuilder, criteriaRoot, criteriaQuery, join, null));

        javax.persistence.Query q = getEntityManager().createQuery(criteriaQuery);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<T> findLikeParameter(RestrictionDTO restriction, OrderDTO order, int[] range) {
        if (order != null) {
            return findLikeParameterByOnClass(restriction, order, range, entityClass);
        } else if (entityClass.getSimpleName().matches(".*Change$") || entityClass.getSimpleName().matches("^ChangeUnion$")) {
            return findLikeParameterByOnClass(restriction, new OrderDTO(OrderDTO.OrderType.DESC, "changeTime"), range, entityClass);
        } else {
            return findLikeParameterByOnClass(restriction, new OrderDTO(OrderDTO.OrderType.ASC, "id"), range, entityClass);
        }
    }
    
    public List<T> findLikeParameterByOnClass(RestrictionDTO restriction, OrderDTO order, int[] range, Class<T> entityClass) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root criteriaRoot = criteriaQuery.from(entityClass);
        Join join = null;

        criteriaQuery.select(criteriaRoot);

        criteriaQuery.where(getRestrictions(null, restriction, criteriaBuilder, criteriaRoot, criteriaQuery, join, order));

        if (order != null && !order.isJoinedColumn()) {
            setOrder(order, join, criteriaRoot, criteriaBuilder, criteriaQuery);
        }

        TypedQuery emQuery = getEntityManager().createQuery(criteriaQuery);

        setRanges(range, emQuery);

        return emQuery.getResultList();
    }

    protected void setOrder(OrderDTO order, Join join, Root criteriaRoot,
            CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery) {
        List<Order> orderList = new ArrayList<Order>();

        logger.trace(order);
        From from;

        if (order.isJoinedColumn() && join != null && join.get(order.getExpression()) != null) {
            from = join;
        } else {
            from = criteriaRoot;
        }

        switch (order.getType()) {
            case ASC:
                orderList.add(criteriaBuilder.asc(from.get(order.getExpression())));

                if (order.isForceFullSort()) {
                    orderList.add(criteriaBuilder.asc(from.get("id")));
                }
                break;
            case DESC:
                orderList.add(criteriaBuilder.desc(from.get(order.getExpression())));

                if (order.isForceFullSort()) {
                    orderList.add(criteriaBuilder.desc(from.get("id")));
                }
                break;
            default:
                logger.warn("Unkown short type: " + order.getType());
        }

        if (!orderList.isEmpty()) {
            criteriaQuery.orderBy(orderList);
        }
    }

    protected void setRanges(int[] range, TypedQuery emQuery) {
        if (range != null && range.length == 2) {
            logger.trace("Range: " + range[1] + " - " + range[0]);
            emQuery.setMaxResults(range[1] - range[0]);
            emQuery.setFirstResult(range[0]);
        }
    }

    public List<T> findByParameters(RestrictionDTO restriction) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root criteriaRoot = criteriaQuery.from(entityClass);
        Join join = null;

        criteriaQuery.select(criteriaRoot);

        criteriaQuery.where(getRestrictions(null, restriction, criteriaBuilder, criteriaRoot, criteriaQuery, join, null));

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    protected Predicate getRestrictions(String key, RestrictionDTO<?> restriction,
            CriteriaBuilder criteriaBuilder, Root criteriaRoot, CriteriaQuery c,
            Join join, OrderDTO orderDTO) throws RuntimeException {
        Predicate predicate = null;
        Expression expression;
        String columnName;

        if (restriction.getColumnName() != null) {
            columnName = restriction.getColumnName();
        } else {
            columnName = key;
        }

        logger.debug("Restriction:" + restriction);

        if (restriction.getType() != null) {
            if (restriction.getType().isComperationOperator()) {
                if (restriction.getJoinColumn() != null && !"".equals(restriction.getJoinColumn())) {
                    expression = join.get(columnName);
                } else {
                    expression = criteriaRoot.get(columnName);
                }

                if (orderDTO != null && orderDTO.isJoinedColumn() && restriction.isOrderRestriction()) {
                    setOrder(orderDTO, join, criteriaRoot, criteriaBuilder, c);
                }
            } else {
                expression = null;
            }

            switch (restriction.getType()) {
                case AND:
                    predicate = criteriaBuilder.and(getSubRestrictions(restriction, criteriaBuilder, criteriaRoot, c, join, orderDTO).toArray(new Predicate[0]));
                    break;
                case OR:
                    predicate = criteriaBuilder.or(getSubRestrictions(restriction, criteriaBuilder, criteriaRoot, c, join, orderDTO).toArray(new Predicate[0]));
                    break;
                case EQUAL:
                    predicate = criteriaBuilder.equal(expression, restriction.getColumnValue());
                    break;
                case LIKE:
                    predicate = criteriaBuilder.like(expression, "%" + restriction.getColumnValue()/*.toUpperCase()*/ + "%");
                    break;
                case NE:
                    predicate = criteriaBuilder.notEqual(expression, restriction.getColumnValue());
                    break;
                // filtered element have to convert to number!
                case GT:
                    predicate = criteriaBuilder.gt(expression, Double.valueOf(restriction.getColumnValue().toString()));
                    break;
                case LT:
                    predicate = criteriaBuilder.lt(expression, Double.valueOf(restriction.getColumnValue().toString()));
                    break;
                default:
                    throw new RuntimeException("Unkown restriction type!");
            }
        } else {
            throw new RuntimeException("Unkown restriction type!");
        }

        return predicate;
    }

    private List<Predicate> getSubRestrictions(RestrictionDTO<?> restriction,
            CriteriaBuilder criteriaBuilder, Root criteriaRoot, CriteriaQuery c,
            Join join, OrderDTO orderDTO) throws RuntimeException {
        List<Predicate> restrictions = new ArrayList<Predicate>();
        // subquery
        if (restriction.getSubqueryClass() != null) {
            Subquery subQuery = c.subquery(restriction.getSubqueryClass());
            Root subRoot = subQuery.from(restriction.getSubqueryClass());
            subQuery.select(subRoot);

            List<Predicate> subRestrictions = new ArrayList<Predicate>();
            if (restriction.getFilterMap() != null && !restriction.getFilterMap().isEmpty()) {
                if (logger.isTraceEnabled()) {
                    logger.trace("subquery filterMap:" + restriction.getFilterMap());
                    logger.trace("subquery joinColumn:" + restriction.getJoinColumn());
                }

                for (Map.Entry<String, RestrictionDTO<?>> entry : restriction.getFilterMap().entrySet()) {
                    Predicate entryPredicate = getRestrictions(entry.getKey(), entry.getValue(), criteriaBuilder, subRoot, c, join, orderDTO);

                    subRestrictions.add(entryPredicate);
                }
            }

            subQuery.where(subRestrictions.toArray(new Predicate[0]));

            restrictions.add(criteriaBuilder.in(criteriaRoot.get(restriction.getJoinColumn())).value(subQuery));

        // join query
        } else if (restriction.getJoinColumn() != null) {
            join = criteriaRoot.join(restriction.getJoinColumn());

            if (restriction.getFilterMap() != null && !restriction.getFilterMap().isEmpty()) {
                if (logger.isTraceEnabled()) {
                    logger.trace("join query filterMap:" + restriction.getFilterMap());
                    logger.trace("join query joinColumn:" + restriction.getJoinColumn());
                }

                for (Map.Entry<String, RestrictionDTO<?>> entry : restriction.getFilterMap().entrySet()) {
                    Predicate entryPredicate = getRestrictions(entry.getKey(), entry.getValue(), criteriaBuilder, criteriaRoot, c, join, orderDTO);
                    restrictions.add(entryPredicate);
                }
            }

        // normal query with restrictions
        } else if (restriction.getFilterMap() != null && !restriction.getFilterMap().isEmpty()) {
            if (logger.isTraceEnabled()) {
                logger.trace("filterMap:" + restriction.getFilterMap());
                logger.trace("joinColumn:" + restriction.getJoinColumn());
            }

            for (Map.Entry<String, RestrictionDTO<?>> entry : restriction.getFilterMap().entrySet()) {
                Predicate entryPredicate = getRestrictions(entry.getKey(), entry.getValue(), criteriaBuilder, criteriaRoot, c, join, orderDTO);

                restrictions.add(entryPredicate);
            }
        }

        return restrictions;
    }
}
