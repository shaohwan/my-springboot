package com.demo.daniel.service;

import com.demo.daniel.entity.Person;
import com.demo.daniel.model.PersonRequest;
import com.demo.daniel.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person createPerson(PersonRequest request) {
        Person person = Person.builder().name(request.getName()).password(request.getPassword()).email(request.getEmail()).phone(request.getPhone()).build();
        return personRepository.save(person);
    }

    public Optional<Person> getPersonById(String id) {
        return personRepository.findById(id);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> updatePerson(String id, String name, String password, String email) {
        return personRepository.findById(id).map(person -> {
            person.setName(name);
            person.setPassword(password);
            person.setEmail(email);
            return personRepository.save(person);
        });
    }

    public Optional<Person> deletePerson(String id) {
        return personRepository.findById(id).map(person -> {
            person.setIsDeleted(true);
            return personRepository.save(person);
        });
    }
}
