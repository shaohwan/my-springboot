package com.demo.daniel.controller;

import com.demo.daniel.entity.Person;
import com.demo.daniel.model.PersonRequest;
import com.demo.daniel.model.Response;
import com.demo.daniel.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    // Create a new person
    @PostMapping
    public Response<Person> createPerson(@RequestBody PersonRequest request) {
        Person person = personService.createPerson(request);
        return Response.success(person);
    }

    @GetMapping
    public Response<List<Person>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return Response.success(persons);
    }
}
