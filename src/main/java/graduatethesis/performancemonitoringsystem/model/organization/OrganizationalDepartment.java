package graduatethesis.performancemonitoringsystem.model.organization;

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
@Table(name="o_organizational_department")
public class OrganizationalDepartment implements Comparable<OrganizationalDepartment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String name;

    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "organizationalDepartment")
    private List<OrganizationalDepartmentUser> organizationalDepartmentUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "organizationalDepartment")
    private List<OrganizationalDepartmentWorkingItem> organizationalDepartmentWorkingItems;


    @Override
    public int compareTo(OrganizationalDepartment o) {
        if(o.getId().equals(this.id))
            return 1;
        return 0;
    }
    @Override
    public boolean equals (Object o) {
        OrganizationalDepartment orgDepartment = (OrganizationalDepartment)o;
        if(orgDepartment.getId().equals(this.id))
            return true;
        return false;
    }

}
