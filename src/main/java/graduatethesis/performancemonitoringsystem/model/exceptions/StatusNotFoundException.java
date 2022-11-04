package graduatethesis.performancemonitoringsystem.model.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javassist.NotFoundException;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed"})
public class StatusNotFoundException extends NotFoundException {
    public StatusNotFoundException(String msg) {
        super(msg);
    }

}
