package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.filters.EmployeeTrackingFormFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.*;
import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeTrackingFormService {

    EmployeeTrackingForm create(EmployeeTrackingForm employeeTrackingForm);

    List<EmployeeTrackingForm> findAll();

    Optional<EmployeeTrackingForm> save(EmployeeTrackingFormHelper employeeTrackingFormHelper);

    void deleteById(Long id);

    Optional<EmployeeTrackingForm> update(EmployeeTrackingFormHelper employeeTrackingFormHelper);

    List<EmployeeTrackingForm> findAllCustom(EmployeeTrackingFormFilter employeeTrackingFormFilter);

    Page<EmployeeTrackingForm> findAllCustomPaginated(String status, EmployeeTrackingFormFilter employeeTrackingFormFilter, Pageable pageable);

    List<EmployeeTrackingForm> findAllByUser(EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id);

    Page<EmployeeTrackingForm> findAllByUserPaginated(String status, EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id, Pageable pageable);

    List<EmployeeTrackingForm> findAllFormsForUser(EmployeeTrackingFormFilter employeeTrackingFormFilter,  Long userId);

    List<EmployeeTrackingForm> findAllFormsForUserPaginated(String status, EmployeeTrackingFormFilter employeeTrackingFormFilter,  Long userId, Pageable pageable);

    byte[] export(HttpServletResponse response, EmployeeTrackingFormFilter employeeTrackingFormFilter, Boolean forDone) throws IOException;

    Optional<EmployeeTrackingForm> findById(Long id);

    List<ReportTotalTasksHelper> findAllOrderedByOrganizationalDepartment();

    List<ReportTotalTasksByEmployeeHelper> findAllOrderedByEmployee();

    List<ReportTasksByTimeHelper> findAllByTime(DateHelper dateHelper);

    void closeTask(Long id);

    List<EmployeeTrackingForm> findAllByOrganizationalDepartmentId(Long id);

    List<EmployeeTrackingForm> findAllCreatedByUser(Long id);

    OffsetDateTime setOffsetDateTimeToZero(OffsetDateTime startDate);

    List<EmployeeTrackingForm> findAllWhichAreNotInHeadOrganizationalDepartment(Long userId, List<Long> organizationalDepartmentIds);

    void updateClosedTask(Long id);

}
