package graduatethesis.performancemonitoringsystem.repository.organization;

import graduatethesis.performancemonitoringsystem.model.organization.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {

    @Query("select n from Note n where n.user.id = :id")
    List<Note> findAllWithPaginationForEmployee(Long id);

}
