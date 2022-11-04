package graduatethesis.performancemonitoringsystem.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DifferentPrivilegeException extends RuntimeException {
    public DifferentPrivilegeException(){
        super(String.format("This user is not logged in"));
    }

}
