package graduatethesis.performancemonitoringsystem.service.interfaces;

import graduatethesis.performancemonitoringsystem.model.users.Role;


public interface RoleService {

    Role findByName(String roleName);

    Role findById(Long Id);

}
