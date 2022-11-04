package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingFormUser;
import graduatethesis.performancemonitoringsystem.repository.organization.EmployeeTrackingFormUserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.EmployeeTrackingFormUserService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeTrackingFormUserServiceImpl implements EmployeeTrackingFormUserService {

    private final EmployeeTrackingFormUserRepository employeeTrackingFormUserRepository;

    public EmployeeTrackingFormUserServiceImpl(EmployeeTrackingFormUserRepository employeeTrackingFormUserRepository) {
        this.employeeTrackingFormUserRepository = employeeTrackingFormUserRepository;
    }

    @Override
    public void save(EmployeeTrackingFormUser employeeTrackingFormUser) {
        employeeTrackingFormUserRepository.save(employeeTrackingFormUser);
    }

    @Override
    public void deleteByEmployeeTrackingFormAndUser(Long employeeTrackingFormId, Long userId) {
        employeeTrackingFormUserRepository.deleteByEmployeeTrackingFormAndUser(employeeTrackingFormId, userId);
    }

    @Override
    public EmployeeTrackingFormUser findByEmployeeTrackingForm(Long employeeTrackingFormId) {
        return employeeTrackingFormUserRepository.findByEmployeeTrackingForm(employeeTrackingFormId);
    }
}
