package graduatethesis.performancemonitoringsystem.service.interfaces;

public interface PrivilegeService {

    public boolean loggedUserHasAnyPrivilege(String... targetPrivileges);

    public boolean loggedUserHasAllPrivileges(String... targetPrivileges);

}

