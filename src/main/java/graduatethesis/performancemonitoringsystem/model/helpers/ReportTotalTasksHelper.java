package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTotalTasksHelper {

    String workingItem;

    String status;

    String organizationalDepartment;

    Integer countForTasks;
}

