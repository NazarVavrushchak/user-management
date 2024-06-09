package com.nazar.usermanagement.DTO;

import com.nazar.usermanagement.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    public void setRole(Role.RoleType role) {
        this.role = Role.RoleType.valueOf(role.name());
    }

    public enum RoleType {
        STUDENT,
        WORKER,
        RETIRED,
        PUPIL,
    }

    private Long roleId;
    private Role.RoleType role;
}
