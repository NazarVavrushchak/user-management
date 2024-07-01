package com.nazar.usermanagement.repository;

import com.nazar.usermanagement.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group , Long> {
    Optional<Group> findByGroupName(String groupName);
}
