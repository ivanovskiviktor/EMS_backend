package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTotalTasksByEmployeeHelper {

    String workingItem;

    String status;

    String firsName;

    String lastName;

    Integer countForTasks;
}

