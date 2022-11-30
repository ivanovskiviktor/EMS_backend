package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTrackingFormHelperFront implements Comparable<EmployeeTrackingFormHelperFront>{

    private Long id;
    private String workingItemName;
    private String organizationalDepCode;
    private String employeeTrackingFormDescription;
    private Long creatorId;
    private String creatorName;
    private String creatorSurname;
    private String startDate;
    private String endDate;
    private String statusName;
    private Integer value;
    private Long statusId;
    private Long organizationalDepartmentId;
    private Long workingItemId;
    private Boolean hasPreviousReport;
    private Boolean hasReport;
    private Long reportId;
    private String title;
    private Boolean isAssignedToMe;
    private List<String> emails;
    private List<String> nameSurnames;
    private OffsetDateTime dateModified;

    @Override
    public int compareTo(EmployeeTrackingFormHelperFront o) {
        if(o.getId().equals(id)){
            return 1;
        }
        return 0;
    }

}
