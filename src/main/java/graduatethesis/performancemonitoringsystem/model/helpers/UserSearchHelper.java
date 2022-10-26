package graduatethesis.performancemonitoringsystem.model.helpers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchHelper {
    Long id;

    String email;

    String nameSurname;
}
