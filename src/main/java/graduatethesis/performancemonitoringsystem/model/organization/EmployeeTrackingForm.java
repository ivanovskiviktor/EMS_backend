package graduatethesis.performancemonitoringsystem.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graduatethesis.performancemonitoringsystem.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.sql.Time;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="o_employee_tracking_form")
public class EmployeeTrackingForm implements Comparable<EmployeeTrackingForm> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "u_user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "date_created")
    private OffsetDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "date_modified")
    private OffsetDateTime dateModified;

    @Column(name = "task_start_date")
    private OffsetDateTime taskStartDate;

    @Column(name = "task_end_date")
    private OffsetDateTime taskEndDate;

    private String description;

    private Integer value;

    private String title;

    @JsonIgnore
    @OneToMany(mappedBy = "employeeTrackingForm")
    private List<Report> reports;

    @JsonIgnore
    @OneToMany(mappedBy = "employeeTrackingForm")
    private List<EmployeeTrackingFormUser> timeTrackingFormUsers;

    @ManyToOne
    @JoinColumn(name = "o_employee_tracking_form_status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "o_organizational_department_working_item_id")
    private OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem;

    @Override
    public int compareTo(EmployeeTrackingForm e) {
        if(e.getId().equals(this.id)){
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object e){
        if(e instanceof EmployeeTrackingForm){
            EmployeeTrackingForm toCompare = (EmployeeTrackingForm) e;
            return this.id.equals(toCompare.id);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
