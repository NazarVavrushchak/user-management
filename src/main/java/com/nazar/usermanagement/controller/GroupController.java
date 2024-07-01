package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.entity.Group;
import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody String name) {
        Group group = groupService.createGroup(name);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/addUser/{userId}")
    public ResponseEntity<Group> addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        Group group = groupService.addUserToGroup(groupId, userId);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/addRole/{roleId}")
    public ResponseEntity<Group> addRoleToGroup(@PathVariable Long groupId, @PathVariable Long roleId) {
        Group group = groupService.addRoleToGroup(groupId, roleId);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/byRole/{role}")
    public ResponseEntity<List<Group>> getGroupsByRole(@PathVariable Role.RoleType role) {
        List<Group> groups = groupService.getGroupsByRole(role);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/createWithSpecification")
    public Group createGroupWithSpecification(@RequestBody String name, @PathVariable Role.RoleType roleRole) {
        Group group = groupService.createGroupWithSpecification(name, Collections.singleton(roleRole));
        return group;
    }

    @PostMapping("/{groupId}/assignUsersByAgeAndRole")
    public ResponseEntity<Group> assignUserToGroupByAgeAndRole(
            @PathVariable Long groupId,
            @RequestParam int minAge,
            @RequestParam int maxAge,
            @RequestBody Set<Role.RoleType> roleTypes) {
        Group group = groupService.assignUserToGroupByAgeAndRole(groupId, minAge, maxAge, roleTypes);
        return ResponseEntity.ok(group);
    }
}