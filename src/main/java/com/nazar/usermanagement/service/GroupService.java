package com.nazar.usermanagement.service;

import com.nazar.usermanagement.entity.Group;
import com.nazar.usermanagement.entity.GroupRole;
import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.repository.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final GroupRoleRepository groupRoleRepository;

    public GroupService(GroupRepository groupRepository, RoleRepository roleRepository
            , UserRepository userRepository, GroupRoleRepository groupRoleRepository) {
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.groupRoleRepository = groupRoleRepository;
    }

    public Group createGroup(String name) {
        if (groupRepository.findByGroupName(name).isPresent()) {
            throw new IllegalStateException("Group with that name is already exists");
        }
        Group group = new Group();
        group.setGroupName(name);
        return groupRepository.save(group);
    }

    public Group addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with that od not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        group.getUsers().add(user);
        user.getGroups().add(group);

        return group;
    }

    public GroupRole addRoleToGroup(Long groupId, Long userId , String groupRoleName) {
        if (!groupRoleName.matches("^[a-zA-Z0-9]{3,}$")) {
            throw new ConstraintViolationException("Group role name must be at least 3 characters long and contain only letters.", null);
        }
        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Role role = roleRepository.findByName(groupRoleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(groupRoleName);
                    return roleRepository.save(newRole);
                });

        GroupRole groupRole = new GroupRole();
        groupRole.setGroup(group);
        groupRole.setUser(user);
        groupRole.setRole(role);
        groupRole.setGroupRole(groupRoleName);

        return groupRoleRepository.save(groupRole);
    }

    public List<Group> getGroupsByRole(Role.RoleType roleType) {
        Role role = roleRepository.findByRole(roleType)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        return new ArrayList<>(role.getGroups());
    }

    public Group createGroupWithSpecification(String name, Set<Role.RoleType> roleTypes) {
        Group group = createGroup(name);
        Set<User> userToAdd = new HashSet<>();
        for (Role.RoleType roleType : roleTypes) {
            Role role = roleRepository.findByRole(roleType)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleType));
            userToAdd.addAll(role.getUsers());
        }

        for (User user : userToAdd) {
            if (group.getUsers().contains(user)) {
                addUserToGroup(group.getId(), user.getId());
            }
        }
        return group;
    }

    public Group assignUserToGroupByAgeAndRole(Long groupId , int minAge , int maxAge , Set<Role.RoleType> roleTypes){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Set<User> userToAdd = userRepository.findAll().stream()
                .filter(user -> user.getAge() >= minAge && user.getAge() <= maxAge)
                .filter(user -> roleTypes.contains(user.getRole().getRole()))
                .collect(Collectors.toSet());

        for(User user : userToAdd){
            if (group.getUsers().contains(user)) {
                addUserToGroup(group.getId(), user.getId());
            }
        }
        return group;
        }
}