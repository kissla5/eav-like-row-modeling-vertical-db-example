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

import java.util.HashMap;
import java.util.Map;

/**
 * Class for represent a restriction for the entity selection
 * @author kissla
 * @param <T>
 */
public class RestrictionDTO<T> {

    private RestrictionType type;
    private boolean orderRestriction = false;
    private String joinColumn;
    private String columnName;
    private T columnValue;
    private Class subqueryClass;
    private final Map<String, RestrictionDTO<?>> filterMap = new HashMap<>();

    public RestrictionDTO() {
    }

    public RestrictionDTO(RestrictionType type) {
        this.type = type;
    }

    public RestrictionDTO(RestrictionType type, String columnName, T columnValue) {
        this.type = type;
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public RestrictionDTO(RestrictionType type, String columnName, T columnValue,
            String joinColumn) {
        this.type = type;
        this.joinColumn = joinColumn;
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public RestrictionDTO(RestrictionType type, boolean orderRestriction, String columnName, T columnValue,
            String joinColumn) {
        this.type = type;
        this.orderRestriction = orderRestriction;
        this.joinColumn = joinColumn;
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public RestrictionType getType() {
        return type;
    }

    public void setType(RestrictionType type) {
        this.type = type;
    }

    public String getJoinColumn() {
        return joinColumn;
    }

    public void setJoinColumn(String joinColumn) {
        this.joinColumn = joinColumn;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public T getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(T columnValue) {
        this.columnValue = columnValue;
    }

    public Class getSubqueryClass() {
        return subqueryClass;
    }

    public void setSubqueryClass(Class subqueryClass) {
        this.subqueryClass = subqueryClass;
    }

    public Map<String, RestrictionDTO<?>> getFilterMap() {
        return filterMap;
    }

    public boolean isOrderRestriction() {
        return orderRestriction;
    }

    @Override
    public String toString() {
        return "RestrictionDTO{" + "type=" + type + ", orderRestriction=" + orderRestriction + ", joinColumn=" + joinColumn + ", columnName=" + columnName + ", columnValue=" + columnValue + ", filterMap=" + filterMap + '}';
    }

    public static <T> RestrictionDTO<T> valueOfWithDeletedFilter() {
        RestrictionDTO<T> restrictionDTO = new RestrictionDTO<T>(RestrictionType.AND);

        restrictionDTO.getFilterMap().put("deleted", new RestrictionDTO<Boolean>(RestrictionType.EQUAL, "deleted", false));

        return restrictionDTO;
    }

    public static <T> RestrictionDTO valueOfWithDeletedFilter(RestrictionType type, String columnName,
            T columnValue) {
        RestrictionDTO<T> restrictionDTO = new RestrictionDTO<T>(RestrictionType.AND);

        restrictionDTO.getFilterMap().put("deleted", new RestrictionDTO<Boolean>(RestrictionType.EQUAL, "deleted", false));
        restrictionDTO.getFilterMap().put(columnName, new RestrictionDTO<T>(type, columnName, columnValue));

        return restrictionDTO;
    }

    public static <T> RestrictionDTO valueOfWithDeletedFilter(RestrictionType type, String joinColumn,
            String columnName, T columnValue) {
        RestrictionDTO<T> restrictionDTO = new RestrictionDTO<T>(RestrictionType.AND);

        restrictionDTO.getFilterMap().put("deleted", new RestrictionDTO<Boolean>(RestrictionType.EQUAL, "deleted", false));
        restrictionDTO.getFilterMap().put(columnName, new RestrictionDTO<T>(type, columnName, columnValue, joinColumn));

        return restrictionDTO;
    }

    public enum RestrictionType {
        EQUAL("=", true),
        LIKE("~", true),
        NE("!=", true),
        NL("!~", true),
        GT(">", true),
        LT("<", true),
        AND("&&", false),
        OR("||", false);
        
        private final String operator;
        private final boolean comperationOperator;

        private RestrictionType(String operator, boolean comperationOperator) {
            this.operator = operator;
            this.comperationOperator = comperationOperator;
        }

        public String getOperator() {
            return operator;
        }

        public boolean isComperationOperator() {
            return comperationOperator;
        }
    }
}
