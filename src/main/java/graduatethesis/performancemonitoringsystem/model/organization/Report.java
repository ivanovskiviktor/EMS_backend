package graduatethesis.performancemonitoringsystem.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graduatethesis.performancemonitoringsystem.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="o_report")
public class Report implements Comparable<Report> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Integer hours;

    private Integer minutes;

    @Column(name = "submission_date")
    private OffsetDateTime submissionDate;

    private Boolean editable;

    private Boolean accepted;

    @Column(name = "has_previous_report")
    private Boolean hasPreviousReport;

    @ManyToOne
    @JoinColumn(name = "o_employee_tracking_form_id")
    private EmployeeTrackingForm employeeTrackingForm;

    @ManyToOne
    @JoinColumn(name = "o_approver_user_id")
    private User approver;

    @ManyToOne
    @JoinColumn(name = "o_submitter_user_id")
    private User submitter;

    @Override
    public int compareTo(Report o) {
        if(o.getId().equals(this.id)){
            return 1;
        }
        return 0;
    }
}

