package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.exceptions.OrganizationalDepartmentNotFoundException;
import graduatethesis.performancemonitoringsystem.model.exceptions.OrganizationalDepartmentWorkingItemNotFoundException;
import graduatethesis.performancemonitoringsystem.model.exceptions.WorkingItemNotFoundException;
import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentWorkingItemHelper;
import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingForm;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartment;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentWorkingItem;
import graduatethesis.performancemonitoringsystem.model.organization.WorkingItem;
import graduatethesis.performancemonitoringsystem.repository.organization.EmployeeTrackingFormRepository;
import graduatethesis.performancemonitoringsystem.repository.organization.OrganizationalDepartmentRepository;
import graduatethesis.performancemonitoringsystem.repository.organization.OrganizationalDepartmentWorkingItemRepository;
import graduatethesis.performancemonitoringsystem.repository.organization.WorkingItemRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentWorkingItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationalDepartmentWorkingItemServiceImpl implements OrganizationalDepartmentWorkingItemService {

    private final OrganizationalDepartmentWorkingItemRepository organizationalDepartmentWorkingItemRepository;
    private final OrganizationalDepartmentRepository organizationalDepartmentRepository;
    private final WorkingItemRepository workingItemRepository;
    private final EmployeeTrackingFormRepository employeeTrackingFormRepository;

    public OrganizationalDepartmentWorkingItemServiceImpl(OrganizationalDepartmentWorkingItemRepository organizationalDepartmentWorkingItemRepository, OrganizationalDepartmentRepository organizationalDepartmentRepository, WorkingItemRepository workingItemRepository, EmployeeTrackingFormRepository employeeTrackingFormRepository) {
        this.organizationalDepartmentWorkingItemRepository = organizationalDepartmentWorkingItemRepository;
        this.organizationalDepartmentRepository = organizationalDepartmentRepository;
        this.workingItemRepository = workingItemRepository;
        this.employeeTrackingFormRepository = employeeTrackingFormRepository;
    }

    @Override
    public OrganizationalDepartmentWorkingItem findById(Long id) {
        return this.organizationalDepartmentWorkingItemRepository.findById(id).orElseThrow(OrganizationalDepartmentWorkingItemNotFoundException ::new);
    }

    @Override
    public OrganizationalDepartmentWorkingItem save(OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem) {
        return this.organizationalDepartmentWorkingItemRepository.save(organizationalDepartmentWorkingItem);
    }

    @Override
    public List<OrganizationalDepartmentWorkingItem> create(OrganizationalDepartmentWorkingItemHelper organizationalDepartmentWorkingItemHelper) {
        OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem = new OrganizationalDepartmentWorkingItem();
        OrganizationalDepartment organizationalDepartment = this.organizationalDepartmentRepository.findById(organizationalDepartmentWorkingItemHelper.getOrganizationalDepartmentId())
                .orElseThrow(OrganizationalDepartmentNotFoundException::new);
        List<Long> list= organizationalDepartmentWorkingItemHelper.getWorkingItemIds();
        List<OrganizationalDepartmentWorkingItem> lista = new ArrayList<>();
        List<EmployeeTrackingForm> employeeTrackingForms = this.employeeTrackingFormRepository.findAll();
        List<OrganizationalDepartmentWorkingItem> organizationalDepartmentWorkingItems = this.organizationalDepartmentWorkingItemRepository.findAll();
        for(OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem1:organizationalDepartmentWorkingItems){
            if(organizationalDepartmentWorkingItem1.getOrganizationalDepartment().getId().equals(organizationalDepartmentWorkingItemHelper.getOrganizationalDepartmentId())){
                boolean flag=false;
                for(EmployeeTrackingForm employeeTrackingForm:employeeTrackingForms) {

                    if (employeeTrackingForm.getOrganizationalDepartmentWorkingItem()!=null && organizationalDepartmentWorkingItem1.getId().equals(employeeTrackingForm.getOrganizationalDepartmentWorkingItem().getId())) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    organizationalDepartmentWorkingItemRepository.delete(organizationalDepartmentWorkingItem1);
                }
            }
        }


        for(Long l : list) {
            WorkingItem workingItem = this.workingItemRepository.findById(l).orElseThrow(() -> new WorkingItemNotFoundException(l));
            organizationalDepartmentWorkingItem.setOrganizationalDepartment(organizationalDepartment);
            organizationalDepartmentWorkingItem.setWorkingItem(workingItem);
            OrganizationalDepartmentWorkingItem organizationalDepartmentWorkingItem1 = this.organizationalDepartmentWorkingItemRepository.
                    findByOrganizationalDepartmentIdAndAndWorkingItemId(organizationalDepartmentWorkingItem.getOrganizationalDepartment().getId(),
                            organizationalDepartmentWorkingItem.getWorkingItem().getId());
            if(organizationalDepartmentWorkingItem1!=null) {
                continue;
            }else{
                organizationalDepartmentWorkingItemRepository.save(organizationalDepartmentWorkingItem);
                lista.add(organizationalDepartmentWorkingItem1);
            }
            organizationalDepartmentWorkingItem = new OrganizationalDepartmentWorkingItem();


        }
        return lista;
    }


    @Override
    public OrganizationalDepartmentWorkingItem findByOrganizationalDepartmentAndWorkingItem(Long oid, Long wid) {
            return this.organizationalDepartmentWorkingItemRepository.findByOrganizationalDepartmentIdAndAndWorkingItemId(oid, wid);
    }
}
