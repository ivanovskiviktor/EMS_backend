package graduatethesis.performancemonitoringsystem.model.organization;

import graduatethesis.performancemonitoringsystem.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="o_organizational_department_user")
public class OrganizationalDepartmentUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "u_user_id")
    private User urUserId;

    @ManyToOne
    @JoinColumn(name = "o_organizational_department_id")
    private OrganizationalDepartment organizationalDepartment;

    @Column(name = "is_head")
    private Boolean isHead;
}
