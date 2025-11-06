package org.genc.sneakoapp.usermanagementservice.repo;


import org.genc.sneakoapp.usermanagementservice.entity.Role;
import org.genc.sneakoapp.usermanagementservice.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
