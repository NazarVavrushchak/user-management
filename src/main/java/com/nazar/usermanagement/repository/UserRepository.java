package com.nazar.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nazar.usermanagement.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
}
