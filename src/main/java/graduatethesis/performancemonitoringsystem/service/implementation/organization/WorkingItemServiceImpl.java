package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.exceptions.WorkingItemNotFoundException;
import graduatethesis.performancemonitoringsystem.model.helpers.WorkingItemHelper;
import graduatethesis.performancemonitoringsystem.model.organization.WorkingItem;
import graduatethesis.performancemonitoringsystem.repository.organization.WorkingItemRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.WorkingItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkingItemServiceImpl implements WorkingItemService {

    private final WorkingItemRepository workingItemRepository;

    public WorkingItemServiceImpl(WorkingItemRepository workingItemRepository) {
        this.workingItemRepository = workingItemRepository;
    }

    @Override
    public List<WorkingItem> findAll() {
        return this.workingItemRepository.findAll();
    }

    @Override
    public Optional<WorkingItem> findById(Long id) {
        return this.workingItemRepository.findById(id);
    }

    @Override
    public Optional<WorkingItem> findAllByName(String name) {
        return this.workingItemRepository.findByName(name);
    }

    @Override
    public WorkingItem create(String name, String label) {
        WorkingItem workingItem = new WorkingItem();
        workingItem.setName(name);
        workingItem.setLabel(label);
        return this.workingItemRepository.save(workingItem);
    }

    @Override
    public Optional<WorkingItem> create(WorkingItemHelper workingItemHelper) {
        WorkingItem workingItem = new WorkingItem();
        workingItem.setName(workingItemHelper.getName());
        workingItem.setLabel(workingItemHelper.getLabel());
        return Optional.of(this.workingItemRepository.save(workingItem));
    }

    @Override
    public void deleteById(Long id) {
        this.workingItemRepository.deleteById(id);
    }

    @Override
    public Optional<WorkingItem> update(Long id, String name, String label) {
        WorkingItem workingItem = this.workingItemRepository.findById(id).orElseThrow(()-> new WorkingItemNotFoundException(id));
        workingItem.setName(name);
        workingItem.setLabel(label);
        return Optional.of(this.workingItemRepository.save(workingItem));
    }

    @Override
    public Optional<WorkingItem> update(WorkingItemHelper workingItemHelper) {
        WorkingItem workingItem = this.workingItemRepository.findById(workingItemHelper.getId()).orElseThrow(()-> new WorkingItemNotFoundException(workingItemHelper.getId()));
        workingItem.setId(workingItemHelper.getId());
        workingItem.setName(workingItemHelper.getName());
        workingItem.setLabel(workingItemHelper.getLabel());
        return Optional.of(this.workingItemRepository.save(workingItem));
    }

    @Override
    public List<WorkingItem> findAllByOrganizationalDepartment(Long id) {
        return this.workingItemRepository.findAllByOrganizationalDepartment(id);
    }
}
