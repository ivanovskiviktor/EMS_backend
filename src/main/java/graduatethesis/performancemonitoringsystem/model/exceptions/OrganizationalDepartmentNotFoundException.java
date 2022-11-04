package graduatethesis.performancemonitoringsystem.model.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed"})
public class OrganizationalDepartmentNotFoundException extends RuntimeException {
    public OrganizationalDepartmentNotFoundException() {
        super();
    }
}
