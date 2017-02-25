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

/**
 * Class for represent an order of entities.
 * @author kissla
 */
public class OrderDTO {
    private OrderType type;
    private String expression;
    private boolean joinedColumn;
    private boolean forceFullSort = true;

    public OrderDTO() {
    }

    public OrderDTO(OrderType type, String expression) {
        this.type = type;
        this.expression = expression;
    }

    public OrderDTO(OrderType type, String expression, boolean joinedColumn) {
        this.type = type;
        this.expression = expression;
        this.joinedColumn = joinedColumn;
    }

    public OrderDTO(OrderType type, String expression, boolean joinedColumn, boolean forceFullSort) {
        this.type = type;
        this.expression = expression;
        this.joinedColumn = joinedColumn;
        this.forceFullSort = forceFullSort;
    }

    public String getExpression() {
        return expression;
    }

    public OrderType getType() {
        return type;
    }

    public enum OrderType {
        ASC, DESC;
    }

    public boolean isJoinedColumn() {
        return joinedColumn;
    }

    public void setJoinedColumn(boolean joinedColumn) {
        this.joinedColumn = joinedColumn;
    }

    public boolean isForceFullSort() {
        return forceFullSort;
    }

    public void setForceFullSort(boolean forceFullSort) {
        this.forceFullSort = forceFullSort;
    }

    @Override
    public String toString() {
        return "OrderDTO{" + "type=" + type + ", expression=" + expression + ", joinedColumn=" + joinedColumn + ", forceFullSort=" + forceFullSort + '}';
    }
}
