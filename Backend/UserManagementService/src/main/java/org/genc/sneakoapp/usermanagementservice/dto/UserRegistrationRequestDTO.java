package org.genc.sneakoapp.usermanagementservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.genc.sneakoapp.usermanagementservice.enums.RoleType;


@Data
@AllArgsConstructor
public class UserRegistrationRequestDTO {


    @NotNull(message = "Enter the name")
    private String username;

    @NotNull(message = "Enter the password")
    private String password;

    @Email(message = "Enter the valid format of email")
    private String email;

    @NotNull(message = "Enter the role type")
    private RoleType roleType;

    @NotNull(message = "Enter the address")
    private String address;

    @NotNull(message = "Enter the phone number")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    private String phone;

}
