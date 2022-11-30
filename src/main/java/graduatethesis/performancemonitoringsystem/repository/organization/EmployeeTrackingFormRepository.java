package graduatethesis.performancemonitoringsystem.repository.organization;

import graduatethesis.performancemonitoringsystem.model.filters.EmployeeTrackingFormFilter;
import graduatethesis.performancemonitoringsystem.model.helpers.DateHelper;
import graduatethesis.performancemonitoringsystem.model.organization.EmployeeTrackingForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Repository
public interface EmployeeTrackingFormRepository extends JpaRepository<EmployeeTrackingForm, Long> {

    @Query("select distinct etf from EmployeeTrackingForm etf join User u on etf.user.id= u.id where (:#{#employeeTrackingFormFilter.workingItemId} is null or :#{#employeeTrackingFormFilter.workingItemId} = etf.organizationalDepartmentWorkingItem.workingItem.id) and" +
            "(:#{#employeeTrackingFormFilter.getFirstName() == null} = true or lower(u.person.firstName) like %:#{#employeeTrackingFormFilter.getFirstName()}%) and" +
            "(:#{#employeeTrackingFormFilter.getLastName() == null} = true or lower(u.person.lastName) like %:#{#employeeTrackingFormFilter.getLastName()}%) and" +
            "(:#{#employeeTrackingFormFilter.startDate == null} = true or :#{#employeeTrackingFormFilter.startDate} = etf.taskStartDate) and" +
            "(:#{#employeeTrackingFormFilter.endDate == null} = true or :#{#employeeTrackingFormFilter.endDate} = etf.taskEndDate) and" +
            "(:#{#employeeTrackingFormFilter.getDescription() == null} = true or lower(etf.description) like %:#{#employeeTrackingFormFilter.getDescription()}%) and" +
            "(:#{#employeeTrackingFormFilter.getTitle() == null} = true or lower(etf.title) like %:#{#employeeTrackingFormFilter.getTitle()}%) and" +
            "(:#{#employeeTrackingFormFilter.valueId} is null or :#{#employeeTrackingFormFilter.valueId} = etf.value) and" +
            "(:#{#timeTrackingFormFilter.organizationalDepartmentId} is null or :#{#employeeTrackingFormFilter.organizationalDepartmentId} = etf.organizationalDepartmentWorkingItem.organizationalDepartment.id) and"+
            "(:#{#timeTrackingFormFilter.statusId} is null or :#{#employeeTrackingFormFilter.statusId} = etf.status.id) order by etf.dateModified")
    List<EmployeeTrackingForm> findAllCustom(@Param("employeeTrackingFormFilter") EmployeeTrackingFormFilter employeeTrackingFormFilter);

    @Query("select etf from EmployeeTrackingForm etf join EmployeeTrackingFormUser etfu on etf.id = etfu.employeeTrackingForm.id where etfu.user.id =:id and (:#{#employeeTrackingFormFilter.workingItemId} is null or :#{#employeeTrackingFormFilter.workingItemId} = etf.organizationalDepartmentWorkingItem.workingItem.id) and" +
            "(:#{#employeeTrackingFormFilter.getFirstName() == null} = true or lower(etf.user.person.firstName) like %:#{#employeeTrackingFormFilter.getFirstName()}%) and" +
            "(:#{#employeeTrackingFormFilter.getLastName() == null} = true or lower(etf.user.person.lastName) like %:#{#employeeTrackingFormFilter.getLastName()}%) and" +
            "(:#{#employeeTrackingFormFilter.startDate == null} = true or :#{#employeeTrackingFormFilter.startDate} = etf.taskStartDate) and" +
            "(:#{#employeeTrackingFormFilter.endDate == null} = true or :#{#employeeTrackingFormFilter.endDate} = etf.taskEndDate) and" +
            "(:#{#employeeTrackingFormFilter.getDescription() == null} = true or lower(etf.description) like %:#{#employeeTrackingFormFilter.getDescription()}%) and" +
            "(:#{#employeeTrackingFormFilter.getTitle() == null} = true or lower(etf.title) like %:#{#employeeTrackingFormFilter.getTitle()}%) and" +
            "(:#{#employeeTrackingFormFilter.valueId} is null or :#{#employeeTrackingFormFilter.valueId} = etf.value) and" +
            "(:#{#employeeTrackingFormFilter.organizationalDepartmentId} is null or :#{#employeeTrackingFormFilter.organizationalDepartmentId} = etf.organizationalDepartmentWorkingItem.organizationalDepartment.id) and" +
            "(:#{#employeeTrackingFormFilter.statusId} is null or :#{#employeeTrackingFormFilter.statusId} = etf.status.id) order by etf.dateModified")
    List<EmployeeTrackingForm> findAllByUser(@Param("employeeTrackingFormFilter") EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id);

