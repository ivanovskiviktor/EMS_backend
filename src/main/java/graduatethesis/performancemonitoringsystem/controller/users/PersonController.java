package graduatethesis.performancemonitoringsystem.controller.users;

import graduatethesis.performancemonitoringsystem.model.helpers.PersonHelper;
import graduatethesis.performancemonitoringsystem.model.users.Person;
import graduatethesis.performancemonitoringsystem.service.interfaces.PersonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/person")
@PreAuthorize("isAuthenticated()")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }



    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @GetMapping("/get/{id}")
    public Optional<Person> findById(@PathVariable Long id){
        return this.personService.findById(id);
    }


    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL')")
    @PostMapping("/create")
    public Person save(@RequestBody PersonHelper personHelper){
        return this.personService.create(personHelper);
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PutMapping("/update")
    public Optional<Person> update(@RequestBody PersonHelper personHelper) {
        return this.personService.update(personHelper);
    }

}
