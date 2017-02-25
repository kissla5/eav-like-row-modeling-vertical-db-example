package hu.kissla5.poc.vdb.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kissla
 */
@Entity
@Table(name = "clinic_record")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClinicRecord.findAll", query = "SELECT c FROM ClinicRecord c"),
    @NamedQuery(name = "ClinicRecord.findById", query = "SELECT c FROM ClinicRecord c WHERE c.id = :id"),
    @NamedQuery(name = "ClinicRecord.findByCheckDate", query = "SELECT c FROM ClinicRecord c WHERE c.checkDate = :checkDate"),
    @NamedQuery(name = "ClinicRecord.findByPatientId", query = "SELECT c FROM ClinicRecord c WHERE c.patientId = :patientId")})
public class ClinicRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "check_date")
    @Temporal(TemporalType.DATE)
    private Date checkDate;
    @Basic(optional = false)
    @Column(name = "patient_id")
    private long patientId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicRecordId", fetch= FetchType.EAGER)
    private Collection<ClinicRecordParameter> clinicRecordParameterCollection;


    public ClinicRecord() {
    }

    public ClinicRecord(Long id) {
        this.id = id;
    }

    public ClinicRecord(Long id, long patientId) {
        this.id = id;
        this.patientId = patientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    @XmlTransient
    public Collection<ClinicRecordParameter> getClinicRecordParameterCollection() {
        if (clinicRecordParameterCollection == null) {
            clinicRecordParameterCollection = new ArrayList<>();
        }
                
        return clinicRecordParameterCollection;
    }

    public void setClinicRecordParameterCollection(Collection<ClinicRecordParameter> clinicRecordParameterCollection) {
        this.clinicRecordParameterCollection = clinicRecordParameterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClinicRecord)) {
            return false;
        }
        ClinicRecord other = (ClinicRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ClinicRecord{" + "id=" + id + ", checkDate=" + checkDate + ", patientId=" + patientId + '}';
    }
    
}
