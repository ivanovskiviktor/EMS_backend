package graduatethesis.performancemonitoringsystem.repository.users;

import graduatethesis.performancemonitoringsystem.model.filters.UserFilter;
import graduatethesis.performancemonitoringsystem.model.users.Token;
import graduatethesis.performancemonitoringsystem.model.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query("select u from User u where u.email=:email")
    User findByEmailCustom(String email);

    User findByTokensContaining(Token token);

    @Query("select u from User u join UserRole ur on u.id=ur.user.id where ur.role.name=:role")
    List<User> getAllByUserRoles(String role);

    @Transactional
    @Modifying
    @Query("delete from User u where u.id = :id")
    void deleteById(Long id);

    @Query("select distinct u from User u join OrganizationalDepartmentUser odu on u.id = odu.urUserId.id and odu.organizationalDepartment.id = :id")
    List<User> findAllInOrganizationalUnit(Long id);


    @Query("select u from User u where u.enabled = true order by u.person.firstName asc")
    List<User> findAllEnabled();

    @Query("select u from User u join UserRole ur on u.id=ur.user.id where ur.role.name <> 'ROLE_EMPLOYEE'")
    List<User> getAllUsersThatAreNotEmployees();

    @Query("select u from User u where (:#{#userFilter.firstName==null}=true or lower(u.person.firstName) like %:#{#userFilter.getFirstName()}%) and "+
            "(:#{#userFilter.lastName==null}=true or lower(u.person.lastName) like %:#{#userFilter.getLastName()}%) and "+
            "(:#{#userFilter.email==null}=true or lower(u.email) like %:#{#userFilter.getEmail()}%) order by u.id desc")
    List<User> findAllFiltered(@Param("userFilter") UserFilter userFilter);

    @Query("select u from User u where u.id = :id")
    User findUserById(Long id);

}
