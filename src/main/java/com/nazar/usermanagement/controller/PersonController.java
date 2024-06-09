package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.DTO.UserDTO;
import com.nazar.usermanagement.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController//обробляє http запити і поверта дані у json or xml
@RequestMapping("/persons")//path
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping//позначає метод контролера, який повинен реагувати на POST-запити на вказаний шлях
    public void createPerson(@RequestBody UserDTO userDTO) {
        //save person to the db
        personService.createPerson(userDTO);
    }

    @GetMapping("/{id}")
    public Optional<UserDTO> getPerson(@PathVariable Long id) {
        return personService.getPerson(id);
    }

    @DeleteMapping("/{id}")
    public void removePerson(@PathVariable Long id) {
        personService.removePerson(id);
    }

    @GetMapping
    public List<UserDTO> getAllPersons() {
        return personService.getAllPersons();
    }

    @PutMapping("/{id}")
    public Optional<UserDTO> updatePerson(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return personService.updatePerson(id, userDTO);
    }
}