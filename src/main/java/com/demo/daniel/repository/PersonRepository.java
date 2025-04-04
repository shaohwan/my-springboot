package com.demo.daniel.repository;

import com.demo.daniel.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {
    Optional<Person> findByName(String username);

    Optional<Person> findByEmail(String email);
}
