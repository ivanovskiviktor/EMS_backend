package graduatethesis.performancemonitoringsystem.service.implementation.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.NoteHelper;
import graduatethesis.performancemonitoringsystem.model.organization.Note;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentUser;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.organization.NoteRepository;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.PrivilegeService;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.NoteService;
import graduatethesis.performancemonitoringsystem.model.exceptions.DescriptionIsEmptyOrNotFoundException;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentUserService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final PrivilegeService privilegeService;
    private final UserService userService;
    private final LoggedUserService loggedUserService;
    private final OrganizationalDepartmentUserService organizationalDepartmentUserService;

    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository, PrivilegeService privilegeService, UserService userService, LoggedUserService loggedUserService, OrganizationalDepartmentUserService organizationalDepartmentUserService) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.privilegeService = privilegeService;
        this.userService = userService;
        this.loggedUserService = loggedUserService;
        this.organizationalDepartmentUserService = organizationalDepartmentUserService;
    }

    @Override
    public Note save(NoteHelper noteHelper) {

        Note note = new Note();

        //postavuvanje na opis za zabeleskata
        if(noteHelper.getDescription()!=null && !noteHelper.getDescription().isEmpty()){
            note.setDescription(noteHelper.getDescription());
        }else {
            throw new DescriptionIsEmptyOrNotFoundException();
        }

        //postavuvanje na logiraniot korisnik kako kreator na zabeleskata
        User loggedUser = this.loggedUserService.getLoggedUser();
        note.setUser(loggedUser);


        //postavuvanje na poceten i kraen datum na samata zabeleska
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();

        if(now.getDayOfWeek() == DayOfWeek.MONDAY){
            OffsetDateTime odtStart = now.atZone(zoneId).toOffsetDateTime();
            note.setStartWeekDate(odtStart);

            LocalDateTime friday = now.plusDays(4);
            OffsetDateTime odtEnd = friday.atZone(zoneId).toOffsetDateTime();
            note.setEndWeekDate(odtEnd);
        }
        else if(now.getDayOfWeek() == DayOfWeek.TUESDAY){
            LocalDateTime monday = now.minusDays(1);
            OffsetDateTime odtStart = monday.atZone(zoneId).toOffsetDateTime();
            note.setStartWeekDate(odtStart);

            LocalDateTime friday = now.plusDays(3);
            OffsetDateTime odtEnd = friday.atZone(zoneId).toOffsetDateTime();
            note.setEndWeekDate(odtEnd);
        }
        else if(now.getDayOfWeek() == DayOfWeek.WEDNESDAY){
            LocalDateTime monday = now.minusDays(2);
            OffsetDateTime odtStart = monday.atZone(zoneId).toOffsetDateTime();
            note.setStartWeekDate(odtStart);

            LocalDateTime friday = now.plusDays(2);
            OffsetDateTime odtEnd = friday.atZone(zoneId).toOffsetDateTime();
            note.setEndWeekDate(odtEnd);
        }
        else if(now.getDayOfWeek() == DayOfWeek.THURSDAY){
            LocalDateTime monday = now.minusDays(3);
            OffsetDateTime odtStart = monday.atZone(zoneId).toOffsetDateTime();
            note.setStartWeekDate(odtStart);

            LocalDateTime friday = now.plusDays(1);
            OffsetDateTime odtEnd = friday.atZone(zoneId).toOffsetDateTime();
            note.setEndWeekDate(odtEnd);
        }
        else if(now.getDayOfWeek() == DayOfWeek.FRIDAY){
            LocalDateTime monday = now.minusDays(4);
            OffsetDateTime odtStart = monday.atZone(zoneId).toOffsetDateTime();
            note.setStartWeekDate(odtStart);

            OffsetDateTime odtEnd = now.atZone(zoneId).toOffsetDateTime();
            note.setEndWeekDate(odtEnd);
        }
        else if(now.getDayOfWeek() == DayOfWeek.SATURDAY){
            LocalDateTime monday = now.minusDays(5);
            OffsetDateTime odtStart = monday.atZone(zoneId).toOffsetDateTime();
            note.setStartWeekDate(odtStart);

            LocalDateTime friday = now.minusDays(1);
            OffsetDateTime odtEnd = friday.atZone(zoneId).toOffsetDateTime();
            note.setEndWeekDate(odtEnd);
        }else {
            LocalDateTime monday = now.minusDays(6);
            OffsetDateTime odtStart = monday.atZone(zoneId).toOffsetDateTime();
            note.setStartWeekDate(odtStart);

            LocalDateTime friday = now.minusDays(2);
            OffsetDateTime odtEnd = friday.atZone(zoneId).toOffsetDateTime();
            note.setEndWeekDate(odtEnd);
        }


        return this.noteRepository.save(note);
    }


    @Override
    public List<Note> findAll() {
        return this.noteRepository.findAll();
    }

    @Override
    public List<Note> findAllWithPaginationForEmployee(Long id) {
        return this.noteRepository.findAllWithPaginationForEmployee(id);
    }

    @Override
    public List<Note> findAllWithPaginationForHead() {
        List<Note> notes = new ArrayList<>();
        List<OrganizationalDepartmentUser> organizationalUDepartmentsWhereUserIsHead = this.organizationalDepartmentUserService.findAllWhereUserIsHead(loggedUserService.getLoggedUser().getId());
        List<User> calculatedUsers = new ArrayList<>();
        for (OrganizationalDepartmentUser o: organizationalUDepartmentsWhereUserIsHead) {

            List<User> users = this.userService.findAllInOrganizationalDepartment(o.getOrganizationalDepartment().getId());

            for(User u : users){
                if(!calculatedUsers.contains(u)){
                    notes.addAll(u.getNotes());
                    calculatedUsers.add(u);
                }
            }
        }
        return notes;
    }

}

