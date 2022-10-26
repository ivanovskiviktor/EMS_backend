package graduatethesis.performancemonitoringsystem.service.implementation;

import graduatethesis.performancemonitoringsystem.model.users.UserRole;
import graduatethesis.performancemonitoringsystem.repository.users.UserRoleRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserRoleService;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole create(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }


    @Override
    public void deleteRolesOfUser(Long userId) {
        userRoleRepository.deleteRolesOfUser(userId);
    }

    @Override
    public void deleteUserRoleByUserId(Long userId) {
        userRoleRepository.deleteUserRoleByUserId(userId);
    }


}

