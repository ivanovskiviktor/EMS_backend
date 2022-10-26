package graduatethesis.performancemonitoringsystem.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id){
        super(String.format("User with id: %d was not found",id));
    }
    public UserNotFoundException(){super("The user with the given email does not exist!");}
}
