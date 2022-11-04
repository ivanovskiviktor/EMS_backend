package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTrackingFormHelper {

    private Long id;

    private OffsetDateTime taskStartDate;

    private OffsetDateTime taskEndDate;

    private String description;

    private int value;

    private Long workingItemId;

    private Long employeeTrackingFormStatusId;

    private Long organizationalDepartmentId;

    private String title;

    private List<Long> userIds;

}
