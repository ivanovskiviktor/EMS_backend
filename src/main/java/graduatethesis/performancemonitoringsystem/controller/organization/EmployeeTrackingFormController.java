package graduatethesis.performancemonitoringsystem.controller.organization;

import graduatethesis.performancemonitoringsystem.model.filters.EmployeeTrackingFormFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.*;
import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingForm;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.organization.EmployeeTrackingFormRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.PrivilegeService;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.EmployeeTrackingFormService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.EmployeeTrackingFormStatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/employeeTrackingForm")
@PreAuthorize("isAuthenticated()")
public class EmployeeTrackingFormController {

    private final EmployeeTrackingFormService employeeTrackingFormService;
    private final EmployeeTrackingFormRepository employeeTrackingFormRepository;
    private final PrivilegeService privilegeService;
    private final LoggedUserService loggedUserService;
    private final UserService userService;
    private final EmployeeTrackingFormStatusService employeeTrackingFormStatusService;


    public EmployeeTrackingFormController(EmployeeTrackingFormService employeeTrackingFormService, EmployeeTrackingFormRepository employeeTrackingFormRepository, PrivilegeService privilegeService, LoggedUserService loggedUserService, UserService userService, EmployeeTrackingFormStatusService employeeTrackingFormStatusService) {
        this.employeeTrackingFormService = employeeTrackingFormService;
        this.employeeTrackingFormRepository = employeeTrackingFormRepository;
        this.privilegeService = privilegeService;
        this.loggedUserService = loggedUserService;
        this.userService = userService;
        this.employeeTrackingFormStatusService = employeeTrackingFormStatusService;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping(value = "/get")
    public List<EmployeeTrackingForm> findAll(){
        return this.employeeTrackingFormService.findAll();
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping(value = "/get/{id}")
    public Optional<EmployeeTrackingForm> findById(@PathVariable Long id){
        return this.employeeTrackingFormService.findById(id);
    }


    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value="/create")
    @Transactional(rollbackFor = Exception.class)
    public Optional<EmployeeTrackingForm> save(@RequestBody EmployeeTrackingFormHelper employeeTrackingFormHelper){
        return this.employeeTrackingFormService.save(employeeTrackingFormHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','READ_USER_DATA','HEAD_READ_DATA')")
    @PutMapping("/update")
    public Optional<EmployeeTrackingForm> update(@RequestBody EmployeeTrackingFormHelper employeeTrackingFormHelper){
        return this.employeeTrackingFormService.update(employeeTrackingFormHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL', 'READ_USER_DATA','HEAD_READ_DATA')")
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id){
        this.employeeTrackingFormService.deleteById(id);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value = "/exportNotDone")
    public ResponseEntity exportToExcelNotDone(HttpServletResponse response, @RequestBody EmployeeTrackingFormFilter employeeTrackingFormFilter) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        String filename="employee_tracking_forms_not_done.xlsx";
        byte [] content = employeeTrackingFormService.export(response, employeeTrackingFormFilter, false);
        String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType + "; charset=utf-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+filename+"\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                .body(content);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value = "/exportDone")
    public ResponseEntity exportToExcelDone(HttpServletResponse response,@RequestBody EmployeeTrackingFormFilter employeeTrackingFormFilter) throws IOException{

        response.setContentType("application/vnd.ms-excel");
        String filename="employee_tracking_forms_done.xlsx";
        byte [] content = employeeTrackingFormService.export(response, employeeTrackingFormFilter, true);
        String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType + "; charset=utf-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+filename+"\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                .body(content);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping(value = "/getOrderedByOrganizationalDepartment/{page}/{size}")
    public Page<ReportTotalTasksHelper> findAllOrderedByOrganizationalDepartment(@PathVariable int page, @PathVariable int size){
        List<ReportTotalTasksHelper> list = this.employeeTrackingFormService.findAllOrderedByOrganizationalDepartment();

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), list.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startIdx, endIdx), pageable, list.size());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping(value = "/getReportByEmployee/{page}/{size}")
    public Page<ReportTotalTasksByEmployeeHelper> findAllOrderedByEmployee(@PathVariable int page, @PathVariable int size){
        List<ReportTotalTasksByEmployeeHelper> list =  this.employeeTrackingFormService.findAllOrderedByEmployee();

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), list.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startIdx, endIdx), pageable, list.size());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/getAllByTime/{page}/{size}")
    public Page<ReportTasksByTimeHelper> findAllByTime(@RequestBody DateHelper dateHelper, @PathVariable int page, @PathVariable int size){
        List<ReportTasksByTimeHelper> list = this.employeeTrackingFormService.findAllByTime(dateHelper);

        Pageable pageable = PageRequest.of(page, size);
        int startIdx = Math.min((int) pageable.getOffset(), list.size());
        int endIdx = Math.min(startIdx + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startIdx, endIdx), pageable, list.size());
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping(value = "/getAllByTime")
    public List<ReportTasksByTimeHelper> findAllByTime(@RequestBody DateHelper dateHelper){
        List<ReportTasksByTimeHelper> list = this.employeeTrackingFormService.findAllByTime(dateHelper);

        return list;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping(value = "/getReportByEmployee")
    public List<ReportTotalTasksByEmployeeHelper> findAllOrderedByEmployee(){
        List<ReportTotalTasksByEmployeeHelper> list =  this.employeeTrackingFormService.findAllOrderedByEmployee();

        return list;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping(value = "/getOrderedByOrganizationalDepartment")
    public List<ReportTotalTasksHelper> findAllOrderedByOrganizationalDepartment(){
        List<ReportTotalTasksHelper> list = this.employeeTrackingFormService.findAllOrderedByOrganizationalDepartment();

        return list;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping(value = "/closeTask/{id}")
    public void closeTask(@PathVariable Long id) throws MessagingException {
        this.employeeTrackingFormService.closeTask(id);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA')")
    @PostMapping("/updateClosedTask/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void updateClosedTask(@PathVariable Long id){
        this.employeeTrackingFormService.updateClosedTask(id);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping("/allWithStatusNotDone/{page}/{size}")
    public Page<EmployeeTrackingFormHelperFront> findAllWithStatusNotDonePageable(@PathVariable int page, @PathVariable int size, @RequestBody EmployeeTrackingFormFilter employeeTrackingFormFilter) {
        User user = this.loggedUserService.getLoggedUser();
        List<EmployeeTrackingForm> employeeTrackingForms;
        List<EmployeeTrackingFormHelperFront> employeeTrackingFormHelperFronts = new ArrayList<>();
        if(employeeTrackingFormFilter.getStartDate()!=null && employeeTrackingFormFilter.getStartDate().getMinute()!=0){
            OffsetDateTime newStartDate = employeeTrackingFormService.setOffsetDateTimeToZero(employeeTrackingFormFilter.getStartDate());
            employeeTrackingFormFilter.setStartDate(newStartDate);
        }

        if(employeeTrackingFormFilter.getEndDate()!=null && employeeTrackingFormFilter.getEndDate().getMinute()!=0){
            OffsetDateTime newEndDate = employeeTrackingFormService.setOffsetDateTimeToZero(employeeTrackingFormFilter.getEndDate());
            employeeTrackingFormFilter.setEndDate(newEndDate);
        }

        if(privilegeService.loggedUserHasAnyPrivilege("ACCESS_ALL")) {
            employeeTrackingForms = this.employeeTrackingFormService.findAllCustomPaginated("in_progress", employeeTrackingFormFilter, PageRequest.of(page, size)).toList();
        }
        else if (privilegeService.loggedUserHasAnyPrivilege("READ_USER_DATA")) {
            employeeTrackingForms = this.employeeTrackingFormService.findAllByUserPaginated("in_progress", employeeTrackingFormFilter, user.getId(), PageRequest.of(page,size)).toList();
        }
        else {
            employeeTrackingForms = this.employeeTrackingFormService.findAllFormsForUserPaginated("in_progress", employeeTrackingFormFilter, user.getId(), PageRequest.of(page,size));
        }

        for(EmployeeTrackingForm employeeTrackingForm: employeeTrackingForms) {
            employeeTrackingFormHelperFronts.add(employeeTrackingForm.getAsEmployeeTrackingFormHelperFront(loggedUserService.getLoggedUser()));
        }

        employeeTrackingFormHelperFronts = employeeTrackingFormHelperFronts.stream().sorted((first, second) -> -first.getDateModified().compareTo(second.getDateModified())).collect(Collectors.toList());

        var forms = new PageImpl<>(employeeTrackingFormHelperFronts, PageRequest.of(page, size),employeeTrackingFormHelperFronts.size());
        return forms;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping("/allWithStatusDone/{page}/{size}")
    public Page<EmployeeTrackingFormHelperFront> findAllWithStatusDonePageable(@PathVariable int page, @PathVariable int size, @RequestBody EmployeeTrackingFormFilter employeeTrackingFormFilter) {
        User user = this.loggedUserService.getLoggedUser();
        List<EmployeeTrackingForm> employeeTrackingForms;
        List<EmployeeTrackingFormHelperFront> employeeTrackingFormHelperFronts = new ArrayList<>();
        if(employeeTrackingFormFilter.getStartDate()!=null && employeeTrackingFormFilter.getStartDate().getMinute()!=0){
            OffsetDateTime newStartDate = employeeTrackingFormService.setOffsetDateTimeToZero(employeeTrackingFormFilter.getStartDate());
            employeeTrackingFormFilter.setStartDate(newStartDate);
        }

        if(employeeTrackingFormFilter.getEndDate()!=null && employeeTrackingFormFilter.getEndDate().getMinute()!=0){
            OffsetDateTime newEndDate = employeeTrackingFormService.setOffsetDateTimeToZero(employeeTrackingFormFilter.getEndDate());
            employeeTrackingFormFilter.setEndDate(newEndDate);
        }

        if(privilegeService.loggedUserHasAnyPrivilege("ACCESS_ALL")) {
            employeeTrackingForms = this.employeeTrackingFormService.findAllCustomPaginated("done", employeeTrackingFormFilter, PageRequest.of(page, size)).toList();
        }
        else if (privilegeService.loggedUserHasAnyPrivilege("READ_USER_DATA")) {
            employeeTrackingForms = this.employeeTrackingFormService.findAllByUserPaginated("done", employeeTrackingFormFilter, user.getId(), PageRequest.of(page,size)).toList();
        }
        else {
            employeeTrackingForms = this.employeeTrackingFormService.findAllFormsForUserPaginated("done", employeeTrackingFormFilter, user.getId(), PageRequest.of(page,size));
        }

        for(EmployeeTrackingForm employeeTrackingForm: employeeTrackingForms) {
            employeeTrackingFormHelperFronts.add(employeeTrackingForm.getAsEmployeeTrackingFormHelperFront(loggedUserService.getLoggedUser()));
        }

        employeeTrackingFormHelperFronts = employeeTrackingFormHelperFronts.stream().sorted((first, second) -> -first.getDateModified().compareTo(second.getDateModified())).collect(Collectors.toList());

        var forms = new PageImpl<>(employeeTrackingFormHelperFronts, PageRequest.of(page, size),employeeTrackingFormHelperFronts.size());
        return forms;
    }

}