    @Query("select etf from EmployeeTrackingForm etf order by etf.organizationalDepartmentWorkingItem.workingItem.name, etf.status.name, etf.organizationalDepartmentWorkingItem.organizationalDepartment.name")
    List<EmployeeTrackingForm> findAllOrderedByOrganizationalDepartment();

    @Query("select etf from EmployeeTrackingForm etf order by etf.organizationalDepartmentWorkingItem.workingItem.name, etf.status.name, etf.user.person.firstName, etf.user.person.lastName")
    List<EmployeeTrackingForm> findAllOrderedByEmployee();

    @Query("select etf from EmployeeTrackingForm etf where (:#{#dateHelper.startDate == null} = true or :#{#dateHelper.startDate} <= etf.taskEndDate) and" +
            "(:#{#dateHelper.endDate == null} = true or :#{#dateHelper.endDate} >= etf.taskEndDate) order by etf.user.id, etf.organizationalDepartmentWorkingItem.workingItem.name, etf.status.name")
    List<EmployeeTrackingForm> findAllByTime(@Param("dateHelper") DateHelper dateHelper);

    @Query("select etf from EmployeeTrackingForm etf join OrganizationalDepartmentWorkingItem odwi on etf.organizationalDepartmentWorkingItem.id = odwi.id where odwi.organizationalDepartment.id = :id and (:#{#employeeTrackingFormFilter.workingItemId} is null or :#{#employeeTrackingFormFilter.workingItemId} = etf.organizationalDepartmentWorkingItem.workingItem.id) and" +
            "(:#{#employeeTrackingFormFilter.getFirstName() == null} = true or lower(etf.user.person.firstName) like %:#{#employeeTrackingFormFilter.getFirstName()}%) and" +
            "(:#{#employeeTrackingFormFilter.getLastName() == null} = true or lower(etf.user.person.lastName) like %:#{#employeeTrackingFormFilter.getLastName()}%) and" +
            "(:#{#employeeTrackingFormFilter.startDate == null} = true or :#{#employeeTrackingFormFilter.startDate} = etf.taskStartDate) and" +
            "(:#{#employeeTrackingFormFilter.endDate == null} = true or :#{#employeeTrackingFormFilter.endDate} = etf.taskEndDate) and" +
            "(:#{#employeeTrackingFormFilter.getDescription() == null} = true or lower(etf.description) like %:#{#employeeTrackingFormFilter.getDescription()}%) and" +
            "(:#{#employeeTrackingFormFilter.getTitle() == null} = true or lower(etf.title) like %:#{#employeeTrackingFormFilter.getTitle()}%) and" +
            "(:#{#employeeTrackingFormFilter.valueId} is null or :#{#employeeTrackingFormFilter.valueId} = etf.value) and" +
            "(:#{#employeeTrackingFormFilter.organizationalDepartmentId} is null or :#{#employeeTrackingFormFilter.organizationalDepartmentId} = etf.organizationalDepartmentWorkingItem.organizationalDepartment.id) and" +
            "(:#{#employeeTrackingFormFilter.statusId} is null or :#{#employeeTrackingFormFilter.statusId} = etf.status.id) order by etf.dateModified")
    List<EmployeeTrackingForm> findAllByOrganizationalDepartmentId(@Param("employeeTrackingFormFilter") EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id);

