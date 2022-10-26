package graduatethesis.performancemonitoringsystem.model.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter {

    private String firstName;

    private String lastName;

    private String email;

    public String getFirstName() {
        if(firstName!=null) {
            return firstName.toLowerCase();
        }
        return null;
    }

    public String getLastName() {
        if(lastName!=null) {
            return lastName.toLowerCase();
        }
        return null;
    }

    public String getEmail() {
        if(email!=null) {
            return email.toLowerCase();
        }
        return null;
    }
}
