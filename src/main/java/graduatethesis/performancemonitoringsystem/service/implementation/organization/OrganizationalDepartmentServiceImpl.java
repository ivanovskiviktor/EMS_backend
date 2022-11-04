package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.exceptions.OrganizationalDepartmentNotFoundException;
import graduatethesis.performancemonitoringsystem.model.helpers.HeadUserForOrganizationalDepartmentHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentUserCheckHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentUserHelper;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartment;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.organization.OrganizationalDepartmentRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationalDepartmentServiceImpl implements OrganizationalDepartmentService {

    private final OrganizationalDepartmentRepository organizationalDepartmentRepository;
    private final UserService userService;
    private final OrganizationalDepartmentUserService organizationalDepartmentUserService;

    public OrganizationalDepartmentServiceImpl(OrganizationalDepartmentRepository organizationalDepartmentRepository, UserService userService, OrganizationalDepartmentUserService organizationalDepartmentUserService) {
        this.organizationalDepartmentRepository = organizationalDepartmentRepository;
        this.userService = userService;
        this.organizationalDepartmentUserService = organizationalDepartmentUserService;
    }

    @Override
    public List<OrganizationalDepartment> findAll() {
        return this.organizationalDepartmentRepository.findAll();
    }

    @Override
    public OrganizationalDepartment findByCode(String code) {
        return this.organizationalDepartmentRepository.findByCode(code);
    }

    @Override
    public Optional<OrganizationalDepartment> save(OrganizationalDepartmentHelper organizationalDepartmentHelper) {
        OrganizationalDepartment organizationalDepartment = new OrganizationalDepartment();
        organizationalDepartment.setName(organizationalDepartmentHelper.getName());
        organizationalDepartment.setLabel(organizationalDepartmentHelper.getLabel());
        organizationalDepartment.setCode(organizationalDepartmentHelper.getCode());

        return Optional.of(this.organizationalDepartmentRepository.save(organizationalDepartment));

    }

    @Override
    public Optional<OrganizationalDepartment> edit(OrganizationalDepartmentHelper organizationalDepartmentHelper) {
        OrganizationalDepartment organizationalDepartment = this.organizationalDepartmentRepository.findById(organizationalDepartmentHelper.getId()).orElseThrow(OrganizationalDepartmentNotFoundException::new);
        organizationalDepartment.setId(organizationalDepartmentHelper.getId());
        organizationalDepartment.setCode(organizationalDepartmentHelper.getCode());
        organizationalDepartment.setName(organizationalDepartmentHelper.getName());
        organizationalDepartment.setLabel(organizationalDepartmentHelper.getLabel());

        return Optional.of(this.organizationalDepartmentRepository.save(organizationalDepartment));

    }

    @Override
    public void deleteById(Long id) {
        this.organizationalDepartmentRepository.deleteById(id);
    }

    @Override
    public OrganizationalDepartment findById(Long id) {
        return this.organizationalDepartmentRepository.findById(id).orElseThrow(OrganizationalDepartmentNotFoundException::new);
    }

    @Override
    public List<OrganizationalDepartment> findAllForUser(Long id) {
        return this.organizationalDepartmentRepository.findAllForUser(id);
    }

    @Override
    public void createOrganizationalDepartmentUsers(OrganizationalDepartmentUserCheckHelper organizationalDepartmentUserCheckHelper) {
        OrganizationalDepartment organizationalDepartment = this.organizationalDepartmentRepository.findById(organizationalDepartmentUserCheckHelper.getOrgId()).orElseThrow(OrganizationalDepartmentNotFoundException::new);
        List<User> usersToBeDeleted = this.userService.findAllInOrganizationalDepartment(organizationalDepartment.getId());

        for (User user : usersToBeDeleted) {
            this.organizationalDepartmentUserService.deleteByOrganizationalDepartmentAndUser(organizationalDepartment.getId(), user.getId());
        }

        for(int i=0;i<organizationalDepartmentUserCheckHelper.getUsers().size();i++){
            OrganizationalDepartmentUserHelper organizationalDepartmentUserHelper = new OrganizationalDepartmentUserHelper();
            organizationalDepartmentUserHelper.setOrganizationalDepartment(organizationalDepartment);
            User u =  this.userService.findById(organizationalDepartmentUserCheckHelper.getUsers().get(i));
            organizationalDepartmentUserHelper.setUrUserId(u);
            this.organizationalDepartmentUserService.save(organizationalDepartmentUserHelper);
        }


    }

    @Override
    public void setHeadUsersForOrganization(HeadUserForOrganizationalDepartmentHelper headUserForOrganizationalDepartmentHelper) {
        OrganizationalDepartment organizationalDepartment = this.organizationalDepartmentRepository.findById(headUserForOrganizationalDepartmentHelper.getOrgId()).orElseThrow(OrganizationalDepartmentNotFoundException::new);
        List<User> usersInOrganization = this.userService.findAllInOrganizationalDepartment(organizationalDepartment.getId());

        for(User u : usersInOrganization){
            if(u.getUserRoles()!=null && u.getUserRoles().size()>0){
                if(u.getUserRoles().get(0).getRole().getName().equals("ROLE_HEAD_OF_DEPARTMENT")){
                    this.organizationalDepartmentUserService.deleteByOrganizationalDepartmentAndUser(organizationalDepartment.getId(), u.getId());
                }
            }
        }


        for(int i=0;i<headUserForOrganizationalDepartmentHelper.getUsers().size();i++){
            OrganizationalDepartmentUserHelper organizationalDepartmentUserHelper = new OrganizationalDepartmentUserHelper();
            organizationalDepartmentUserHelper.setOrganizationalDepartment(organizationalDepartment);
            User u =  this.userService.findById(headUserForOrganizationalDepartmentHelper.getUsers().get(i));
            organizationalDepartmentUserHelper.setUrUserId(u);
            this.organizationalDepartmentUserService.save(organizationalDepartmentUserHelper);
        }

    }

    @Override
    public List<OrganizationalDepartment> findAllByWorkingItem(Long workingItemId) {
        return this.organizationalDepartmentRepository.findAllByWorkingItem(workingItemId);
    }
}
