package graduatethesis.performancemonitoringsystem.service.interfaces;


import graduatethesis.performancemonitoringsystem.model.users.UserRole;

public interface UserRoleService {

    UserRole create(UserRole userRole);

    void deleteRolesOfUser(Long userId);

    void deleteUserRoleByUserId(Long userId);

}
