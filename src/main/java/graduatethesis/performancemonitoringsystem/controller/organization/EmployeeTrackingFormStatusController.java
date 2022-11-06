package graduatethesis.performancemonitoringsystem.controller.organization;

import graduatethesis.performancemonitoringsystem.model.exceptions.StatusNotFound;
import graduatethesis.performancemonitoringsystem.model.helpers.EmployeeTrackingFormStatusHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Status;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.EmployeeTrackingFormStatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/status")
@PreAuthorize("isAuthenticated()")
public class EmployeeTrackingFormStatusController {

    private final EmployeeTrackingFormStatusService employeeTrackingFormStatusService;

    public EmployeeTrackingFormStatusController(EmployeeTrackingFormStatusService employeeTrackingFormStatusService) {
        this.employeeTrackingFormStatusService = employeeTrackingFormStatusService;
    }

    @GetMapping("/all")
    public List<Status> findAll(){
        List<Status> statuses = this.employeeTrackingFormStatusService.findAll();
        return statuses;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','READ_USER_DATA','HEAD_READ_DATA')")
    @GetMapping (path = "/all/{page}/{size}")
    public Page<Status> getAllStatusesPageable(@PathVariable int page, @PathVariable int size){
        List<Status> statuses = this.employeeTrackingFormStatusService.findAll();

        statuses = statuses.stream().sorted((first, second) -> -first.getId().compareTo(second.getId())).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), statuses.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), statuses.size());
        return new PageImpl<>(statuses.subList(startIdx, endIdx), pageable, statuses.size());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping(value = "get/{id}")
    public Status getById(@PathVariable Long id){
        return this.employeeTrackingFormStatusService.findById(id).orElseThrow(()->new StatusNotFound(id));
    }


    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping (path = "/create")
    public ResponseEntity<Status> createEmployeeTrackingFormStatus(@RequestBody EmployeeTrackingFormStatusHelper employeeTrackingFormStatusHelper){
        try{
            Status status = employeeTrackingFormStatusService.createEmployeeTrackingFormStatus(employeeTrackingFormStatusHelper);
            return new ResponseEntity(status, HttpStatus.CREATED);
        }
        catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PutMapping(path ="/update")
    public ResponseEntity<Status> updateEmployeeTrackingFormStatus(@RequestBody EmployeeTrackingFormStatusHelper employeeTrackingFormStatusHelper){
        try{
            Status updatedStatus = employeeTrackingFormStatusService.updateEmployeeTrackingFormStatus(employeeTrackingFormStatusHelper);
            return new ResponseEntity(updatedStatus, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @DeleteMapping(path ="/delete/{id}")
    public void deleteEmployeeTrackingFormStatus(@PathVariable("id") long id){
        employeeTrackingFormStatusService.deleteEmployeeTrackingFormStatus(id);
    }

}
