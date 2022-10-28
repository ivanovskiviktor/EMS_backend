package graduatethesis.performancemonitoringsystem.service.implementation;

import graduatethesis.performancemonitoringsystem.constants.UserRoles;
import graduatethesis.performancemonitoringsystem.model.exceptions.AccessForbiddenException;
import graduatethesis.performancemonitoringsystem.model.exceptions.UserNotFoundException;
import graduatethesis.performancemonitoringsystem.model.filters.UserFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.*;
import graduatethesis.performancemonitoringsystem.model.users.*;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;
    private final LoggedUserService loggedUserService;
    private final RoleService roleService;
    private final PersonService personService;
    private final UserRoleService userRoleService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PasswordEncoder passwordEncoder, LoggedUserService loggedUserService, RoleService roleService, @Lazy PersonService personService, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.passwordEncoder = passwordEncoder;
        this.loggedUserService = loggedUserService;
        this.roleService = roleService;
        this.personService = personService;
        this.userRoleService = userRoleService;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailCustom(email);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAllEnabled();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }


    @Override
    public List<UserHelperFront> findAllWithPagination(UserFilter userFilter) {

        List<User> users = this.userRepository.findAllFiltered(userFilter);
        List<UserHelperFront> userHelperFrontList = new ArrayList<UserHelperFront>();

        for(int i=0; i<users.size();i++ )
        {
            UserHelperFront userHelperFront = new UserHelperFront();
            userHelperFront.setId(users.get(i).getId());
            userHelperFront.setEmail(users.get(i).getEmail());
            userHelperFront.setFirstName(users.get(i).getAsUserHelper().getFirstName());
            userHelperFront.setLastName(users.get(i).getAsUserHelper().getLastName());
            userHelperFront.setEnabled(users.get(i).isEnabled());
            if(users.get(i).getUserRoles()!=null && users.get(i).getUserRoles().size()>0){
                if(users.get(i).getUserRoles().get(0).getRole().getName().equals(UserRoles.ROLE_HEAD_OF_DEPARTMENT)){
                    userHelperFront.setIsHead(true);
                }else{
                    userHelperFront.setIsHead(false);
                }
            }

            if(users.get(i).getHead()!=null) {
                userHelperFront.setHeadFirstName(users.get(i).getHead().getPerson().getFirstName());
                userHelperFront.setHeadLastName(users.get(i).getHead().getPerson().getLastName());
                userHelperFront.setHeadId(users.get(i).getHead().getId());
            }

            userHelperFrontList.add(userHelperFront);
        }
        return userHelperFrontList;
    }

    @Override
    public boolean passwordMatches(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public User login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new AccessForbiddenException();
        }

        User user = userRepository.findByEmailCustom(email);

        assert user != null;
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AccessForbiddenException();
        } else {
            return user;
        }

    }

    @Override
    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return this.userRepository.save(user);
    }

    @Override
    public void enableUser(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

        user.setEnabled(true);
        this.userRepository.save(user);
    }

    @Override
    public void disableUser(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

        user.setEnabled(false);
        this.userRepository.save(user);
    }

    @Override
    public User registerUser(UserHelper userHelper) {
        return null;
    }

