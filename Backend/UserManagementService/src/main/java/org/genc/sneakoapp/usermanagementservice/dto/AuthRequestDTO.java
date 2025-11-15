package org.genc.sneakoapp.usermanagementservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @NotNull(message = "enter the username")
    private String username;

    @NotNull(message = "enter the password")
    private String password;

    @NotNull(message = "enter the role type")
    private String role;

}
