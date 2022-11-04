package graduatethesis.performancemonitoringsystem.model.exceptions;

public class DescriptionIsEmptyOrNotFoundException extends RuntimeException{

    public DescriptionIsEmptyOrNotFoundException()
    {
        super("The description does not exist!");
    }
}
