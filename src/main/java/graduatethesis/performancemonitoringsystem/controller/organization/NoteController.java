package graduatethesis.performancemonitoringsystem.controller.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.NoteHelper;
import graduatethesis.performancemonitoringsystem.model.helpers.NoteHelperFront;
import graduatethesis.performancemonitoringsystem.model.organization.Note;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.PrivilegeService;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private final LoggedUserService loggedUserService;
    private final PrivilegeService privilegeService;


    public NoteController(NoteService noteService, UserService userService, LoggedUserService loggedUserService, PrivilegeService privilegeService) {
        this.noteService = noteService;
        this.userService = userService;
        this.loggedUserService = loggedUserService;
        this.privilegeService = privilegeService;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value = "/create")
    public Note save(@RequestBody NoteHelper noteHelper) {
        return this.noteService.save(noteHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @GetMapping("/all/{page}/{size}")
    public Page<NoteHelperFront> findAllWithPaginationForUsers(@PathVariable int page, @PathVariable int size) {
        List<NoteHelperFront> list = new ArrayList<>();

        User user = this.loggedUserService.getLoggedUser();
        if (privilegeService.loggedUserHasAnyPrivilege("ACCESS_ALL")) {
            list = this.noteService.findAll().stream().map(Note::getAsNoteHelperFront).collect(Collectors.toList());
        }
        else if (privilegeService.loggedUserHasAnyPrivilege("READ_USER_DATA")) {
            list = this.noteService.findAllWithPaginationForEmployee(user.getId()).stream().map(Note::getAsNoteHelperFront).collect(Collectors.toList());
        }
        else {
            list = this.noteService.findAllWithPaginationForHead().stream().map(Note::getAsNoteHelperFront).collect(Collectors.toList());
        }

        list = list.stream().sorted((first, second) -> -first.getId().compareTo(second.getId())).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page,size);
        int startIndex = Math.min((int) pageable.getOffset(), list.size());
        int endIndex = Math.min(startIndex + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startIndex,endIndex),pageable,list.size());
    }

    }
