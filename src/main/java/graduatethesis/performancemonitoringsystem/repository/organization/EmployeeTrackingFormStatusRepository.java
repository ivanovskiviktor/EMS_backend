package graduatethesis.performancemonitoringsystem.repository.organization;

import graduatethesis.performancemonitoringsystem.model.organization.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeTrackingFormStatusRepository extends JpaRepository<Status, Long> {

    @Query("select s from Status s where s.name = :name")
    Status findByName(String name);

    @Query("select s from Status s order by s.id asc")
    List<Status> findAllCustom();
}
