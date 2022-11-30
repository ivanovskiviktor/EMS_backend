package graduatethesis.performancemonitoringsystem.model.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTrackingFormFilter {

    Long workingItemId;

    String firstName;

    String lastName;

    OffsetDateTime startDate;

    OffsetDateTime endDate;

    String description;

    Integer valueId;

    Long statusId;

    Long organizationalDepartmentId;

    String title;

    String submitterFirstNameLastName;


    public String getsubmitterFirstNameLastNameToLower(){
        if(this.submitterFirstNameLastName!=null){
            return this.submitterFirstNameLastName.toLowerCase();
        }
        return null;
    }

    public String getFirstName() {
        if(firstName!=null){
            return firstName.toLowerCase();
        }
        return null;
    }

    public String getLastName() {
        if(lastName!=null){
            return lastName.toLowerCase();
        }
        return null;
    }
    public String getTitle() {
        if(title!=null){
            return title.toLowerCase();
        }
        return null;
    }
    public String getDescription() {
        if(description!=null){
            return description.toLowerCase();
        }
        return null;
    }
}