//    @Override
//    public List<User> findAllInOrganizationalUnit(Long id) {
//        return null;
//    }
//
//    @Override
//    public List<UserOrgDepartmentHelper> getAllHeadForOrganization(Long orgId) {
//        return null;
//    }


    @Override
    public void setLoggedUserAsHead(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        userRoleService.deleteRolesOfUser(user.getId());

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleService.findByName(UserRoles.ROLE_HEAD_OF_DEPARTMENT_SHORT));
        userRoleService.create(userRole);
    }

    @Override
    public void setLoggedUserAsEmployee(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        userRoleService.deleteRolesOfUser(user.getId());

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleService.findByName(UserRoles.ROLE_EMPLOYEE));
        userRoleService.create(userRole);
    }

    @Override
    public void setHeadUserForUser(HeadUserHelper headUserHelper) {
        User user = this.userRepository.findById(headUserHelper.getId()).orElseThrow();
        User userHead = this.userRepository.findById(headUserHelper.getHeadId()).orElseThrow();
        user.setHead(userHead);
        this.userRepository.save(user);
    }

    @Override
    public UserHeadHelperFront findUserById(Long id) {
        User user =this.userRepository.findUserById(id);
        UserHeadHelperFront userHeadHelperFront = new UserHeadHelperFront();
        userHeadHelperFront.setId(id);

        if (user.getHead() != null) {
            userHeadHelperFront.setHeadId(user.getHead().getId());
            userHeadHelperFront.setHeadFirstName(user.getHead().getPerson().getFirstName());
            userHeadHelperFront.setHeadLastName(user.getHead().getPerson().getLastName());
        }
        return userHeadHelperFront;
    }

    @Override
    public void removeHeadUserFromUser(HeadUserHelper headUserHelper) {
        User user = this.userRepository.findById(headUserHelper.getId()).orElseThrow();
        user.setHead(null);
        this.userRepository.save(user);
    }


    @Override
    public User signUp(UserHelper userHelper) {
        return signUp(getAsUser(userHelper));
    }

    @Override
    public User getAsUser(UserHelper userHelper) {
        User user = new User();
        user.setId(userHelper.getId());
        user.setEmail(userHelper.getEmail());
        user.setPassword(userHelper.getPassword());
        user.setPerson(userHelper.getPerson());
        user.setTokens(userHelper.getTokens());
        user.setEnabled(userHelper.isEnabled());
        user.setDate_created(userHelper.getDate_created());
        user.setDate_modified(userHelper.getDate_modified());

        if (userHelper.getRoles() != null) {
            user.setUserRoles(userHelper.getRoles().stream().map(roleService::findById).map(role -> {
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);
                return userRole;
            }).collect(Collectors.toList()));
        }
        personService.createPerson(userHelper.getPerson());
        return user;
    }

    @Override
    public User signUp(User user) {
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

//        Person person = personService.create(user.getPerson().getFirstName(),user.getPerson().getLastName());
        userRepository.save(user);

        if (user.getUserRoles()  == null || user.getUserRoles() .stream().noneMatch(role -> role.getRole().getName().equals(UserRoles.ROLE_EMPTY_ROLE))) {
            addUserRoleToUser(user, UserRoles.ROLE_EMPLOYEE);
        }


//        person.setUser(user);
        if (!user.isEnabled()) {
            user.setEnabled(false);
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User newUserData) {
        return updateUser(getAsUserHelper(newUserData));
    }

    @Override
    public User updateUser(UserHelper newUserData) {
        return null;
    }

    private User updateRoles(User user, List<Role> newRoles) {
        user.setUserRoles(null);
        userRoleService.deleteRolesOfUser(user.getId());

        for (var role : newRoles) {
            addUserRoleToUser(user, role);
        }

        return user;
    }

    @Override
    public UserHelper getAsUserHelper(User user) {
        UserHelper userHelper = new UserHelper();
        userHelper.setEmail(user.getEmail());
        userHelper.setId(user.getId());
        userHelper.setDate_created(user.getDate_created());
        userHelper.setDate_modified(user.getDate_modified());
        userHelper.setPerson(user.getPerson());
        userHelper.setRoles(user.getUserRoles().stream().map(x -> x.getRole().getId()).collect(Collectors.toList()));
        userHelper.setEnabled(user.isEnabled());
        userHelper.setPassword(user.getPassword());
        userHelper.setTokens(user.getTokens());
        return userHelper;
    }

    @Override
    public User findByTokensContaining(Token token) {
        return userRepository.findByTokensContaining(token);
    }

    private UserRole addUserRoleToUser(User user, String roleName) {
        return addUserRoleToUser(user, roleService.findByName(roleName));
    }

    private UserRole addUserRoleToUser(User user, Role role) {
        var userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRole = userRoleService.create(userRole);

        if (user.getUserRoles() == null) {
            user.setUserRoles(new ArrayList<>());
        }

        user.getUserRoles().add(userRole);
        return userRole;
    }

    @Override
    public void updatePassword(User customer, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
        userRepository.save(customer);
    }

}
