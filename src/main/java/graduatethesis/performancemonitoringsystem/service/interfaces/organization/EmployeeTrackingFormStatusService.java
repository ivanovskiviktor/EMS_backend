package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.EmployeeTrackingFormStatusHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Status;

import java.util.List;
import java.util.Optional;

public interface EmployeeTrackingFormStatusService {

    Status createEmployeeTrackingFormStatus(EmployeeTrackingFormStatusHelper employeeTrackingFormStatusHelper);

    Status updateEmployeeTrackingFormStatus(EmployeeTrackingFormStatusHelper employeeTrackingFormStatusHelper);

    void deleteEmployeeTrackingFormStatus(long id);

    List<Status> findAll();

    Optional<Status> findById(Long id);

    Status findByName(String name);

}
