package graduatethesis.performancemonitoringsystem.repository.organization;


import graduatethesis.performancemonitoringsystem.model.organization.WorkingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingItemRepository extends JpaRepository<WorkingItem, Long> {

    Optional<WorkingItem> findByName(String name);

    @Query("select distinct wi from WorkingItem wi join OrganizationalDepartmentWorkingItem odwi on wi.id = odwi.workingItem.id where odwi.organizationalDepartment.id = :id " +
            "order by wi.label")
    List<WorkingItem> findAllByOrganizationalUnit(Long id);
}
