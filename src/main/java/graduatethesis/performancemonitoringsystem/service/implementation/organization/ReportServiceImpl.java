package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.constants.UserRoles;
import graduatethesis.performancemonitoringsystem.model.exceptions.*;
import graduatethesis.performancemonitoringsystem.model.filters.ReportFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportFrontHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportIdsHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.TimeSpentOnReportHelper;
import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingForm;
import graduatethesis.performancemonitoringsystem.model.organization.Report;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.organization.ReportRepository;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.PrivilegeService;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.EmployeeTrackingFormService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.ReportService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.time.DayOfWeek.*;


@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final EmployeeTrackingFormService employeeTrackingFormService;
    private final PrivilegeService privilegeService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LoggedUserService loggedUserService;
    private final OrganizationalDepartmentUserService organizationalDepartmentUserService;

    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);

    public ReportServiceImpl(ReportRepository reportRepository, EmployeeTrackingFormService employeeTrackingFormService, PrivilegeService privilegeService, UserRepository userRepository, UserService userService, LoggedUserService loggedUserService, OrganizationalDepartmentUserService organizationalDepartmentUserService) {
        this.reportRepository = reportRepository;
        this.employeeTrackingFormService = employeeTrackingFormService;
        this.privilegeService = privilegeService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.loggedUserService = loggedUserService;
        this.organizationalDepartmentUserService = organizationalDepartmentUserService;
    }


    @Override
    public Optional<Report> save(ReportHelper reportHelper) throws MessagingException {
        Report report = null;
        EmployeeTrackingForm employeeTrackingForm = null;

        if (reportHelper.getId() != null) {
            if (this.reportRepository.existsById(reportHelper.getId())) {
                report = this.reportRepository.getById(reportHelper.getId());
                if (loggedUserService.getLoggedUser().getUserRoles().get(0).getRole().getName().equals(UserRoles.ROLE_PREFIX + UserRoles.ROLE_EMPLOYEE) && !report.getEditable()) {
                    throw new ReportNotEditableException();
                } else {
                    employeeTrackingForm = this.employeeTrackingFormService.findById(reportHelper.getEmployeeTrackingFormId())
                            .orElseThrow(() -> new EmployeeTrackingFormNotFoundException(reportHelper.getEmployeeTrackingFormId()));
                    report.setDescription(reportHelper.getDescription());
                    report.setHours(reportHelper.getHours());
                    report.setMinutes(reportHelper.getMinutes());
                    LocalDateTime now = LocalDateTime.now();

                    if (now.getDayOfWeek() == SATURDAY) {
                        LocalDateTime friday = now.minusDays(1);
                        ZoneId zoneId = ZoneId.systemDefault();
                        OffsetDateTime odt = friday.atZone(zoneId).toOffsetDateTime();
                        report.setSubmissionDate(odt);
                    } else if (now.getDayOfWeek() == SUNDAY) {
                        LocalDateTime friday = now.minusDays(2);
                        ZoneId zoneId = ZoneId.systemDefault();
                        OffsetDateTime odt = friday.atZone(zoneId).toOffsetDateTime();
                        report.setSubmissionDate(odt);
                    } else if (now.getDayOfWeek() == MONDAY && now.getHour() < 10) {
                        LocalDateTime friday = now.minusDays(3);
                        ZoneId zoneId = ZoneId.systemDefault();
                        OffsetDateTime odt = friday.atZone(zoneId).toOffsetDateTime();
                        report.setSubmissionDate(odt);
                    } else if (now.getHour() < 10) {
                        LocalDateTime yesterday = now.minusDays(1);
                        ZoneId zoneId = ZoneId.systemDefault();
                        OffsetDateTime odt = yesterday.atZone(zoneId).toOffsetDateTime();
                        report.setSubmissionDate(odt);
                    } else {
                        ZoneId zoneId = ZoneId.systemDefault();
                        OffsetDateTime odt = now.atZone(zoneId).toOffsetDateTime();
                        report.setSubmissionDate(odt);
                    }

                    if (employeeTrackingForm.getStatus().getName().equals("done")) {
                        if (loggedUserService.getLoggedUser().getUserRoles().get(0).getRole().getName().equals(UserRoles.ROLE_PREFIX + UserRoles.ROLE_HEAD_OF_DEPARTMENT_SHORT)) {
                            report = Optional.of(this.reportRepository.save(report)).get();
                            report.setEditable(false);
                        } else {
                            report.setEditable(false);
                            throw new ReportCanNotBeCreatedException();
                        }
                    }


                }
            }
        } else {
            employeeTrackingForm = this.employeeTrackingFormService.findById(reportHelper.getEmployeeTrackingFormId())
                    .orElseThrow(() -> new EmployeeTrackingFormNotFoundException(reportHelper.getEmployeeTrackingFormId()));

            report = new Report();
            report.setDescription(reportHelper.getDescription());
            report.setHours(reportHelper.getHours());
            report.setMinutes(reportHelper.getMinutes());
            report.setEditable(true);
            report.setAccepted(null);
            report.setSubmitter(loggedUserService.getLoggedUser());

            LocalDateTime now = LocalDateTime.now();

            if(now.getDayOfWeek() == SATURDAY){
                LocalDateTime friday = now.minusDays(1);
                ZoneId zoneId = ZoneId.systemDefault();
                OffsetDateTime odt = friday.atZone(zoneId).toOffsetDateTime();
                report.setSubmissionDate(odt);
            }else if(now.getDayOfWeek() == SUNDAY){
                LocalDateTime friday = now.minusDays(2);
                ZoneId zoneId = ZoneId.systemDefault();
                OffsetDateTime odt = friday.atZone(zoneId).toOffsetDateTime();
                report.setSubmissionDate(odt);
            }else if(now.getDayOfWeek() == MONDAY && now.getHour()<10){
                LocalDateTime friday = now.minusDays(3);
                ZoneId zoneId = ZoneId.systemDefault();
                OffsetDateTime odt = friday.atZone(zoneId).toOffsetDateTime();
                report.setSubmissionDate(odt);
            }else if(now.getHour()<10){
                LocalDateTime yesterday = now.minusDays(1);
                ZoneId zoneId = ZoneId.systemDefault();
                OffsetDateTime odt = yesterday.atZone(zoneId).toOffsetDateTime();
                report.setSubmissionDate(odt);
            }else  {
                report.setEmployeeTrackingForm(employeeTrackingForm);
                report.setSubmissionDate(OffsetDateTime.now());
            }
        }
        report.setEmployeeTrackingForm(employeeTrackingForm);
        report.setSubmissionDate(OffsetDateTime.now());
        report = Optional.of(this.reportRepository.save(report)).get();

        employeeTrackingForm.setDateModified(OffsetDateTime.now());
        this.employeeTrackingFormService.create(employeeTrackingForm);
        return Optional.of(report);
    }


    @Override
    public Optional<Report> findById(Long id) {
        return this.reportRepository.findById(id);
    }

    @Override
    public List<Report> findAllCustom(ReportFilter reportFilter, Boolean approvedByMe, Long userId, Pageable pageable) {
        reportFilter.setStartDate(reportFilter.getStartDate().plusDays(1));
        return this.reportRepository.findAllCustom(reportFilter, approvedByMe, userId, pageable).toList();
    }

    @Override
    public List<Report> findAllCustomForUser(ReportFilter reportFilter, Long id, Pageable pageable) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User loggedUser = this.loggedUserService.getLoggedUser();
        String thisUser = user.getEmail();
        String loggedUserEmail = loggedUser.getEmail();
        reportFilter.setStartDate(reportFilter.getStartDate().plusDays(1));
        if (!thisUser.equals(loggedUserEmail)) {
            throw new DifferentPrivilegeException();
        }else {
            return this.reportRepository.findAllCustomForUser(reportFilter, user.getId(), pageable).toList();
        }

    }

    @Override
    public List<Report> findAllCustomForHead(ReportFilter reportFilter, Long id, Pageable pageable, Boolean approvedByMe) {
        List<Report> reports = new ArrayList<>();
        List<Long> organizationalDepartmentsForHead = new ArrayList<>();
        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User loggedUser = this.loggedUserService.getLoggedUser();
        String thisUser = user.getEmail();
        String loggedUserEmail = loggedUser.getEmail();
        reportFilter.setStartDate(reportFilter.getStartDate().plusDays(1));
        if (!thisUser.equals(loggedUserEmail)) {
            throw new DifferentPrivilegeException();
        }else if(privilegeService.loggedUserHasAnyPrivilege("HEAD_READ_DATA")) {
            //get all
            for(int i=0; i<loggedUser.getOrganizationalDepartmentUsers().size(); i++){
                if(loggedUser.getOrganizationalDepartmentUsers().get(i).getIsHead()){
                    organizationalDepartmentsForHead.add(loggedUser.getOrganizationalDepartmentUsers().get(i).getOrganizationalDepartment().getId());
                }
            }
            reports.addAll(this.reportRepository.findAllForHeads(loggedUser.getId(), reportFilter, organizationalDepartmentsForHead, pageable, approvedByMe).map(ReportFrontHelper::getReport).toList());
        }

        return reports;

    }



    @Override
    public void acceptReport(ReportIdsHelper reportIdsHelper) {
        List<Long> reportIds=reportIdsHelper.getReportIds();
        List<Report> reports = this.reportRepository.findReportsById(reportIds);
        for (Report report : reports) {
            if (report.getAccepted() == null) {
                report.setAccepted(true);
                report.setApprover(loggedUserService.getLoggedUser());
            }

            this.reportRepository.save(report);
        }

    }

    @Override
    public List<Report> findAll() {
        return this.reportRepository.findAll();
    }

    @Override
    public List<Report> findAllByEmployeeTrackingForm(Long id) {
        return this.reportRepository.findAllByEmployeeTrackingForm(id);
    }

    @Override
    public int getAllCustomCount(ReportFilter reportFilter, Boolean approvedByMe, Long userId) {
        return this.reportRepository.getAllCustomCount(reportFilter, approvedByMe, userId);
    }

    @Override
    public int getAllCustomForUserCount(ReportFilter reportFilter, Long id) {
        return this.reportRepository.getAllCustomForUserCount(reportFilter, id);
    }

    @Override
    public int getAllCustomForHeadCount(ReportFilter reportFilter, Long id, Boolean approvedByMe) {
        User loggedUser = loggedUserService.getLoggedUser();
        List<Long> organizationalUnitsForHead = new ArrayList<>();
        for(int i=0; i<loggedUser.getOrganizationalDepartmentUsers().size(); i++){
            if(loggedUser.getOrganizationalDepartmentUsers().get(i).getIsHead()){
                organizationalUnitsForHead.add(loggedUser.getOrganizationalDepartmentUsers().get(i).getOrganizationalDepartment().getId());
            }
        }

        return this.reportRepository.getAllCustomForHeadCount(reportFilter, id, organizationalUnitsForHead, approvedByMe);
    }

    @Override
    public int getAllCustomForHeadNotAcceptedCount(Long userId) {
        User loggedUser = loggedUserService.getLoggedUser();
        if(loggedUser.getUserRoles().get(0).getRole().getName().equals(UserRoles.ROLE_HEAD_OF_DEPARTMENT)){
            List<Long> organizationalUnitsForHead = new ArrayList<>();
            for(int i=0; i<loggedUser.getOrganizationalDepartmentUsers().size(); i++){
                if(loggedUser.getOrganizationalDepartmentUsers().get(i).getIsHead()){
                    organizationalUnitsForHead.add(loggedUser.getOrganizationalDepartmentUsers().get(i).getOrganizationalDepartment().getId());
                }
            }
            return this.reportRepository.getAllCustomForHeadNotAcceptedCount(loggedUser.getId(), organizationalUnitsForHead);
        }else{
            return 0;
        }
    }

    @Override
    public TimeSpentOnReportHelper timeSpentOnReportsByUser(ReportFilter reportFilter) {
        reportFilter.setStartDate(reportFilter.getStartDate().plusDays(1));
        List<Report> reports = this.reportRepository.getReportsWithNameAndDate(reportFilter);
        TimeSpentOnReportHelper timeSpentOnReportHelper=new TimeSpentOnReportHelper();
        int hours=0;
        int minutes=0;
        for(Report report:reports){
            if(report.getHours()!=null) {
                hours += report.getHours();
            }
            if(report.getMinutes()!=null) {
                minutes += report.getMinutes();
            }
        }
        int hour=minutes/60;
        minutes%=60;
        timeSpentOnReportHelper.setHours(hours+hour);
        timeSpentOnReportHelper.setMinutes(minutes);
        return timeSpentOnReportHelper;

    }

    @Override
    public List<Report> getAllNotAcceptedReportsForHead(Long id) {
        List<Report> reports = new ArrayList<>();
        User loggedUser = loggedUserService.getLoggedUser();
        if(loggedUser.getUserRoles().get(0).getRole().getName().equals(UserRoles.ROLE_HEAD_OF_DEPARTMENT)){
            List<Long> organizationalUnitsForHead = new ArrayList<>();
            for(int i=0; i<loggedUser.getOrganizationalDepartmentUsers().size(); i++){
                if(loggedUser.getOrganizationalDepartmentUsers().get(i).getIsHead()){
                    organizationalUnitsForHead.add(loggedUser.getOrganizationalDepartmentUsers().get(i).getOrganizationalDepartment().getId());
                }
            }
            reports.addAll(this.reportRepository.getAllNotAcceptedReportsForHead(loggedUser.getId(), organizationalUnitsForHead));
            return reports;
        }else{
            return null;
        }
    }

}

