package org.genc.sneakoapp.usermanagementservice.service.api;



import org.genc.sneakoapp.usermanagementservice.dto.RoleRequestDTO;
import org.genc.sneakoapp.usermanagementservice.dto.RoleResponseDTO;
import org.genc.sneakoapp.usermanagementservice.entity.Role;
import org.genc.sneakoapp.usermanagementservice.enums.RoleType;

import java.util.List;

public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO request);
    RoleResponseDTO getRoleById(Long id);
    List<RoleResponseDTO> getAllRoles();
    RoleResponseDTO updateRole(Long id, RoleRequestDTO request);
    void deleteRole(Long id);
    Role getRoleByName(RoleType roleType);
    public Role seedRoleData(RoleRequestDTO request);
}
