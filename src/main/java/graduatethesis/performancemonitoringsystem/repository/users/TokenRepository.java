package graduatethesis.performancemonitoringsystem.repository.users;

import graduatethesis.performancemonitoringsystem.model.users.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select ct from Token ct where ct.token=:token")
    Token findByConfirmationToken(String token);

    @Transactional
    @Modifying
    @Query("delete from Token u where u.id = :id")
    void deleteById(Long id);





}
