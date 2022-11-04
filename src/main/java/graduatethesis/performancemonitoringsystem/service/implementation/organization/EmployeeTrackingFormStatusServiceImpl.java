package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.exceptions.StatusNotFoundException;
import graduatethesis.performancemonitoringsystem.model.helpers.EmployeeTrackingFormStatusHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Status;
import graduatethesis.performancemonitoringsystem.repository.organization.EmployeeTrackingFormStatusRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.EmployeeTrackingFormStatusService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeTrackingFormStatusServiceImpl implements EmployeeTrackingFormStatusService {

    private final EmployeeTrackingFormStatusRepository employeeTrackingFormStatusRepository;

    public EmployeeTrackingFormStatusServiceImpl(EmployeeTrackingFormStatusRepository employeeTrackingFormStatusRepository) {
        this.employeeTrackingFormStatusRepository = employeeTrackingFormStatusRepository;
    }

    @Override
    public Status createEmployeeTrackingFormStatus(EmployeeTrackingFormStatusHelper employeeTrackingFormStatusHelper) {
        Status status = new Status(null, employeeTrackingFormStatusHelper.getName(), employeeTrackingFormStatusHelper.getLabel(), null);
        return this.employeeTrackingFormStatusRepository.save(status);
    }

    @Override
    public Status updateEmployeeTrackingFormStatus(EmployeeTrackingFormStatusHelper employeeTrackingFormStatusHelper) {
        Status statusForUpdate = null;
        try {
            statusForUpdate = this.employeeTrackingFormStatusRepository.findById(employeeTrackingFormStatusHelper.getId())
                    .orElseThrow(()-> new StatusNotFoundException("EmployeeTrackingFormStatus not found!"));
            statusForUpdate.setId(employeeTrackingFormStatusHelper.getId());
            statusForUpdate.setName(employeeTrackingFormStatusHelper.getName());
            statusForUpdate.setLabel(employeeTrackingFormStatusHelper.getLabel());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return this.employeeTrackingFormStatusRepository.save(statusForUpdate);
    }

    @Override
    public void deleteEmployeeTrackingFormStatus(long id) {
        employeeTrackingFormStatusRepository.deleteById(id);
    }

    @Override
    public List<Status> findAll() {
        return this.employeeTrackingFormStatusRepository.findAllCustom();
    }

    @Override
    public Optional<Status> findById(Long id) {
        return this.employeeTrackingFormStatusRepository.findById(id);
    }

    @Override
    public Status findByName(String name) {
        return this.employeeTrackingFormStatusRepository.findByName(name);
    }
}
