package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrgDepartmentHelper {
    private Long id;

    private Long userId;

    private Long organizationalDepartmentId;

    private Boolean isHead;
}
