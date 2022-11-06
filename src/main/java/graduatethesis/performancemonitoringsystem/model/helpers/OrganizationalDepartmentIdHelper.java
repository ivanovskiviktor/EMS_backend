package graduatethesis.performancemonitoringsystem.model.helpers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationalDepartmentIdHelper {

    private Long userId;

    List<Long> organizationalDepartmentIds;

}
