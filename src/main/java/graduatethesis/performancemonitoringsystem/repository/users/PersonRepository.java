package graduatethesis.performancemonitoringsystem.repository.users;

import graduatethesis.performancemonitoringsystem.model.users.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Transactional
    @Modifying
    @Query("delete from Person u where u.id = :id")
    void deleteById(Long id);
}
