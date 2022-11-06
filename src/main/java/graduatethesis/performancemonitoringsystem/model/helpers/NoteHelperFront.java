package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteHelperFront {

    private Long id;

    private String description;

    private String firstName;

    private String lastName;

    private OffsetDateTime weekStartDate;

    private OffsetDateTime weekEndDate;

}

