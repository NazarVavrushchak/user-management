package com.nazar.usermanagement.entity;

import java.util.HashSet;
import java.util.Set;

import com.nazar.usermanagement.DTO.RoleDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    public void setRole(RoleDTO.RoleType role) {
        this.role = RoleType.valueOf(role.name());
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public enum RoleType {
        STUDENT,
        WORKER,
        RETIRED,
        PUPIL,
        USER //default
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<Group> groups = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.setRole(this);
    }
}
