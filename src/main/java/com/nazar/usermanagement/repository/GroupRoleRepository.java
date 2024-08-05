package com.nazar.usermanagement.repository;

import com.nazar.usermanagement.entity.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRoleRepository extends JpaRepository<GroupRole , Long> {
    Optional<GroupRole> findByName(String groupRoleName);
}
