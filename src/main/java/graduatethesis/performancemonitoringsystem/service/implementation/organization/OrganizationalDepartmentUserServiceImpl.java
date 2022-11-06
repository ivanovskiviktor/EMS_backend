package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentUserHelper;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentUser;
import graduatethesis.performancemonitoringsystem.repository.organization.OrganizationalDepartmentUserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationalDepartmentUserServiceImpl implements OrganizationalDepartmentUserService {

    private final OrganizationalDepartmentUserRepository organizationalDepartmentUserRepository;
    private final UserService userService;

    public OrganizationalDepartmentUserServiceImpl(OrganizationalDepartmentUserRepository organizationalDepartmentUserRepository, UserService userService) {
        this.organizationalDepartmentUserRepository = organizationalDepartmentUserRepository;
        this.userService = userService;
    }


    @Override
    public List<OrganizationalDepartmentUser> findAll() {
        return this.organizationalDepartmentUserRepository.findAll();
    }

    @Override
    public OrganizationalDepartmentUser save(OrganizationalDepartmentUserHelper organizationalDepartmentUserHelper) {
        OrganizationalDepartmentUser organizationalDepartmentUser = new OrganizationalDepartmentUser();
        organizationalDepartmentUser.setOrganizationalDepartment(organizationalDepartmentUserHelper.getOrganizationalDepartment());
        organizationalDepartmentUser.setUrUserId(organizationalDepartmentUserHelper.getUrUserId());
        organizationalDepartmentUser.setIsHead(true);
        return this.organizationalDepartmentUserRepository.save(organizationalDepartmentUser);

    }

    @Override
    public void deleteByOrganizationalDepartmentAndUser(Long oid, Long uid) {
        this.organizationalDepartmentUserRepository.deleteByOrganizationalDepartmentAndUser(oid, uid);
    }

    @Override
    public List<OrganizationalDepartmentUser> findAllForOneUser(Long id) {
        return this.organizationalDepartmentUserRepository.findAllForOneUser(id);
    }

    @Override
    public List<OrganizationalDepartmentUser> findAllWhereUserIsHead(Long userId) {
        return this.organizationalDepartmentUserRepository.findAllWhereUserIsHead(userId);
    }

    @Override
    public List<OrganizationalDepartmentUser> findAllByOrganizationalDepartmentId(Long orgDepartment) {
        return this.organizationalDepartmentUserRepository.findAllByOrganizationalDepartmentId(orgDepartment);
    }
}
