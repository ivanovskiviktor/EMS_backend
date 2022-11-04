package graduatethesis.performancemonitoringsystem.service.interfaces.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.NoteHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Note;

import java.util.List;

public interface NoteService {

    Note save(NoteHelper noteHelper);

    List<Note> findAll();

    List<Note> findAllWithPaginationForEmployee(Long id);

    List<Note> findAllWithPaginationForHead();

}
