package com.nazar.usermanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nazar.usermanagement.DTO.UserDTO;
import com.nazar.usermanagement.repository.PersonRepository;
import com.nazar.usermanagement.utils.PersonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nazar.usermanagement.entity.Person;


@Service
@Transactional
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //отримати персона(за id наприклад)
    @Transactional(readOnly = true)//тільки читаються данні
    public Optional<UserDTO> getPerson(Long id) {
        return personRepository.findById(id).map(PersonMapper::toDTO);
    }

    public void createPerson(UserDTO userDTO) {
        Person person = PersonMapper.toEntity(userDTO);
        personRepository.save(person);
    }

    //видалити (за id наприклад)
    public void removePerson(Long id) {
        personRepository.deleteById(id);
    }

    //getAll
    @Transactional(readOnly = true)
    public List<UserDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(PersonMapper::toDTO).collect(Collectors.toList());
    }

    //оновлення юзера
    public Optional<UserDTO> updatePerson(Long id, UserDTO userDTO) {
        return personRepository.findById(id).map(person -> {
            person.setEmail(userDTO.getEmail());
            person.setFirstName(userDTO.getFirstName());
            person.setLastName(userDTO.getLastName());
            person.setAge(userDTO.getAge());
            personRepository.save(person);
            return PersonMapper.toDTO(person);
        });
    }
}
