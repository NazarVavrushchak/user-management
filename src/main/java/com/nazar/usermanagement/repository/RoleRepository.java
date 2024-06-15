package com.nazar.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nazar.usermanagement.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.RoleType role);
}