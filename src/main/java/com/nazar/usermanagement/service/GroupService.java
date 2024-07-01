package com.nazar.usermanagement.service;

import com.nazar.usermanagement.entity.Group;
import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.repository.GroupRepository;
import com.nazar.usermanagement.repository.RoleRepository;
import com.nazar.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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

    public Group addRoleToGroup(Long groupId, Long roleId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        group.getRoles().add(role);
        role.getGroups().add(group);

        return group;
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
