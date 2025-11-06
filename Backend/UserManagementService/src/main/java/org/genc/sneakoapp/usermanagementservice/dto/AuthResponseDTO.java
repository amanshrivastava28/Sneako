package org.genc.sneakoapp.usermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String jwt;
    private Long id;
    private String userName;
    private String address;
    private String role;
    private String email;
    private String phone;
    private String appInstance;

}
