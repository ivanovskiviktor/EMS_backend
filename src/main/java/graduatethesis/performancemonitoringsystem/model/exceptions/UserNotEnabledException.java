package graduatethesis.performancemonitoringsystem.model.exceptions;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed"})
public class UserNotEnabledException extends RuntimeException{
    public UserNotEnabledException(){
        super();
    }

}

