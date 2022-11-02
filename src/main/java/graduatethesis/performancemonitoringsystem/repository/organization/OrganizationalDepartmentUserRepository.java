package graduatethesis.performancemonitoringsystem.repository.organization;

import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrganizationalDepartmentUserRepository extends JpaRepository<OrganizationalDepartmentUser, Long> {

    @Query("select odu from OrganizationalDepartmentUser odu join User u on odu.urUserId.id = u.id and u.id=:id")
    List<OrganizationalDepartmentUser> findAllForOneUser(Long id);

    @Transactional
    @Modifying
    @Query("delete from OrganizationalDepartmentUser odu where odu.urUserId.id = :uid and odu.organizationalDepartment.id = :oid")
    void deleteByOrganizationalUnitAndUser(Long oid, Long uid);

    @Query("select odu from OrganizationalDepartmentUser odu where odu.urUserId.id = :userId and odu.isHead = true")
    List<OrganizationalDepartmentUser> findAllWhereUserIsHead(Long userId);

    @Query("select odu from OrganizationalDepartmentUser odu where odu.organizationalDepartment.id = :orgDepartmentId and odu.isHead = true")
    List<OrganizationalDepartmentUser> findAllByOrganizationalUnitId(Long orgDepartmentId);
}
