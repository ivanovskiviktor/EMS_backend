package graduatethesis.performancemonitoringsystem.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UserRoles {
    public static final String ROLE_PREFIX = "ROLE_";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String ROLE_HEAD_OF_DEPARTMENT = "ROLE_HEAD_OF_DEPARTMENT";
    public static final String ROLE_HEAD_OF_DEPARTMENT_SHORT = "HEAD_OF_DEPARTMENT";

    public static final String ROLE_EMPTY_ROLE="";


    public static String getRoleWithPrefix(String role) {
        return ROLE_PREFIX + role;
    }

}
