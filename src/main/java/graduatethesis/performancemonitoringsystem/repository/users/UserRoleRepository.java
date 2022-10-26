package graduatethesis.performancemonitoringsystem.repository.users;

import graduatethesis.performancemonitoringsystem.model.users.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Transactional
    @Modifying
    @Query("delete from UserRole ur where ur.user.id = :userId")
    void deleteRolesOfUser(Long userId);

    void deleteUserRoleByUserId(Long userId);

}
