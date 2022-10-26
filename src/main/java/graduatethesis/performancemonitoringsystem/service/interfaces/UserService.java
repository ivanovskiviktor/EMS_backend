package graduatethesis.performancemonitoringsystem.service.interfaces;

import graduatethesis.performancemonitoringsystem.model.filters.UserFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.*;
import graduatethesis.performancemonitoringsystem.model.users.Token;
import graduatethesis.performancemonitoringsystem.model.users.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByEmail(String email);

    List<User> findAll();

    User findById(Long id);

    void deleteUser(Long id);

    List<UserHelperFront> findAllWithPagination(UserFilter userFilter);

    boolean passwordMatches(User user, String password);

    User login(String username, String password);

    Optional<User> findOne(Long id);

    User save(User user);

    User changeUserPassword(User user,String password);

    void enableUser(Long id);

    User signUp(UserHelper userHelper);

    User getAsUser(UserHelper userHelper);

    User signUp(User user);

    User updateUser(User user);

    User updateUser(UserHelper newUserData);

    UserHelper getAsUserHelper(User user);

    User findByTokensContaining(Token token);

    void updatePassword(User customer, String newPassword);

    void disableUser(Long id);

//    List<User> getAllHeadUsers(String role);

    User registerUser(UserHelper userHelper);

//    List<HeadUsersHelper> getAllHeads();

//    List<User> findAllInOrganizationalUnit(Long id);
//
//    List<UserOrgDepartmentHelper> getAllHeadForOrganization(Long orgId);

    void setLoggedUserAsHead(Long id);

    void setLoggedUserAsEmployee(Long id);

    void setHeadUserForUser(HeadUserHelper headUserHelper);

    UserHeadHelperFront findUserById(Long id);

    void removeHeadUserFromUser(HeadUserHelper headUserHelper);

}
