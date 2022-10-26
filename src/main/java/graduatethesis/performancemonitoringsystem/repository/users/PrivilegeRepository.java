package graduatethesis.performancemonitoringsystem.repository.users;


import graduatethesis.performancemonitoringsystem.model.users.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
