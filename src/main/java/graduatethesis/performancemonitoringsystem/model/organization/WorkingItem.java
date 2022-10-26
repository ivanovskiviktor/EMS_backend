package graduatethesis.performancemonitoringsystem.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity

@AllArgsConstructor
@Table(name="o_working_item")
public class WorkingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "workingItem")
    private List<OrganizationalDepartmentWorkingItem> organizationalDepartmentWorkingItems;

    public WorkingItem() {

    }

    public WorkingItem(String name, String label) {
        this.name = name;
        this.label = label;
    }
}
