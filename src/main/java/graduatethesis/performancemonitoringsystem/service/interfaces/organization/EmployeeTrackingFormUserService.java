package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingFormUser;

public interface EmployeeTrackingFormUserService {

    void save(EmployeeTrackingFormUser employeeTrackingFormUser);

    void deleteByEmployeeTrackingFormAndUser(Long employeeTrackingFormId, Long userId);

    EmployeeTrackingFormUser findByEmployeeTrackingForm(Long employeeTrackingFormId);

}
