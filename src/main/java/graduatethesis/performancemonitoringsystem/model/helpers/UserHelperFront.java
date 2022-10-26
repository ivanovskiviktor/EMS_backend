package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHelperFront {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    boolean isEnabled;

    private String headFirstName;

    private String headLastName;

    private Long headId;

    private Boolean isHead;
}
