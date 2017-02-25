package hu.kissla5.poc.vdb;

import hu.kissla5.poc.vdb.domain.ClinicRecord;
import hu.kissla5.poc.vdb.domain.ClinicRecordFacade;
import hu.kissla5.poc.vdb.domain.ClinicRecordParameter;
import hu.kissla5.poc.vdb.domain.RestrictionDTO;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author kissla
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClinicRecordFacade clinicRecordFacade = new ClinicRecordFacade(Persistence.createEntityManagerFactory("hu.kissla5.poc_eav-like-row-modeling-vertical-db-example_jar_1.0.0-SNAPSHOTPU").createEntityManager());
        List<ClinicRecord> clinicRecords = clinicRecordFacade.findAll();
        for (ClinicRecord clinicRecord : clinicRecords) {
            System.out.print(clinicRecord);
            for (ClinicRecordParameter clinicRecordParameter : clinicRecord.getClinicRecordParameterCollection()) {
                System.out.print(" | " + clinicRecordParameter);
            }
            System.out.println("");
        }
        
        //new RestrictionDTO(RestrictionDTO.RestrictionType.LIKE, "Temperature", "36")
        List<ClinicRecord> findByParameters = clinicRecordFacade.findByParameters(getParameterJoinRestriction("Temperature", "37", true, "clinicRecordParameterCollection"));
        for (ClinicRecord findByParameter : findByParameters) {
            System.out.println(findByParameter);
            for (ClinicRecordParameter clinicRecordParameter : findByParameter.getClinicRecordParameterCollection()) {
                System.out.print(" | " + clinicRecordParameter);
            }
        }
    }
    
    private static RestrictionDTO getParameterJoinRestriction(String filterColumnId, String columnFilterExpression, boolean orderRestriction, String joinColumn) {
        // set join sub restriction
        RestrictionDTO<String> parameterRestrictionDTO = new RestrictionDTO<String>(RestrictionDTO.RestrictionType.AND);
        parameterRestrictionDTO.setJoinColumn(joinColumn);
        parameterRestrictionDTO.getFilterMap().put(filterColumnId + "_name",
                new RestrictionDTO<String>(RestrictionDTO.RestrictionType.EQUAL, "key", filterColumnId, joinColumn));
        parameterRestrictionDTO.getFilterMap().put(filterColumnId + "_value",
                new RestrictionDTO<String>(RestrictionDTO.RestrictionType.LIKE, "value", columnFilterExpression, joinColumn));
//        parameterRestrictionDTO.getFilterMap().put("deleted",
//                new RestrictionDTO<Boolean>(RestrictionDTO.RestrictionType.EQUAL, "deleted", false));
        return parameterRestrictionDTO;
    }
    
}
