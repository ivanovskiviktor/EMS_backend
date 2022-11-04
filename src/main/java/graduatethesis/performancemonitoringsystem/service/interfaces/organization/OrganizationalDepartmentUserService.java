package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentUserHelper;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentUser;

import java.util.List;

public interface OrganizationalDepartmentUserService {

    List<OrganizationalDepartmentUser> findAll();

    OrganizationalDepartmentUser save(OrganizationalDepartmentUserHelper organizationalDepartmentUserHelper);

    void deleteByOrganizationalDepartmentAndUser(Long oid, Long uid);

    List<OrganizationalDepartmentUser> findAllForOneUser(Long id);

    List<OrganizationalDepartmentUser> findAllWhereUserIsHead(Long userId);

    List<OrganizationalDepartmentUser> findAllByOrganizationalDepartmentId(Long orgDepartment);

}
