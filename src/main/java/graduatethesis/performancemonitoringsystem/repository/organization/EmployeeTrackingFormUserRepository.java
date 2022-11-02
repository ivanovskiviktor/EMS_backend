package graduatethesis.performancemonitoringsystem.repository.organization;

import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingFormUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface EmployeeTrackingFormUserRepository extends JpaRepository<EmployeeTrackingFormUser, Long> {

    @Transactional
    @Modifying
    @Query("delete from EmployeeTrackingFormUser etfu where etfu.employeeTrackingForm.id = :employeeTrackingFormId and etfu.user.id = :userId")
    void deleteByEmployeeTrackingFormAndUser(Long employeeTrackingFormId, Long userId);

    @Query("select etfu from EmployeeTrackingFormUser etfu where etfu.employeeTrackingForm.id = :employeeTrackingFormId")
    EmployeeTrackingFormUser findByTimeTrackingForm(Long employeeTrackingFormId);
}
