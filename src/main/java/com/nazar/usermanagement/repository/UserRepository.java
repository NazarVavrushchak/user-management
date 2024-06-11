package com.nazar.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nazar.usermanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{//імплементуємо багато логіки

}
