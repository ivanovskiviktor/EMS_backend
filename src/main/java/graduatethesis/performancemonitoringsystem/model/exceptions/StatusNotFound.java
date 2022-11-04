package graduatethesis.performancemonitoringsystem.model.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StatusNotFound extends RuntimeException {
    public StatusNotFound(Long id){
        super(String.format("Status with id: %s was not found",id));
    }
}
