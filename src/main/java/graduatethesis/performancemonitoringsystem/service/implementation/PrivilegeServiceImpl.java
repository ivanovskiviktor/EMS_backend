package graduatethesis.performancemonitoringsystem.service.implementation;

import graduatethesis.performancemonitoringsystem.model.users.Privilege;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.PrivilegeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
    private final LoggedUserService loggedUserService;

    public PrivilegeServiceImpl(LoggedUserService loggedUserService) {
        this.loggedUserService = loggedUserService;
    }

    @Override
    public boolean loggedUserHasAnyPrivilege(String... targetPrivileges) {
        User loggedUser = loggedUserService.getLoggedUser();

        Set<String> userPrivileges = loggedUser.getPrivileges().stream().map(Privilege::getName).collect(Collectors.toSet());

        for (String targetPrivilege: targetPrivileges) {
            if (userPrivileges.contains(targetPrivilege)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean loggedUserHasAllPrivileges(String... targetPrivileges) {
        User loggedUser = loggedUserService.getLoggedUser();

        Set<String> userPrivileges = loggedUser.getPrivileges().stream().map(Privilege::getName).collect(Collectors.toSet());
        List<String> targetPrivilegesList = List.of(targetPrivileges);

        return userPrivileges.containsAll(targetPrivilegesList);
    }   
}

