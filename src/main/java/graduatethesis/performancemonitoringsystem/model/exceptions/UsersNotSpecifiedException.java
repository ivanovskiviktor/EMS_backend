package graduatethesis.performancemonitoringsystem.model.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed"})
public class UsersNotSpecifiedException extends RuntimeException{
    public UsersNotSpecifiedException(){
        super("Users not specified exception");
    }

}
