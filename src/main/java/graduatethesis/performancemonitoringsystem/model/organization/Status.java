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
@Table(name="o_employee_tracking_form_status")
public class Status implements Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<EmployeeTrackingForm> employeeTrackingForms;

    @Override
    public int compareTo(Object o) {
        if(o instanceof Status){
            if (((Status) o).getId().equals(this.id)) {
                return 1;
            }
        }
        return 0;
    }
}

