package com.demo.daniel.repository;

import com.demo.daniel.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, String> {
    Optional<Person> findByName(String username);

    Optional<Person> findByEmail(String email);
}
