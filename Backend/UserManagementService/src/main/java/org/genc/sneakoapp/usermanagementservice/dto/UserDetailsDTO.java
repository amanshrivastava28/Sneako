package org.genc.sneakoapp.usermanagementservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsDTO {
    private Long id;
    private String username;
    private String email;
}
