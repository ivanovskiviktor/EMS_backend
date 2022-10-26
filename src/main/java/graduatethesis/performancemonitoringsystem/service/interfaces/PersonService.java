package graduatethesis.performancemonitoringsystem.service.interfaces;


import graduatethesis.performancemonitoringsystem.model.helpers.PersonHelper;
import graduatethesis.performancemonitoringsystem.model.users.Person;
import graduatethesis.performancemonitoringsystem.model.users.User;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    void deletePerson(Long id);

    List<Person> findAll();

    Optional<Person> findById(Long id);

    Person create(String firstName, String lastName);

    Person create(PersonHelper personHelper);

    Person update(Long id, String firstName, String lastName, User user);

    Optional<Person> update(PersonHelper personHelper);

    Person updatePerson(Person person, Person newPersonData);

    Person createPerson(Person person);


}
