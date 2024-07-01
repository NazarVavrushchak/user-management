package com.nazar.usermanagement.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDTO {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
