package graduatethesis.performancemonitoringsystem.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="u_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "privilege")
    private List<RolePrivilege> rolePrivileges;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles;

}

