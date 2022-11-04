package graduatethesis.performancemonitoringsystem.model.helpers;

import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartment;
import graduatethesis.performancemonitoringsystem.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationalDepartmentUserHelper {

    private Long Id;

    private User urUserId;

    private OrganizationalDepartment organizationalDepartment;

}
