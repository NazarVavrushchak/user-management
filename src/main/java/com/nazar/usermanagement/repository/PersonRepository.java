package com.nazar.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nazar.usermanagement.entity.Person;

public interface PersonRepository extends JpaRepository<Person , Long>{//імплементуємо багато логіки

}
