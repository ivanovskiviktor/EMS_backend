package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.filters.ReportFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportIdsHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.TimeSpentOnReportHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Report;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface ReportService {

    Optional<Report> save(ReportHelper reportHelper) throws MessagingException;

    Optional<Report> findById(Long id);

    List<Report> findAllCustom(ReportFilter reportFilter, Long userId, Pageable pageable);

    List<Report> findAllCustomForUser(ReportFilter reportFilter, Long id, Pageable pageable);

    List<Report> findAllCustomForHead(ReportFilter reportFilter, Long id, Pageable pageable, Boolean approvedByMe);

    void acceptReport(ReportIdsHelper reportIdsHelper);

    List<Report> findAll();

    List<Report> findAllByEmployeeTrackingForm(Long id);

    int getAllCustomCount(ReportFilter reportFilter, Long userId);

    int getAllCustomForUserCount(ReportFilter reportFilter, Long id);

    int getAllCustomForHeadCount(ReportFilter reportFilter, Long id, Boolean approvedByMe);

    int getAllCustomForHeadNotAcceptedCount(Long userId);

    TimeSpentOnReportHelper timeSpentOnReportsByUser(ReportFilter reportFilter);

    List<Report> getAllNotAcceptedReportsForHead(Long id);
}
