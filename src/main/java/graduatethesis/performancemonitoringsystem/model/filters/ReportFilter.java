package graduatethesis.performancemonitoringsystem.model.filters;

import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ReportFilter {

    private Long employeeTrackingFormId;

    private LocalDate startDate;

    private OffsetDateTime endDate;

    private String description;

    private String submitterEmail;

    private String approverEmail;

    private String submitterFirstNameLastName;

    private String approverFirstNameLastName;

    public String getDescriptionToLower(){
        if(this.description!=null){
            return this.description.toLowerCase();
        }
        return null;
    }

    public String getsubmitterFirstNameLastNameToLower(){
        if(this.submitterFirstNameLastName!=null){
            return this.submitterFirstNameLastName.toLowerCase();
        }
        return null;
    }

    public String getapproverFirstNameLastNameToLower(){
        if(this.approverFirstNameLastName!=null){
            return this.approverFirstNameLastName.toLowerCase();
        }
        return null;
    }

}

