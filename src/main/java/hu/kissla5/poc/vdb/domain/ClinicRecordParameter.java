package hu.kissla5.poc.vdb.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kissla
 */
@Entity
@Table(name = "clinic_record_parameter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClinicRecordParameter.findAll", query = "SELECT d FROM ClinicRecordParameter d"),
    @NamedQuery(name = "ClinicRecordParameter.findById", query = "SELECT d FROM ClinicRecordParameter d WHERE d.deleted = false AND d.id = :id"),
    @NamedQuery(name = "ClinicRecordParameter.findByName", query = "SELECT d FROM ClinicRecordParameter d WHERE d.deleted = false AND d.key = :key"),
    @NamedQuery(name = "ClinicRecordParameter.findByValue", query = "SELECT d FROM ClinicRecordParameter d WHERE d.deleted = false AND d.value = :value"),
    @NamedQuery(name = "ClinicRecordParameter.findByDeleted", query = "SELECT d FROM ClinicRecordParameter d WHERE d.deleted = :deleted")})
public class ClinicRecordParameter extends AbstractParameter<ClinicRecord, List<String>> implements Serializable {
    private static final long serialVersionUID = 1234235674342L;
    @JoinColumn(name = "clinic_record_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ClinicRecord clinicRecordId;

    public ClinicRecord getClinicRecordId() {
        return clinicRecordId;
    }

    public void setClinicRecordId(ClinicRecord clinicRecordId) {
        this.clinicRecordId = clinicRecordId;
    }

    @Override
    public ClinicRecord getParentId() {
        return this.getClinicRecordId();
    }

    @Override
    public void setParentId(ClinicRecord pi) {
        this.setClinicRecordId(pi);
    }

    @Override
    public Collection<List<String>> getParameterChangeCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
