package com.nazar.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nazar.usermanagement.entity.Role;


//TODO add two more implementation using JDBC and HIBERNATE
public interface RoleRepository extends JpaRepository<Role, Long> {

}