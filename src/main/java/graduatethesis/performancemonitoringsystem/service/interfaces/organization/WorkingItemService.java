package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.WorkingItemHelper;
import graduatethesis.performancemonitoringsystem.model.organization.WorkingItem;

import java.util.List;
import java.util.Optional;

public interface WorkingItemService {

    List<WorkingItem> findAll();

    Optional<WorkingItem> findById(Long id);

    Optional<WorkingItem> findAllByName(String name);

    WorkingItem create(String name, String label);

    Optional<WorkingItem> create(WorkingItemHelper workingItemHelper);

    void deleteById(Long id);

    Optional<WorkingItem> update(Long id, String name, String label);

    Optional<WorkingItem> update(WorkingItemHelper workingItemHelper);

    List<WorkingItem> findAllByOrganizationalDepartment(Long id);
}
