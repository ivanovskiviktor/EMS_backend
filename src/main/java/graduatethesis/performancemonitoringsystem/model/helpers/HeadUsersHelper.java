package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadUsersHelper {
    String name;

    String surname;

    String email;

    Long userId;

    List<Long> organizationalUnitsIds;
}
