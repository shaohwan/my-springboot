package com.demo.daniel.controller;

import com.demo.daniel.entity.Person;
import com.demo.daniel.model.PersonRequest;
import com.demo.daniel.model.Response;
import com.demo.daniel.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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
    public Response<Page<Person>> getAllPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        Page<Person> persons = personService.getAllPersons(PageRequest.of(page, size), name, email, phone);
        return Response.success(persons);
    }
}
