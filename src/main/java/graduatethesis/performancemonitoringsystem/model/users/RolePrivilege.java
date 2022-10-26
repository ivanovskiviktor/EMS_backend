package graduatethesis.performancemonitoringsystem.model.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "u_role_privilege")
public class RolePrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ur_privilege_id")
    private Privilege privilege;

    @ManyToOne
    @JoinColumn(name = "ur_role_id")
    private Role role;

}
