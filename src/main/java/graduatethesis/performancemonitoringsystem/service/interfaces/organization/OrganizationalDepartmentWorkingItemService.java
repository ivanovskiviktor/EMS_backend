package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentWorkingItemHelper;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentWorkingItem;

import java.util.List;

public interface OrganizationalDepartmentWorkingItemService {

    OrganizationalDepartmentWorkingItem findById(Long id);

    OrganizationalDepartmentWorkingItem save(OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem);

    List<OrganizationalDepartmentWorkingItem> create(OrganizationalDepartmentWorkingItemHelper organizationalDepartmentWorkingItemHelper);

    OrganizationalDepartmentWorkingItem findByOrganizationalDepartmentAndWorkingItem(Long oid, Long wid);

}
