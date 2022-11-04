package graduatethesis.performancemonitoringsystem.model.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed"})
public class OrganizationalDepartmentWorkingItemNotFoundException extends RuntimeException {
    public OrganizationalDepartmentWorkingItemNotFoundException(){
        super("Organizational department working item not found!");
    }
}
