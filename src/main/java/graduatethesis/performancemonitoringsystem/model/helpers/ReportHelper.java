package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportHelper {

    Long id;

    Long employeeTrackingFormId;

    String description;

    Integer hours;

    Integer minutes;

    String firstName;

    String lastName;

    OffsetDateTime submissionDate;

    String taskDescription;

    String workingItemName;

    Boolean isAccepted;

    Boolean hasPreviousReport;

    String approver;

    String submitter;

    String approverFirstNameLastName;

    String submitterFirstNameLastName;

    Boolean approveByMe;

}
