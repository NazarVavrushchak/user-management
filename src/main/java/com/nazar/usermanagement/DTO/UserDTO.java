package com.nazar.usermanagement.DTO;

import com.nazar.usermanagement.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private Role role;
    private Long roleId;
}