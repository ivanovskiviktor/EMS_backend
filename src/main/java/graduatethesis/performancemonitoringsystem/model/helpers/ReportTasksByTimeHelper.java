package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTasksByTimeHelper {

    String workingItem;

    String status;

    String firstName;

    String lastName;

    OffsetDateTime startDate;

    OffsetDateTime endDate;

    Integer count;
}
