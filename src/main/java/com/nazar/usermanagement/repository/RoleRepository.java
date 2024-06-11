package com.nazar.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nazar.usermanagement.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}