    @Query("select etf from EmployeeTrackingForm etf where etf.user.id = :id and (:#{#employeeTrackingFormFilter.workingItemId} is null or :#{#employeeTrackingFormFilter.workingItemId} = etf.organizationalDepartmentWorkingItem.workingItem.id) and" +
            "(:#{#employeeTrackingFormFilter.getFirstName() == null} = true or lower(etf.user.person.firstName) like %:#{#employeeTrackingFormFilter.getFirstName()}%) and" +
            "(:#{#employeeTrackingFormFilter.getLastName() == null} = true or lower(etf.user.person.lastName) like %:#{#employeeTrackingFormFilter.getLastName()}%) and" +
            "(:#{#employeeTrackingFormFilter.startDate == null} = true or :#{#employeeTrackingFormFilter.startDate} = etf.taskStartDate) and" +
            "(:#{#employeeTrackingFormFilter.endDate == null} = true or :#{#employeeTrackingFormFilter.endDate} = etf.taskEndDate) and" +
            "(:#{#employeeTrackingFormFilter.getDescription() == null} = true or lower(etf.description) like %:#{#employeeTrackingFormFilter.getDescription()}%) and" +
            "(:#{#employeeTrackingFormFilter.getTitle() == null} = true or lower(etf.title) like %:#{#employeeTrackingFormFilter.getTitle()}%) and" +
            "(:#{#employeeTrackingFormFilter.valueId} is null or :#{#employeeTrackingFormFilter.valueId} = etf.value) and" +
            "(:#{#employeeTrackingFormFilter.organizationalDepartmentId} is null or :#{#employeeTrackingFormFilter.organizationalDepartmentId} = etf.organizationalDepartmentWorkingItem.organizationalDepartment.id) and"+
            "(:#{#employeeTrackingFormFilter.statusId} is null or :#{#employeeTrackingFormFilter.statusId} = etf.status.id) order by etf.dateModified")
    List<EmployeeTrackingForm> findAllCreatedByUser(@Param("employeeTrackingFormFilter") EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id);

    @Query("select etf from EmployeeTrackingForm etf join EmployeeTrackingFormUser etfu on etf.id = etfu.employeeTrackingForm.id where etfu.user.id = :userId and etf.organizationalDepartmentWorkingItem.organizationalDepartment.id not in :organizationalDepartmentIds")
    List<EmployeeTrackingForm> findAllWhichAreNotInHeadOrganizationalDepartment(Long userId, List<Long> organizationalDepartmentIds);

    @Query("SELECT distinct etf from EmployeeTrackingForm etf join User u on etf.user.id= u.id where (:#{#employeeTrackingFormFilter.workingItemId} is null or :#{#employeeTrackingFormFilter.workingItemId} = etf.organizationalDepartmentWorkingItem.workingItem.id) and" +
            "(:#{#employeeTrackingFormFilter.submitterFirstNameLastName==null}=true or lower(concat(concat(u.person.firstName,' '),u.person.lastName)) like %:#{#employeeTrackingFormFilter.getsubmitterFirstNameLastNameToLower()}%) and" +
             "(:#{#employeeTrackingFormFilter.startDate == null} = true or :#{#employeeTrackingFormFilter.startDate} = etf.taskStartDate) and" +
            //"(:#{#employeeTrackingFormFilter.startDate == null} = true or CAST(etf.taskStartDate as LocalDate) = :#{#employeeTrackingFormFilter.startDate}) and" +
            "(:#{#employeeTrackingFormFilter.getDescription() == null} = true or lower(etf.description) like %:#{#employeeTrackingFormFilter.getDescription()}%) and" +
            "(:#{#employeeTrackingFormFilter.getTitle() == null} = true or lower(etf.title) like %:#{#employeeTrackingFormFilter.getTitle()}%) and" +
            "(:#{#employeeTrackingFormFilter.valueId} is null or :#{#employeeTrackingFormFilter.valueId} = etf.value) and" +
            "(:#{#employeeTrackingFormFilter.organizationalDepartmentId} is null or :#{#employeeTrackingFormFilter.organizationalDepartmentId} = etf.organizationalDepartmentWorkingItem.organizationalDepartment.id) and"+
            "(etf.status.name = :status) and " +
            "(:#{#employeeTrackingFormFilter.statusId} is null or :#{#employeeTrackingFormFilter.statusId} = etf.status.id) order by etf.dateModified desc")
    Page<EmployeeTrackingForm> findAllCustomPaginated(String status, @Param("employeeTrackingFormFilter") EmployeeTrackingFormFilter employeeTrackingFormFilter, Pageable pageable);

