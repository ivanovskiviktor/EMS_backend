package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserHelper {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private Long personId;

    private List<String> roles;

    private List<String> organizationalDepartments;

    private String headUserName;
}
