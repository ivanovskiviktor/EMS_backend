package graduatethesis.performancemonitoringsystem.repository.organization;

import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentWorkingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationalDepartmentWorkingItemRepository extends JpaRepository<OrganizationalDepartmentWorkingItem, Long> {

    @Query("select odwi from OrganizationalDepartmentWorkingItem odwi where odwi.organizationalDepartment.id=:organizationaDepartmentId and odwi.workingItem.id=:workingItemId")
    OrganizationalDepartmentWorkingItem findByOrganizationalDepartmentIdAndAndWorkingItemId(Long organizationaDepartmentId,Long workingItemId);
}
