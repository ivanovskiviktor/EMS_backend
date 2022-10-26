package graduatethesis.performancemonitoringsystem.model.helpers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonHelper {

    private Long id;

    private String firstName;

    private String lastName;

    private Long userId;
}
