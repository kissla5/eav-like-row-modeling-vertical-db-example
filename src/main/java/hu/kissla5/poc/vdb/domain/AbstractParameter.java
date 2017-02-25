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

import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kissla
 * @param <PI>
 * @param <PC>
 */
@MappedSuperclass
public abstract class AbstractParameter<PI, PC> {
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PARAMETER_KEY")
    private String key;
    @Size(max = 255)
    @Column(name = "PARAMETER_VALUE")
    private String value;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DELETED")
    private boolean deleted;

    public AbstractParameter() {
    }

    public AbstractParameter(Long id) {
        this.id = id;
    }

    public AbstractParameter(Long id, String name, boolean deleted) {
        this.id = id;
        this.key = name;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public abstract Collection<PC> getParameterChangeCollection();
    
    public abstract PI getParentId();
    
    public abstract void setParentId(PI pi);

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 17 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 17 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 17 * hash + (this.deleted ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractParameter<PI, PC> other = (AbstractParameter<PI, PC>) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
            return false;
        }
        if (this.deleted != other.deleted) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AbstractParameter{" + "id=" + id + ", key=" + key + ", value=" + value + ", deleted=" + deleted + '}';
    }
}
