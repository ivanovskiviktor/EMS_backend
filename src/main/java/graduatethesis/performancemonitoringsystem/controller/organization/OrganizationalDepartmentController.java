package graduatethesis.performancemonitoringsystem.controller.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.*;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartment;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentUser;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/orgdepartment")
@PreAuthorize("isAuthenticated()")
public class OrganizationalDepartmentController {

    private final OrganizationalDepartmentService organizationalDepartmentService;
    private final OrganizationalDepartmentUserService organizationalDepartmentUserService;
    private final UserService userService;

    public OrganizationalDepartmentController(OrganizationalDepartmentService organizationalDepartmentService, OrganizationalDepartmentUserService organizationalDepartmentUserService, UserService userService) {
        this.organizationalDepartmentService = organizationalDepartmentService;
        this.organizationalDepartmentUserService = organizationalDepartmentUserService;
        this.userService = userService;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/create")
    public ResponseEntity<OrganizationalDepartment> save(@RequestBody OrganizationalDepartmentHelper organizationalDepartmentHelper){
        return this.organizationalDepartmentService.save(organizationalDepartmentHelper).map(organizationalDepartment -> ResponseEntity.ok().body(organizationalDepartment))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping(value = "get/{id}")
    public OrganizationalDepartment getById(@PathVariable Long id){
        return this.organizationalDepartmentService.findById(id);
    }


    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PutMapping(value = "/update")
    public ResponseEntity<OrganizationalDepartment> edit(@RequestBody OrganizationalDepartmentHelper organizationalDepartmentHelper){
        return this.organizationalDepartmentService.edit(organizationalDepartmentHelper).map(organizationalDepartment -> ResponseEntity.ok().body(organizationalDepartment))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @DeleteMapping(value = "/delete/{id}")
    public void deleteById(@PathVariable Long id){
        this.organizationalDepartmentService.deleteById(id);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping(value = "/getAll")
    public List<OrganizationalDepartment> findAll(){
        List<OrganizationalDepartment> organizationalDepartments = this.organizationalDepartmentService.findAll();

        return organizationalDepartments.stream().sorted((first, second) -> -first.getName().compareTo(second.getName())).collect(Collectors.toList());
    }


    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping(value = "/get/{page}/{size}")
    public Page<OrganizationalDepartment> findAllPageable(@PathVariable int page, @PathVariable int size){
        List<OrganizationalDepartment> organizationalDepartments =  this.organizationalDepartmentService.findAll();

        organizationalDepartments = organizationalDepartments.stream().sorted((first, second) -> -first.getId().compareTo(second.getId())).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), organizationalDepartments.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), organizationalDepartments.size());
        return new PageImpl<>(organizationalDepartments.subList(startIdx, endIdx), pageable, organizationalDepartments.size());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping("/getForUser/{id}")
    public List<OrganizationalDepartment> getForUser(@PathVariable Long id){
        List<OrganizationalDepartment> organizationalDepartmentsForUser = this.organizationalDepartmentService.findAllForUser(id);

        return organizationalDepartmentsForUser;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping("/allForUser/{id}")
    public List<OrganizationalDepartment> findAllForUser(@PathVariable Long id){
        List<OrganizationalDepartment> allForUser = this.organizationalDepartmentService.findAllForUser(id);

        if(allForUser==null){
            allForUser = new ArrayList<>();
        }
        List<OrganizationalDepartment> list = this.organizationalDepartmentService.findAll();

        for (OrganizationalDepartment organizationalDepartment:list) {
            if(!allForUser.contains(organizationalDepartment)){
                allForUser.add(organizationalDepartment);
            }
        }
        allForUser = allForUser.stream().sorted((first, second) -> -first.getName().compareTo(second.getName())).collect(Collectors.toList());

        return allForUser;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/addOrganizationalUnit")
    public void addOrganizationalDepartment(@RequestBody OrganizationalDepartmentIdHelper organizationalDepartmentIdHelper) {

        List<OrganizationalDepartmentUser> list = this.organizationalDepartmentUserService.findAll();
        List<OrganizationalDepartmentUser> listpom = new ArrayList<>();
        List<Long> longList = organizationalDepartmentIdHelper.getOrganizationalDepartmentIds();
        List<Long> finalList = new ArrayList<>();


        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUrUserId().getId().equals(organizationalDepartmentIdHelper.getUserId())) {
                listpom.add(list.get(i));
                finalList.add(list.get(i).getOrganizationalDepartment().getId());
            }
        }

        for(int j=0;j<longList.size();j++){
            if(!finalList.contains(longList.get(j))){
                OrganizationalDepartmentUserHelper organizationalDepartmentUserHelper = new OrganizationalDepartmentUserHelper();
                OrganizationalDepartment organizationalDepartment = this.organizationalDepartmentService.findById(organizationalDepartmentIdHelper.getOrganizationalDepartmentIds().get(j));
                User user = this.userService.findById(organizationalDepartmentIdHelper.getUserId());
                organizationalDepartmentUserHelper.setOrganizationalDepartment(organizationalDepartment);
                organizationalDepartmentUserHelper.setUrUserId(user);
                organizationalDepartmentUserService.save(organizationalDepartmentUserHelper);
            }
        }
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/deleteOrgDepartment")
    public void deleteOrganizationalDepartmentByIds(@RequestBody OrganizationalDepartmentIdHelper organizationalDepartmentIdHelper){

        for(int i=0; i<organizationalDepartmentIdHelper.getOrganizationalDepartmentIds().size();i++){
            this.organizationalDepartmentUserService.deleteByOrganizationalDepartmentAndUser(organizationalDepartmentIdHelper.getOrganizationalDepartmentIds().get(i), organizationalDepartmentIdHelper.getUserId());
        }
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/checkOtherUsers")
    public void createOrganizationalUnitUsers(@RequestBody OrganizationalDepartmentUserCheckHelper organizationalDepartmentUserCheckHelper){
        this.organizationalDepartmentService.createOrganizationalDepartmentUsers(organizationalDepartmentUserCheckHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','READ_USER_DATA','HEAD_READ_DATA')")
    @GetMapping("/allByWorkingItem/{workingItemId}")
    public List<OrganizationalDepartmentHelper> findAllByWorkingItem(@PathVariable Long workingItemId){
        List<OrganizationalDepartmentHelper> organizationalDepartmentHelpers = this.organizationalDepartmentService.findAllByWorkingItem(workingItemId).stream().map(OrganizationalDepartment::getAsOrganizationalDepartmentHelper).collect(Collectors.toList());

        return organizationalDepartmentHelpers;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','READ_USER_DATA','HEAD_READ_DATA')")
    @PostMapping(value = "/setHeadUsers")
    public void setHeadUsersForOrganization(@RequestBody HeadUserForOrganizationalDepartmentHelper headUserForOrganizationalDepartmentHelper){
        this.organizationalDepartmentService.setHeadUsersForOrganization(headUserForOrganizationalDepartmentHelper);
    }

}
