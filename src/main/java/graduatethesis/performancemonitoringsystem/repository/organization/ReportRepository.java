package graduatethesis.performancemonitoringsystem.repository.organization;

import graduatethesis.performancemonitoringsystem.model.filters.ReportFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.ReportFrontHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query("select r from Report r where r.id in :reportIds")
    List<Report> findReportsById(List<Long> reportIds);

    @Query("select r from Report r where r.employeeTrackingForm.id = :id order by r.id desc")
    List<Report> findAllByEmployeeTrackingForm(Long id);

    @Query("SELECT r from Report r left join r.approver approver left join approver.person person where (:#{#reportFilter.employeeTrackingFormId==null} = true " +
            "or :#{#reportFilter.employeeTrackingFormId} = r.employeeTrackingForm.id) and ((:#{#approvedByMe==null}=true and r.submitter.head.id <> :id) or (:#{#approvedByMe} = true and r.submitter.head.id = :id) or (:#{#approvedByMe} = false and r.submitter.head.id <> :id)) and "+
            "(:#{#reportFilter.submitterEmail==null}=true or lower(r.submitter.email) like %:#{#reportFilter.submitterEmail}%) and "+
            "(:#{#reportFilter.submitterFirstNameLastName==null}=true or lower(concat(concat(r.submitter.person.firstName,' '),r.submitter.person.lastName)) like %:#{#reportFilter.getsubmitterFirstNameLastNameToLower()}%) and " +
            "(:#{#reportFilter.getapproverFirstNameLastNameToLower()==null}=true or lower(concat(concat(person.firstName,' '),person.lastName)) like %:#{#reportFilter.getapproverFirstNameLastNameToLower()}%) and " +
            "(:#{#reportFilter.description==null}=true or lower(r.description) like %:#{#reportFilter.getDescriptionToLower()}%) and "+
            "(:#{#reportFilter.startDate==null}=true or CAST(r.submissionDate as LocalDate) = :#{#reportFilter.startDate}) and " +
            "(:#{#reportFilter.approverEmail==null}=true or lower(approver.email) like %:#{#reportFilter.approverEmail}%) order by date(r.submissionDate) desc, r.submitter.email desc")
    Page<Report> findAllCustom(@Param("reportFilter") ReportFilter reportFilter, Boolean approvedByMe, Long id, Pageable pageable);

    @Query("SELECT r from Report r left join r.approver approver left join approver.person person join EmployeeTrackingForm etf on r.employeeTrackingForm.id = etf.id join EmployeeTrackingFormUser etfu on etfu.employeeTrackingForm.id = etf.id where etfu.user.id = :id and (:#{#reportFilter.employeeTrackingFormId==null} = true " +
            "or :#{#reportFilter.employeeTrackingFormId} = r.employeeTrackingForm.id) and "+
            "(:#{#reportFilter.submitterEmail==null}=true or lower(r.submitter.email) like %:#{#reportFilter.submitterEmail}%) and "+
            "(:#{#reportFilter.description==null}=true or lower(r.description) like %:#{#reportFilter.getDescriptionToLower()}%) and "+
            "(:#{#reportFilter.submitterFirstNameLastName==null}=true or lower(concat(concat(r.submitter.person.firstName,' '),r.submitter.person.lastName)) like %:#{#reportFilter.getsubmitterFirstNameLastNameToLower()}%) and " +
            "(:#{#reportFilter.getapproverFirstNameLastNameToLower()==null}=true or lower(concat(concat(person.firstName,' '),person.lastName)) like %:#{#reportFilter.getapproverFirstNameLastNameToLower()}%) and " +
            "(:#{#reportFilter.startDate==null}=true or CAST(r.submissionDate as LocalDate) = :#{#reportFilter.startDate}) and " +
            "(:#{#reportFilter.approverEmail==null}=true or lower(approver.email) like %:#{#reportFilter.approverEmail}%) order by r.submissionDate desc, r.submitter.email desc")
    Page<Report> findAllCustomForUser(@Param("reportFilter") ReportFilter reportFilter, Long id, Pageable pageable);


    @Query(value = "select new graduatethesis.performancemonitoringsystem.model.helpers.ReportFrontHelper(r, r.submitter.email) from Report r " +
            "join EmployeeTrackingFormUser etfu on r.employeeTrackingForm.id = etfu.employeeTrackingForm.id " +
            "left join r.approver approver left join approver.person person " +
            "join OrganizationalDepartmentWorkingItem odwi on odwi.id = r.employeeTrackingForm.organizationalDepartmentWorkingItem.id " +
            "where ((etfu.user.id = :userId or r.employeeTrackingForm.user.id = :userId or odwi.organizationalDepartment.id in :organizationalDepartmentIds) or (odwi.organizationalDepartment.id not in :organizationalDepartmentIds and etfu.user.head.id = :userId)) and " +
            "((:#{#approvedByMe==null}=true and r.submitter.head.id <> :userId) or (:#{#approvedByMe} = true and r.submitter.head.id = :userId) or (:#{#approvedByMe} = false and r.submitter.head.id <> :userId)) and " +
            "(:#{#reportFilter.getapproverFirstNameLastNameToLower()==null}=true or lower(concat(concat(person.firstName,' '),person.lastName)) like %:#{#reportFilter.getapproverFirstNameLastNameToLower()}%) and " +
            "(:#{#reportFilter.submitterEmail==null}=true or lower(r.submitter.email) like %:#{#reportFilter.submitterEmail}%) and " +
            "(:#{#reportFilter.startDate==null}=true or CAST(r.submissionDate as LocalDate) = :#{#reportFilter.startDate}) and " +
            "(:#{#reportFilter.submitterFirstNameLastName==null}=true or lower(concat(concat(r.submitter.person.firstName,' '),r.submitter.person.lastName)) like %:#{#reportFilter.getsubmitterFirstNameLastNameToLower()}%) and " +
            "(:#{#reportFilter.description==null}=true or lower(r.description) like %:#{#reportFilter.getDescriptionToLower()}%) order by date(r.submissionDate) desc, r.submitter.email desc")
    Page<ReportFrontHelper> findAllForHeads(Long userId, ReportFilter reportFilter, List<Long> organizationalDepartmentIds, Pageable pageable, Boolean approvedByMe);


    @Query("SELECT distinct count(r) from Report r left join User u on r.submitter.id=u.id where (:#{#reportFilter.employeeTrackingFormId==null} = true " +
            "or :#{#reportFilter.employeeTrackingFormId} = r.employeeTrackingForm.id) and ((:#{#approvedByMe==null}=true and r.submitter.head.id <> :id) or (:#{#approvedByMe} = true and r.submitter.head.id = :id) or (:#{#approvedByMe} = false and r.submitter.head.id <> :id)) and "+
            "(:#{#reportFilter.submitterEmail==null}=true or lower(u.email) like %:#{#reportFilter.getSubmitterEmail()}%) and "+
            "(:#{#reportFilter.description==null}=true or lower(r.description) like %:#{#reportFilter.getDescription()}%) and "+
            "(CAST(r.submissionDate as LocalDate) = :#{#reportFilter.startDate}) and "+
            "(:#{#reportFilter.approverEmail==null}=true or lower(u.email) like %:#{#reportFilter.getApproverEmail()}%)")
    int getAllCustomCount(ReportFilter reportFilter, Boolean approvedByMe, Long id);

    @Query("SELECT distinct count(r) from Report r left join User u on r.submitter.id=u.id join EmployeeTrackingForm etf on r.employeeTrackingForm.id = etf.id join EmployeeTrackingFormUser etfu on etfu.employeeTrackingForm.id = etf.id where etfu.user.id = :id and (:#{#reportFilter.employeeTrackingFormId==null} = true " +
            "or :#{#reportFilter.employeeTrackingFormId} = r.employeeTrackingForm.id) and "+
            "(:#{#reportFilter.submitterEmail==null}=true or lower(u.email) like %:#{#reportFilter.getSubmitterEmail()}%) and "+
            "(:#{#reportFilter.description==null}=true or lower(r.description) like %:#{#reportFilter.getDescription()}%) and "+
            "(CAST(r.submissionDate as LocalDate) = :#{#reportFilter.startDate}) and "+
            "(:#{#reportFilter.approverEmail==null}=true or lower(u.email) like %:#{#reportFilter.getApproverEmail()}%)")
    int getAllCustomForUserCount(ReportFilter reportFilter, Long id);

    @Query(value = "select results.* from (select distinct r.* " +
            "from Report r " +
            "join EmployeeTrackingForm etf on r.employeeTrackingForm.id = etf.id " +
            "join EmployeeTrackingFormUser etfu on etfu.employeeTrackingForm.id = etf.id " +
            "join OrganizationalDepartmentWorkingItem odwi on odwi.id = etf.organizationalDepartmentWorkingItem.id" +
            "where (etfu.user.id = :userId or etf.user.id = :userId or odwi.organizationalDepartment.id in :organizationalDepartmentIds and " +
            "(r.submission_date >= :#{#reportFilter.startDate} and r.submission_date <= :#{#reportFilter.endDate}))) as results order by results.id desc", nativeQuery = true, countQuery ="select count(un.*) from (select distinct r.* " +
            "from Report r " +
            "join EmployeeTrackingForm etf on r.employeeTrackingForm.id = etf.id " +
            "join EmployeeTrackingFormUser etfu on etfu.employeeTrackingForm.id = etf.id " +
            "join OrganizationalDepartmentWorkingItem odwi on odwi.id = etf.organizationalDepartmentWorkingItem.id " +
            "where (etfu.user.id = :userId or etf.user.id = :userId or odwi.organizationalDepartment.id in :organizationalDepartmentIds and " +
            "(r.submission_date >= :#{#reportFilter.startDate} and r.submission_date <= :#{#reportFilter.endDate}))) as un")
    Page<Report> findAllByOrganizationalDepartmentIdPaginated(Long userId, ReportFilter reportFilter, List<Long> organizationalDepartmentIds, Pageable pageable);

    @Query("select count(distinct r) from Report r " +
            "join EmployeeTrackingFormUser etfu on r.employeeTrackingForm.id = etfu.employeeTrackingForm.id " +
            "join OrganizationalDepartmentWorkingItem odwi on odwi.id = r.employeeTrackingForm.organizationalDepartmentWorkingItem.id " +
            "where ((etfu.user.id = :userId or r.employeeTrackingForm.user.id = :userId or odwi.organizationalDepartment.id in :organizationalDepartmentIds) or (odwi.organizationalDepartment.id not in :organizationalDepartmentIds and etfu.user.head.id = :userId)) and " +
            "((:#{#approvedByMe==null}=true and r.submitter.head.id <> :userId) or (:#{#approvedByMe} = true and r.submitter.head.id = :userId) or (:#{#approvedByMe} = false and r.submitter.head.id <> :userId)) and " +
            "(CAST(r.submissionDate as LocalDate) = :#{#reportFilter.startDate})")
    int getAllCustomForHeadCount(ReportFilter reportFilter, Long userId, List<Long> organizationalDepartmentIds, Boolean approvedByMe);

    @Query("select count(distinct r) from Report r " +
            "join EmployeeTrackingFormUser etfu on r.employeeTrackingForm.id = etfu.employeeTrackingForm.id " +
            "join OrganizationalDepartmentWorkingItem odwi on odwi.id = r.employeeTrackingForm.organizationalDepartmentWorkingItem.id " +
            "where ((etfu.user.id = :userId or r.employeeTrackingForm.user.id = :userId or odwi.organizationalDepartment.id in :organizationalDepartmentIds) or (odwi.organizationalDepartment.id not in :organizationalDepartmentIds and etfu.user.head.id = :userId)) and r.submitter.head.id = :userId and (r.accepted is null or r.accepted = false)")
    int getAllCustomForHeadNotAcceptedCount(Long userId, List<Long> organizationalDepartmentIds);

    @Query(value = "select count(un.*) from ( " +
            "select distinct r.* " +
            "from Report r " +
            "join EmployeeTrackingForm etf on r.employeeTrackingForm.id = etf.id " +
            "join EmployeeTrackingFormUser etfu on etfu.employeeTrackingForm.id = etf.id " +
            "join OrganizationalDepartmentWorkingItem odwi on odwi.id = etf.organizationalDepartmentWorkingItem.id " +
            "join OrganizationalDepartmentUser odu on odu.organizationalDepartment.id = odwi.organizationalDepartment.id " +
            "where (etfu.user.id <> :userId and odwi.mm_organizationalDepartment.id in :organizationalDepartmentIds) " +
            "and odu.isHead = true) as un where un.accepted = false or un.accepted is null", nativeQuery = true)
    int getAllCustomForSectorHeadNotAcceptedCount(Long userId, List<Long> organizationalDepartmentIds);


    @Query("select r from Report r where (:#{#reportFilter.submitterFirstNameLastName==null}=true or lower(concat(concat(r.submitter.person.firstName,' '),r.submitter.person.lastName)) like %:#{#reportFilter.getsubmitterFirstNameLastNameToLower()}%) and " +
            "(:#{#reportFilter.startDate==null}=true or CAST(r.submissionDate as LocalDate) = :#{#reportFilter.startDate})")
    List<Report> getReportsWithNameAndDate(ReportFilter reportFilter);

    @Query("select r from Report r " +
            "join EmployeeTrackingFormUser etfu on r.employeeTrackingForm.id = etfu.employeeTrackingForm.id " +
            "join OrganizationalDepartmentWorkingItem odwi on odwi.id = r.employeeTrackingForm.organizationalDepartmentWorkingItem.id " +
            "where ((etfu.user.id = :userId or r.employeeTrackingForm.user.id = :userId or odwi.organizationalDepartment.id in :organizationalDepartmentIds) or (odwi.organizationalDepartment.id not in :organizationalDepartmentIds and etfu.user.head.id = :userId)) and r.submitter.head.id = :userId and (r.accepted is null or r.accepted = false)")
    List<Report> getAllNotAcceptedReportsForHead(Long userId, List<Long> organizationalDepartmentIds);


}