    @Query("select etf from EmployeeTrackingForm etf join EmployeeTrackingFormUser etfu on etf.id = etfu.employeeTrackingForm.id where etfu.user.id =:id and (:#{#employeeTrackingFormFilter.workingItemId} is null or :#{#employeeTrackingFormFilter.workingItemId} = etf.organizationalDepartmentWorkingItem.workingItem.id) and" +
            "(:#{#employeeTrackingFormFilter.submitterFirstNameLastName==null}=true or lower(concat(concat(etf.user.person.firstName,' '),etf.user.person.lastName)) like %:#{#employeeTrackingFormFilter.getsubmitterFirstNameLastNameToLower()}%) and" +
            "(:#{#employeeTrackingFormFilter.startDate == null} = true or :#{#employeeTrackingFormFilter.startDate} = etf.taskStartDate) and" +
            "(:#{#employeeTrackingFormFilter.endDate == null} = true or :#{#employeeTrackingFormFilter.endDate} = etf.taskEndDate) and" +
            "(:#{#employeeTrackingFormFilter.getDescription() == null} = true or lower(etf.description) like %:#{#employeeTrackingFormFilter.getDescription()}%) and" +
            "(:#{#employeeTrackingFormFilter.getTitle() == null} = true or lower(etf.title) like %:#{#employeeTrackingFormFilter.getTitle()}%) and" +
            "(:#{#employeeTrackingFormFilter.valueId} is null or :#{#employeeTrackingFormFilter.valueId} = etf.value) and" +
            "(:#{#employeeTrackingFormFilter.organizationalDepartmentId} is null or :#{#employeeTrackingFormFilter.organizationalDepartmentId} = etf.organizationalDepartmentWorkingItem.organizationalDepartment.id) and"+
            "(etf.status.name = :status) and " +
            "(:#{#employeeTrackingFormFilter.statusId} is null or :#{#employeeTrackingFormFilter.statusId} = etf.status.id) order by etf.dateModified desc")
    Page<EmployeeTrackingForm> findAllByUserPaginated(String status, @Param("employeeTrackingFormFilter") EmployeeTrackingFormFilter employeeTrackingFormFilter, Long id, Pageable pageable);

    @Query("select distinct etf from EmployeeTrackingForm etf left join OrganizationalDepartmentWorkingItem odwi on etf.organizationalDepartmentWorkingItem.id = odwi.id " +
            "left join EmployeeTrackingFormUser etfu on etf.id = etfu.employeeTrackingForm.id where (odwi.organizationalDepartment.id in :organizationalDepartmentIds or etf.user.id = :userId or etfu.user.id = :userId) and " +
            "(:#{#employeeTrackingFormFilter.workingItemId} is null or :#{#employeeTrackingFormFilter.workingItemId} = etf.organizationalDepartmentWorkingItem.workingItem.id) and" +
            "(:#{#employeeTrackingFormFilter.submitterFirstNameLastName==null}=true or lower(concat(concat(etf.user.person.firstName,' '),etf.user.person.lastName)) like %:#{#employeeTrackingFormFilter.getsubmitterFirstNameLastNameToLower()}%) and" +
            "(:#{#employeeTrackingFormFilter.startDate == null} = true or :#{#employeeTrackingFormFilter.startDate} = etf.taskStartDate) and" +
            "(:#{#employeeTrackingFormFilter.endDate == null} = true or :#{#employeeTrackingFormFilter.endDate} = etf.taskEndDate) and" +
            "(:#{#employeeTrackingFormFilter.getDescription() == null} = true or lower(etf.description) like %:#{#employeeTrackingFormFilter.getDescription()}%) and" +
            "(:#{#employeeTrackingFormFilter.getTitle() == null} = true or lower(etf.title) like %:#{#employeeTrackingFormFilter.getTitle()}%) and" +
            "(:#{#employeeTrackingFormFilter.valueId} is null or :#{#employeeTrackingFormFilter.valueId} = etf.value) and" +
            "(:#{#employeeTrackingFormFilter.organizationalDepartmentId} is null or :#{#employeeTrackingFormFilter.organizationalDepartmentId} = etf.organizationalDepartmentWorkingItem.organizationalDepartment.id) and"+
            "(etf.status.name = :status) and " +
            "(:#{#employeeTrackingFormFilter.statusId} is null or :#{#employeeTrackingFormFilter.statusId} = etf.status.id) order by etf.dateModified desc")
    Page<EmployeeTrackingForm> findAllByOrganizationalDepartmentIdPaginated(String status, Long userId, EmployeeTrackingFormFilter employeeTrackingFormFilter, List<Long> organizationalDepartmentIds, Pageable pageable);

    @Query("select etf from EmployeeTrackingForm etf where etf.id=:id and etf.status.name=:name")
    EmployeeTrackingForm findTimeTrackingFormByIdAndStatus(Long id, String name);

    @Query("select etf from EmployeeTrackingForm etf where etf.user.id = :id")
    List<EmployeeTrackingForm> getAllByUserIdCustom(Long id);

}