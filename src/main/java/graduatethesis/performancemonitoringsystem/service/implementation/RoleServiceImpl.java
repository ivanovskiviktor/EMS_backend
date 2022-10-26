package graduatethesis.performancemonitoringsystem.service.implementation;

import graduatethesis.performancemonitoringsystem.constants.UserRoles;
import graduatethesis.performancemonitoringsystem.model.exceptions.RoleNotFoundException;
import graduatethesis.performancemonitoringsystem.model.users.Role;
import graduatethesis.performancemonitoringsystem.repository.users.RoleRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(UserRoles.ROLE_PREFIX + roleName).orElseThrow(RoleNotFoundException::new);
    }

    @Override
    public Role findById(Long Id) {
        return roleRepository.findById(Id).orElseThrow(RoleNotFoundException::new);
    }




}
