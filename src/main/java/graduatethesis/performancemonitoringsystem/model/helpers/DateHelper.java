package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateHelper {

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;
}
