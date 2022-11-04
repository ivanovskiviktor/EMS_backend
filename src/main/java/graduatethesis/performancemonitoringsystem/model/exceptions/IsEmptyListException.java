package graduatethesis.performancemonitoringsystem.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IsEmptyListException extends RuntimeException {
    public IsEmptyListException(){
        super("The list is empty");
    }

}
