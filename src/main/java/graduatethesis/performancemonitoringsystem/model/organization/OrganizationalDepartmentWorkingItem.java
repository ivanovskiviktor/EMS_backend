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
@Table(name="o_organizational_department_working_item")
public class OrganizationalDepartmentWorkingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "o_organizational_department_id")
    private OrganizationalDepartment organizationalDepartment;

    @ManyToOne
    @JoinColumn(name = "o_working_item_id")
    private WorkingItem workingItem;

    @JsonIgnore
    @OneToMany(mappedBy = "organizationalDepartmentWorkingItem")
    private List<EmployeeTrackingForm> employeeTrackingForms;
}
