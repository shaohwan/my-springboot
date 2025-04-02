package com.demo.daniel.service;

import com.demo.daniel.entity.Person;
import com.demo.daniel.model.PersonRequest;
import com.demo.daniel.repository.PersonRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Page<Person> getAllPersons(Pageable pageable, String name, String email, String phone) {
        Specification<Person> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(cb.like(root.get("email"), "%" + email + "%"));
            }
            if (phone != null && !phone.isEmpty()) {
                predicates.add(cb.like(root.get("phone"), "%" + phone + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return personRepository.findAll(spec, pageable);
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
