package com.nazar.usermanagement.utils;

import com.nazar.usermanagement.DTO.UserDTO;
import com.nazar.usermanagement.entity.Person;

public class PersonMapper {
    public static UserDTO toDTO(Person person) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(person.getId());
        userDTO.setFirstName(person.getFirstName());
        userDTO.setLastName(person.getLastName());
        userDTO.setEmail(person.getEmail());
        userDTO.setAge(person.getAge());
        return userDTO;
    }

    public static Person toEntity(UserDTO userDTO) {
        Person person = new Person();
        person.setId(userDTO.getId());
        person.setFirstName(userDTO.getFirstName());
        person.setLastName(userDTO.getLastName());
        person.setEmail(userDTO.getEmail());
        person.setAge(userDTO.getAge());
        return person;
    }
}