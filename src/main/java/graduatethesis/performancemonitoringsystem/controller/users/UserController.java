package graduatethesis.performancemonitoringsystem.controller.users;

import graduatethesis.performancemonitoringsystem.constants.UserRoles;
import graduatethesis.performancemonitoringsystem.model.exceptions.AccessForbiddenException;
import graduatethesis.performancemonitoringsystem.model.exceptions.PasswordsNotTheSameException;
import graduatethesis.performancemonitoringsystem.model.exceptions.UserNotFoundException;
import graduatethesis.performancemonitoringsystem.model.filters.UserFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.*;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartment;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentUser;
import graduatethesis.performancemonitoringsystem.model.users.Token;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.*;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import lombok.NonNull;


import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final LoggedUserService loggedUserService;
    private final TokenService tokenService;
    private final PersonService personService;
    private final OrganizationalDepartmentService organizationalDepartmentService;
    private final UserRoleService userRoleService;


    public UserController(UserService userService, UserRepository userRepository, LoggedUserService loggedUserService, TokenService tokenService, PersonService personService, OrganizationalDepartmentService organizationalDepartmentService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.loggedUserService = loggedUserService;
        this.tokenService = tokenService;
        this.personService = personService;
        this.organizationalDepartmentService = organizationalDepartmentService;
        this.userRoleService = userRoleService;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL', 'READ_USER_DATA', 'HEAD_READ_DATA')")
    @GetMapping("/getAllUsers")
    public List<UserSearchHelper> getAllUsers() {
        return userService.findAll().stream().map(User::getAsUserSearchHelper).collect(Collectors.toList());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL', 'READ_USER_DATA', 'HEAD_READ_DATA')")
    @GetMapping("/getAllHeadAndUsers")
    public List<HeadUsersHelper> getAllHeadUsers() {
        List<User> users = this.userRepository.findAll();
        List<HeadUsersHelper> headUsersHelpers = new ArrayList<>();

        for (User user : users) {
            if(user.getUserRoles()!=null && user.getUserRoles().size() != 0 && user.getUserRoles().get(0)!=null && user.getUserRoles().get(0).getRole() != null && user.getUserRoles().get(0).getRole().getName() != null){
                if (user.getUserRoles().get(0).getRole().getName().equals("ROLE_HEAD_OF_DEPARTMENT")){
                    HeadUsersHelper headUsersHelper = new HeadUsersHelper();
                    headUsersHelper.setUserId(user.getId());
                    headUsersHelper.setEmail(user.getEmail());
                    headUsersHelper.setName(user.getPerson().getFirstName());
                    headUsersHelper.setSurname(user.getPerson().getLastName());
                    List<Long> orgIds = new ArrayList<>();
                    List<OrganizationalDepartment> organizationalDepartments = this.organizationalDepartmentService.findAllForUser(user.getId());
                    for (OrganizationalDepartment organizationalDepartment : organizationalDepartments) {
                        orgIds.add(organizationalDepartment.getId());
                    }
                    headUsersHelper.setOrganizationalUnitsIds(orgIds);
                    headUsersHelpers.add(headUsersHelper);
                }
            }
        }

        return headUsersHelpers;

    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL', 'READ_USER_DATA', 'HEAD_READ_DATA')")
    @GetMapping("/getAllHeadInOrganization/{orgId}")
    public List<UserOrgDepartmentHelper> getAllHeadInOrganization(@PathVariable Long orgId) {
        List<OrganizationalDepartmentUser> organizationalDepartmentUsers = this.organizationalDepartmentService.findById(orgId).getOrganizationalDepartmentUsers();
        List<UserOrgDepartmentHelper> userOrgDepartmentHelpers = new ArrayList<>();
        for (var orgDepartmentUser: organizationalDepartmentUsers) {
            if(orgDepartmentUser.getIsHead() != null && orgDepartmentUser.getIsHead()){
                UserOrgDepartmentHelper userOrgDepartmentHelper = new UserOrgDepartmentHelper();
                userOrgDepartmentHelper.setId(orgDepartmentUser.getId());
                userOrgDepartmentHelper.setIsHead(true);
                userOrgDepartmentHelper.setOrganizationalDepartmentId(orgDepartmentUser.getOrganizationalDepartment().getId());
                userOrgDepartmentHelper.setUserId(orgDepartmentUser.getUrUserId().getId());

                userOrgDepartmentHelpers.add(userOrgDepartmentHelper);
            }
        }
        return userOrgDepartmentHelpers;
    }


    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL', 'READ_USER_DATA', 'HEAD_READ_DATA')")
    @GetMapping("/getUserDetails")
    public LoggedUserHelper getUserDetails() {
        User user = loggedUserService.getLoggedUser();
        LoggedUserHelper loggedUserHelper = new LoggedUserHelper();
        loggedUserHelper.setId(user.getAsUserHelper().getId());
        loggedUserHelper.setEmail(user.getAsUserHelper().getEmail());
        loggedUserHelper.setFirstName(user.getAsUserHelper().getFirstName());
        loggedUserHelper.setLastName(user.getAsUserHelper().getLastName());
        loggedUserHelper.setPersonId(user.getAsUserHelper().getPersonId());
        loggedUserHelper.setRoles(user.getAsUserHelper().getRoles());
        loggedUserHelper.setHeadUserName(user.getAsUserHelper().getHeadUserName());
        List<OrganizationalDepartment> list = this.organizationalDepartmentService.findAllForUser(user.getAsUserHelper().getId());
        List<String> orgUnitList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            String orgName = list.get(i).getName();
            orgUnitList.add(orgName);
        }
        loggedUserHelper.setOrganizationalDepartments(orgUnitList);
        return loggedUserHelper;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL', 'READ_USER_DATA')")
    @PutMapping(value = "/changePassword")
    public User changePassword(@RequestHeader String email, @RequestHeader String password, @RequestHeader String newPassword) {
        User user = loggedUserService.getLoggedUser();
        if (user.getEmail().equals(email)) {
            if (!userService.passwordMatches(user, password)) {
                throw new PasswordsNotTheSameException();
            }
            return this.userService.changeUserPassword(user, newPassword);
        }
        throw new AccessForbiddenException();

    }


    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping("/get/{page}/{size}")
    public Page<UserHelperFront> findAllWithPagination(@PathVariable int page, @PathVariable int size, @RequestBody UserFilter userFilter) {

        List<UserHelperFront> loggedUserHelperList = this.userService.findAllWithPagination(userFilter);

        loggedUserHelperList = loggedUserHelperList.stream().sorted((first, second) -> -first.getId().compareTo(second.getId())).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), loggedUserHelperList.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), loggedUserHelperList.size());
        return new PageImpl<>(loggedUserHelperList.subList(startIdx, endIdx), pageable, loggedUserHelperList.size());

    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping("/enableUser/{id}")
    public void enableUser(@PathVariable Long id){
        this.userService.enableUser(id);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping("/setLoggedUserAsHead/{id}")

    public void setLoggedUserAsHead(@PathVariable Long id){
        this.userService.setLoggedUserAsHead(id);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping("/setLoggedUserAsEmployee/{id}")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void setLoggedUserAsEmployee(@PathVariable Long id){
        this.userService.setLoggedUserAsEmployee(id);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping("/getHeadUsers/{page}/{size}")
    public Page<User> findHeadUsers(@PathVariable int page,@PathVariable int size){

        List<User> userHelperFronts = this.userService.getAllHeadUsers(UserRoles.ROLE_HEAD_OF_DEPARTMENT);
        userHelperFronts = userHelperFronts.stream().sorted((first, second) -> -first.getId().compareTo(second.getId())).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), userHelperFronts.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), userHelperFronts.size());
        return new PageImpl<>(userHelperFronts.subList(startIdx, endIdx), pageable, userHelperFronts.size());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping("/getHeadUsers")
    public List<User> getHeadUsers(){
        List<User> userHelperFronts = this.userService.getAllHeadUsers(UserRoles.ROLE_HEAD_OF_DEPARTMENT);
        userHelperFronts = userHelperFronts.stream().sorted((first, second) -> -first.getId().compareTo(second.getId())).collect(Collectors.toList());

        return userHelperFronts;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping("/disableUser/{id}")
    public void disableUser(@PathVariable Long id){
        this.userService.disableUser(id);
    }

    @PostMapping("/signup")
    @Transactional
    public void signUp(@RequestHeader @NonNull String email, @RequestHeader String password, @RequestBody UserHelper user) {

        email = email.toLowerCase();

        try {
            User userFromDB = userService.findByEmail(email);
            if (userFromDB != null && !userFromDB.isEnabled()) {
                Token token = userFromDB.getTokens().get(userFromDB.getTokens().size() - 1);

                    this.tokenService.deleteToken(token.getId());
                    this.userRoleService.deleteRolesOfUser(userFromDB.getId());
                    this.userService.deleteUser(userFromDB.getId());
                    this.personService.deletePerson(userFromDB.getPerson().getId());

            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }


        user.setEmail(email);
        user.setPassword(password);
        user.setEnabled(false);
        var userDB = userService.signUp(user);

        Token token = new Token(userDB);

        tokenService.saveToken(token);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping("/registerUser")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    User registerUser(@RequestBody UserHelper userHelper){
        return this.userService.registerUser(userHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/setHeadUserForUser")
    public void setHeadUserForUser(@RequestBody HeadUserHelper headUserHelper){
        this.userService.setHeadUserForUser(headUserHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/removeHeadUserFromUser")
    public void removeHeadUserFromUser(@RequestBody HeadUserHelper headUserHelper){
        this.userService.removeHeadUserFromUser(headUserHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping("/get/{id}")
    public UserHeadHelperFront findById(@PathVariable Long id){
        return this.userService.findUserById(id);
    }


}


