package graduatethesis.performancemonitoringsystem.model.helpers;

import graduatethesis.performancemonitoringsystem.model.organization.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportFrontHelper {

    private Report report;

    private String submitter;
}
