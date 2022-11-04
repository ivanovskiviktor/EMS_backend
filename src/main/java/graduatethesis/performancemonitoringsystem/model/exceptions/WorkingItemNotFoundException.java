package graduatethesis.performancemonitoringsystem.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WorkingItemNotFoundException extends RuntimeException {

    public WorkingItemNotFoundException(Long id){
        super(String.format("Working item with id: %s was not found",id));
    }
    public WorkingItemNotFoundException(){super("Working item not found!");}
}

