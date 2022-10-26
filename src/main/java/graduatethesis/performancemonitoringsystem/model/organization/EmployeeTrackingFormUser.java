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
@Table(name="o_employee_tracking_form_user")
public class EmployeeTrackingFormUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "u_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "o_employee_tracking_form_id")
    private EmployeeTrackingForm employeeTrackingForm;

}
