package graduatethesis.performancemonitoringsystem.model.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilter {

    private Long employeeTrackingFormId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String description;
    private String submitterEmail;
    private String approverEmail;

    public String getDescriptionToLower(){
        if(this.description!=null){
            return this.description.toLowerCase();
        }
        return null;
    }
}

