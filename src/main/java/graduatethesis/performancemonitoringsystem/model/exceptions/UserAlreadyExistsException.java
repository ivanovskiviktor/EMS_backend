package graduatethesis.performancemonitoringsystem.model.exceptions;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed"})
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){
        super("User already exists");
    }
}
