package graduatethesis.performancemonitoringsystem.repository.organization;


import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationalDepartmentRepository extends JpaRepository<OrganizationalDepartment, Long> {

    @Query("select od from OrganizationalDepartment od join OrganizationalDepartmentUser oud on od.id = oud.organizationalDepartment.id and oud.urUserId.id=:id order by od.label asc")
    List<OrganizationalDepartment> findAllForUser(Long id);

    @Query("select distinct od from OrganizationalDepartment od join OrganizationalDepartmentWorkingItem odwi on od.id= odwi.organizationalDepartment.id where odwi.workingItem.id = :workingItemId")
    List<OrganizationalDepartment> findAllByWorkingItem(Long workingItemId);

    @Query("select ou from OrganizationalDepartment ou where ou.code=:code")
    OrganizationalDepartment findByCode(String code);
}
