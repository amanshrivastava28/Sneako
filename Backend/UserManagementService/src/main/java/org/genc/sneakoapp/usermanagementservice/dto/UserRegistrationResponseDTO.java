package org.genc.sneakoapp.usermanagementservice.dto;

import lombok.Builder;
import lombok.Data;
import org.genc.sneakoapp.usermanagementservice.enums.RoleType;

import java.util.Set;

@Data
@Builder
public class UserRegistrationResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String userMessage;
    private Set<RoleType> roles;
}
