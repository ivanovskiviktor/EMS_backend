package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.HeadUserForOrganizationalDepartmentHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentUserCheckHelper;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartment;

import java.util.List;
import java.util.Optional;

public interface OrganizationalDepartmentService {

    List<OrganizationalDepartment> findAll();

    OrganizationalDepartment findByCode(String code);

    Optional<OrganizationalDepartment> save(OrganizationalDepartmentHelper organizationalDepartmentHelper);

    Optional<OrganizationalDepartment> edit(OrganizationalDepartmentHelper organizationalDepartmentHelper);

    void deleteById(Long id);

    OrganizationalDepartment findById(Long id);

    List<OrganizationalDepartment> findAllForUser(Long id);

    void createOrganizationalDepartmentUsers(OrganizationalDepartmentUserCheckHelper organizationalDepartmentUserCheckHelper);

    void setHeadUsersForOrganization(HeadUserForOrganizationalDepartmentHelper headUserForOrganizationalDepartmentHelper);

    List<OrganizationalDepartment> findAllByWorkingItem(Long workingItemId);

}
