package org.genc.sneakoapp.usermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.genc.sneakoapp.usermanagementservice.enums.RoleType;


@Data
@AllArgsConstructor
public class UserRegistrationRequestDTO {

    private String username;
    private String password;
    private String email;
    private RoleType roleType;
    private String address;
    private String phone;

}
