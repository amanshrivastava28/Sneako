package org.genc.sneakoapp.usermanagementservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.genc.sneakoapp.usermanagementservice.enums.RoleType;


@Data
@Builder
public class RoleRequestDTO {
    @NotNull(message = "Role name is required")
    private RoleType name;
    private String description;
}