package graduatethesis.performancemonitoringsystem.controller.organization;

import graduatethesis.performancemonitoringsystem.model.filters.ReportFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportIdsHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.TimeSpentOnReportHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Report;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.PrivilegeService;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/report")
public class ReportController {

    private final ReportService reportService;
    private final PrivilegeService privilegeService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LoggedUserService loggedUserService;

    public ReportController(ReportService reportService, PrivilegeService privilegeService, UserRepository userRepository, UserService userService, LoggedUserService loggedUserService) {
        this.reportService = reportService;
        this.privilegeService = privilegeService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.loggedUserService = loggedUserService;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping(value = "/get")
    public List<Report> findAll(){
        return this.reportService.findAll();
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping("/all/{page}/{size}")
    public Page<ReportHelper> findAllPageable(@PathVariable int page, @PathVariable int size, @RequestBody ReportFilter reportFilter, @RequestHeader(required = false) Boolean approvedByMe) {
        List<ReportHelper> reports = new ArrayList<>();
        User user = this.loggedUserService.getLoggedUser();
        int count = 0;
        if(privilegeService.loggedUserHasAnyPrivilege("ACCESS_ALL")){
                reports = this.reportService.findAllCustom(reportFilter, approvedByMe, user.getId(), PageRequest.of(page,size)).stream().map(r->r.getAsReportHelper(user)).collect(Collectors.toList());
            count = this.reportService.getAllCustomCount(reportFilter, approvedByMe, user.getId());
        }
        else if(privilegeService.loggedUserHasAnyPrivilege("READ_USER_DATA")) {
            reports = this.reportService.findAllCustomForUser(reportFilter, user.getId(), PageRequest.of(page,size)).stream().map(r->r.getAsReportHelper(user)).collect(Collectors.toList());
            count = this.reportService.getAllCustomForUserCount(reportFilter, user.getId());
        }
        else{
            reports = this.reportService.findAllCustomForHead(reportFilter, user.getId(), PageRequest.of(page, size), approvedByMe).stream().map(r->r.getAsReportHelper(user)).collect(Collectors.toList());
            count = this.reportService.getAllCustomForHeadCount(reportFilter, user.getId(), approvedByMe);
        }

        if(reportFilter.getStartDate() != null && reportFilter.getEndDate() != null){
            reports.removeIf(r->r.getSubmissionDate().isAfter(reportFilter.getEndDate()) || r.getSubmissionDate().toLocalDate().isBefore(reportFilter.getStartDate()));
        }



        return new PageImpl<>(reports, PageRequest.of(page, size), count);

    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping(value="/get/{id}")
    public Optional<Report> findById(@PathVariable Long id){
        Optional<Report> report = this.reportService.findById(id);
        return report;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value="/create")
    @Transactional(rollbackFor = Exception.class)
    public Optional<Report> save(@RequestBody ReportHelper reportHelper) throws MessagingException {
        return this.reportService.save(reportHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value = "/acceptReports")
    public void acceptReport(@RequestBody ReportIdsHelper reportIdsHelper){
        this.reportService.acceptReport(reportIdsHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping("/getNumberOfNotApprovedReportsForUser")
    public int getNumberOfNotApprovedReportsForUser()
    {
        User user = this.loggedUserService.getLoggedUser();
        return this.reportService.getAllCustomForHeadNotAcceptedCount(user.getId());

    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value = "/timeSpentOnReportsByUser")
    public TimeSpentOnReportHelper timeSpentOnReportsByUser(@RequestBody ReportFilter reportFilter){
        return this.reportService.timeSpentOnReportsByUser(reportFilter);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping("/getNotApprovedReportsForLoggedUser")
    public List<ReportHelper> getNotApprovedReportsForLoggedUser()
    {
        List<ReportHelper> reportList = new ArrayList<>();

        User user = this.loggedUserService.getLoggedUser();

        reportList = this.reportService.getAllNotAcceptedReportsForHead(user.getId()).stream().map(r->r.getAsReportHelper(user)).collect(Collectors.toList());
        return reportList;
    }


}
