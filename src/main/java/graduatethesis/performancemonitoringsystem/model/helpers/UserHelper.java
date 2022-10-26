package graduatethesis.performancemonitoringsystem.model.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;

import graduatethesis.performancemonitoringsystem.model.users.Person;
import graduatethesis.performancemonitoringsystem.model.users.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserHelper {


    private Long id;


    private Person person;


    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    private boolean enabled;


    private OffsetDateTime date_created;


    private OffsetDateTime date_modified;


    private List<Long> roles;


    private List<Token> tokens;


    @Override
    public String toString() {
        String userInfo = "User{email=";
        userInfo = userInfo.concat(email);
        userInfo = userInfo.concat(", roles=[");
        for (Long roleId:roles) {
            userInfo.concat(roleId.toString()).concat(", ");
        }
        userInfo = userInfo.concat("], person=");

        if (person != null) {
            userInfo = userInfo.concat(person.getFirstName());
        } else {
            userInfo = userInfo.concat("null");
        }

        userInfo = userInfo.concat("}");

        return userInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHelper user = (UserHelper) o;
        return Objects.equals(id, user.id);
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        if (email != null) {
            email = email.toLowerCase();
        }

        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail() {
        if (email != null) {
            return email.toLowerCase();
        }

        return null;
    }

}
