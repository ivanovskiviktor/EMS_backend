package graduatethesis.performancemonitoringsystem.model.exceptions;

public class ReportNotEditableException extends RuntimeException{
    public ReportNotEditableException(){
        super("Report is not editable");
    }
}
