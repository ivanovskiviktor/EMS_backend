package graduatethesis.performancemonitoringsystem.model.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportHelper;
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

    @JsonIgnore
    public String getApproverFullName(){
        if(approver == null){
            return "";
        }else{
            return this.approver.getPerson().getFirstName().toLowerCase() + " " + this.approver.getPerson().getLastName().toLowerCase();
        }
    }


    @JsonIgnore
    public ReportHelper getAsReportHelper(User user){
        ReportHelper reportHelper = new ReportHelper();

        reportHelper.setDescription(description);
        if(employeeTrackingForm.getUser()!=null){
            if(employeeTrackingForm.getUser().getPerson()!=null)
            {
                reportHelper.setFirstName(employeeTrackingForm.getUser().getPerson().getFirstName());
            }
        }
        if(employeeTrackingForm.getUser()!=null){
            if(employeeTrackingForm.getUser().getPerson()!=null)
            {
                reportHelper.setLastName(employeeTrackingForm.getUser().getPerson().getLastName());
            }
        }
        reportHelper.setHours(hours);
        reportHelper.setMinutes(minutes);
        reportHelper.setEmployeeTrackingFormId(employeeTrackingForm.getId());
        reportHelper.setId(id);
        if(approver!=null){
            reportHelper.setApprover(approver.getEmail());
            reportHelper.setApproverFirstNameLastName(approver.getPerson().getFirstName() + " " + approver.getPerson().getLastName());
        }
        if(submitter!=null){
            reportHelper.setSubmitter(submitter.getEmail());
            reportHelper.setSubmitterFirstNameLastName(submitter.getPerson().getFirstName() + " " + submitter.getPerson().getLastName());
        }
        reportHelper.setSubmissionDate(submissionDate);
        reportHelper.setTaskDescription(employeeTrackingForm.getDescription());
        reportHelper.setWorkingItemName(employeeTrackingForm.getOrganizationalDepartmentWorkingItem().getWorkingItem().getName());
        if(accepted==null || !accepted){
            reportHelper.setIsAccepted(false);
        }else{
            reportHelper.setIsAccepted(true);
        }
        if(this.hasPreviousReport == null || !this.hasPreviousReport){
            reportHelper.setHasPreviousReport(false);
        }else{
            reportHelper.setHasPreviousReport(true);
        }
        boolean isAssigned = employeeTrackingForm.getTimeTrackingFormUsers().stream().anyMatch(c->c.getUser().getId().equals(user.getId()));
        if(submitter.getHead()!=null){
            if(this.submitter.getHead().getId().equals(user.getId())){
                reportHelper.setApproveByMe(true);
            }else{
                reportHelper.setApproveByMe(false);
            }
        }
        return reportHelper;
    }


    @Override
    public int compareTo(Report o) {
        if(o.getId().equals(this.id)){
            return 1;
        }
        return 0;
    }
}

