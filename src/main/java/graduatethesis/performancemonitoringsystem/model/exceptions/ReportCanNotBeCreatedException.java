package graduatethesis.performancemonitoringsystem.model.exceptions;

public class ReportCanNotBeCreatedException extends RuntimeException {
    public ReportCanNotBeCreatedException(){
        super("Report can't be created, because the task is done");
    }
}
