package graduatethesis.performancemonitoringsystem.model.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import graduatethesis.performancemonitoringsystem.model.helpers.LoggedUserHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.UserSearchHelper;
import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingFormUser;
import graduatethesis.performancemonitoringsystem.model.organization.Note;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentUser;
import graduatethesis.performancemonitoringsystem.model.organization.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="u_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name  = "u_person_id")
    private Person person;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="head_u_user_id")
    private User head;

    @Column(unique = true)
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private boolean enabled;

    @Column
    @CreationTimestamp
    private OffsetDateTime date_created;

    @Column
    @UpdateTimestamp
    private OffsetDateTime date_modified;


    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> userRoles;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @JsonIgnore
    @OneToMany(mappedBy = "urUserId")
    private List<OrganizationalDepartmentUser> organizationalDepartmentUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<EmployeeTrackingFormUser> employeeTrackingFormUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Note> notes;

    @JsonIgnore
    @OneToMany(mappedBy = "approver")
    private List<Report> reports;

    @JsonIgnore
    public List<Privilege> getPrivileges() {
        return userRoles.stream().
                map(UserRole::getRole).
                flatMap(r -> r.getRolePrivileges().stream()).
                map(RolePrivilege::getPrivilege).
                distinct().
                collect(Collectors.toList());

    }

    @JsonIgnore
    public LoggedUserHelper getAsUserHelper(){
        LoggedUserHelper userHelper = new LoggedUserHelper();

        userHelper.setId(this.id);
        userHelper.setEmail(this.email);
        if(this.person!=null){
            userHelper.setFirstName(this.person.getFirstName());
            userHelper.setLastName(this.person.getLastName());
            userHelper.setPersonId(this.person.getId());
        }
        userHelper.setRoles(userRoles.stream().map(ur->ur.getRole().getName()).collect(Collectors.toList()));
        return userHelper;
    }

    @JsonIgnore
    public UserSearchHelper getAsUserSearchHelper(){
        UserSearchHelper ush = new UserSearchHelper();

        ush.setId(this.id);
        ush.setEmail(this.email);
        if(this.person!=null){
            ush.setNameSurname(this.person.getFirstName() + " " + this.person.getLastName());
        }

        return ush;
    }


}
