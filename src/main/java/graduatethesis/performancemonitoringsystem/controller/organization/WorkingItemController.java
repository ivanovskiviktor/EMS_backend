package graduatethesis.performancemonitoringsystem.controller.organization;

import graduatethesis.performancemonitoringsystem.model.exceptions.WorkingItemNotFoundException;
import graduatethesis.performancemonitoringsystem.model.helpers.WorkingItemHelper;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentWorkingItem;
import graduatethesis.performancemonitoringsystem.model.organization.WorkingItem;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentWorkingItemService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.WorkingItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/workingItem")
public class WorkingItemController {

    private final WorkingItemService workingItemService;
    private final OrganizationalDepartmentWorkingItemService organizationalDepartmentWorkingItemService;
    private final LoggedUserService loggedUserService;

    public WorkingItemController(WorkingItemService workingItemService, OrganizationalDepartmentWorkingItemService organizationalDepartmentWorkingItemService, LoggedUserService loggedUserService) {
        this.workingItemService = workingItemService;
        this.organizationalDepartmentWorkingItemService = organizationalDepartmentWorkingItemService;
        this.loggedUserService = loggedUserService;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','READ_USER_DATA','HEAD_READ_DATA')")
    @GetMapping("/all")
    public List<WorkingItem> findAll(){
        return this.workingItemService.findAll();
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','READ_USER_DATA','HEAD_READ_DATA')")
    @GetMapping("/get/{page}/{size}")
    public Page<WorkingItem> findAllPageable(@PathVariable int page, @PathVariable int size){

        List<WorkingItem> workingItems = this.workingItemService.findAll();

        workingItems = workingItems.stream().sorted((first, second) -> -first.getId().compareTo(second.getId())).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), workingItems.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), workingItems.size());
        return new PageImpl<>(workingItems.subList(startIdx, endIdx), pageable, workingItems.size());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','READ_USER_DATA','HEAD_READ_DATA')")
    @GetMapping("/allByOrganizationalDepartment/{id}")
    public List<WorkingItemHelper> findAllByOrganizationalDepartment(@PathVariable Long id){
        List<WorkingItemHelper> list =  this.workingItemService.findAllByOrganizationalDepartment(id).stream().map(WorkingItem::getAsWorkingItemHelper).collect(Collectors.toList());

        for (var item:list) {
            OrganizationalDepartmentWorkingItem odwi = organizationalDepartmentWorkingItemService.findByOrganizationalDepartmentAndWorkingItem(id, item.getId());
            if(odwi!=null){
                if(odwi.getEmployeeTrackingForms()!=null && odwi.getEmployeeTrackingForms().size()>0){
                    item.setPresentInEmployeeTrackingForm(true);
                }
            }else{
                item.setPresentInEmployeeTrackingForm(false);
            }
        }

        list = list.stream().sorted().collect(Collectors.toList());
        return list;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping(value = "get/{id}")
    public WorkingItem getById(@PathVariable Long id){
        return this.workingItemService.findById(id).orElseThrow(()->new WorkingItemNotFoundException(id));
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping("/create")
    public ResponseEntity<WorkingItem> save(@RequestBody WorkingItemHelper workingItemHelper){
        return this.workingItemService.create(workingItemHelper)
                .map(workingItem -> ResponseEntity.ok().body(workingItem))
                .orElseGet(()-> ResponseEntity.badRequest().build());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PutMapping("/update")
    public ResponseEntity<WorkingItem> update(@RequestBody WorkingItemHelper workingItemHelper){
        return this.workingItemService.update(workingItemHelper)
                .map(workingItem -> ResponseEntity.ok().body(workingItem))
                .orElseGet(()-> ResponseEntity.badRequest().build());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @DeleteMapping("delete/{id}")
    public void deleteById(@PathVariable Long id){
        this.workingItemService.deleteById(id);
    }

}
