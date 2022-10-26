package graduatethesis.performancemonitoringsystem.service.implementation;

import graduatethesis.performancemonitoringsystem.model.exceptions.PersonNotFoundException;
import graduatethesis.performancemonitoringsystem.model.exceptions.UserNotFoundException;
import graduatethesis.performancemonitoringsystem.model.helpers.PersonHelper;
import graduatethesis.performancemonitoringsystem.model.users.Person;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.users.PersonRepository;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.PersonService;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PersonServiceImpl(PersonRepository personRepository, UserRepository userRepository, UserService userService) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void deletePerson(Long id) {
        this.personRepository.deleteById(id);
    }

    @Override
    public List<Person> findAll() {
        return this.personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(Long id) {
        return this.personRepository.findById(id);
    }

    @Override
    @Transactional
    public Person create(String firstName, String lastName) {

        Person person = new Person(firstName,lastName);
        return this.personRepository.save(person);
    }

    @Override
    public Person create(PersonHelper personHelper) {
        User user = this.userRepository.findById(personHelper.getUserId()).orElseThrow(()-> new UserNotFoundException(personHelper.getUserId()));
        Person person = new Person();
        person.setFirstName(personHelper.getFirstName());
        person.setLastName(personHelper.getLastName());

        person = personRepository.save(person);

        user.setPerson(person);
        userService.save(user);
        return person;
    }

    @Override
    public Person update(Long id, String firstName, String lastName,User user) {
        Person person = this.personRepository.findById(id).orElseThrow(()-> new PersonNotFoundException(id));
        person.setFirstName(firstName);
        person.setLastName(lastName);

        return this.personRepository.save(person);
    }

    @Override
    public Optional<Person> update(PersonHelper personHelper) {
        Person person = this.personRepository.findById(personHelper.getId()).orElseThrow(()-> new PersonNotFoundException(personHelper.getId()));
        User user = this.userRepository.findById(personHelper.getUserId()).orElseThrow(()-> new UserNotFoundException(personHelper.getUserId()));

        person.setId(personHelper.getId());
        person.setFirstName(personHelper.getFirstName());
        person.setLastName(personHelper.getLastName());

        return Optional.of(this.personRepository.save(person));
    }

    @Override
    public Person updatePerson(Person person, Person newPersonData) {
        if (newPersonData.getFirstName() != null) {
            person.setFirstName(newPersonData.getFirstName());
        }

        if (newPersonData.getLastName() != null) {
            person.setLastName(newPersonData.getLastName());
        }

        return create(newPersonData.getFirstName(),newPersonData.getLastName());
    }

    @Override
    public Person createPerson(Person person) {
        return this.personRepository.save(person);
    }

}
