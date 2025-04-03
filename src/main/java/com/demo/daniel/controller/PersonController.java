package com.demo.daniel.controller;

import com.demo.daniel.entity.Person;
import com.demo.daniel.model.GenericResponse;
import com.demo.daniel.model.PersonCreateDTO;
import com.demo.daniel.model.PersonVO;
import com.demo.daniel.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private ModelMapper modelMapper;

    // Create a new person
    @PostMapping
    public GenericResponse<Person> createPerson(@RequestBody PersonCreateDTO request) {
        Person person = personService.createPerson(request);
        return GenericResponse.success(person);
    }

    @GetMapping
    public GenericResponse<Page<PersonVO>> getAllPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        Page<Person> persons = personService.getAllPersons(PageRequest.of(page, size), name, email, phone);
        List<PersonVO> personVOs = persons.getContent().stream()
                .map(person -> modelMapper.map(person, PersonVO.class))
                .collect(Collectors.toList());
        return GenericResponse.success(new PageImpl<>(
                personVOs,
                persons.getPageable(),
                persons.getTotalElements()
        ));
    }
}
