package org.genc.sneakoapp.usermanagementservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.genc.sneakoapp.usermanagementservice.dto.RoleRequestDTO;
import org.genc.sneakoapp.usermanagementservice.dto.UserRegistrationRequestDTO;
import org.genc.sneakoapp.usermanagementservice.entity.Role;
import org.genc.sneakoapp.usermanagementservice.enums.RoleType;
import org.genc.sneakoapp.usermanagementservice.service.api.RoleService;
import org.genc.sneakoapp.usermanagementservice.service.api.UserMgmtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.genc.sneakoapp.usermanagementservice.enums.RoleType.ROLE_ADMIN;


@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataLoader implements CommandLineRunner {

    private final UserMgmtService userMgmtService;
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        // Create or fetch roles
        Role adminRole = roleService.seedRoleData(RoleRequestDTO.builder()
                .name(ROLE_ADMIN)
                .description("Admin User Role")
                .build());

        Role userRole = roleService.seedRoleData(RoleRequestDTO.builder()
                .name(RoleType.ROLE_CUSTOMER)
                .description("Standard User Role")
                .build());

        // Create admin user
        if (userMgmtService.isNewUser("admin")) {
            UserRegistrationRequestDTO userReqDTO = new UserRegistrationRequestDTO("admin", "admin123",
                    "gencadmin@cognizant.com", RoleType.ROLE_ADMIN, "GENC",
                    "9657932761");
            userMgmtService.registerNewUser(userReqDTO);
        }

        // Create regular user
        if (userMgmtService.isNewUser("user")) {
            UserRegistrationRequestDTO userReqDTO = new UserRegistrationRequestDTO("user", "user123",
                    "gencuser@cognizant.com", RoleType.ROLE_CUSTOMER, "genc",
                    "6657932766");
            userMgmtService.registerNewUser(userReqDTO);
        }
    }
}

