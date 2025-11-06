package org.genc.sneakoapp.usermanagementservice.service.api;


import org.genc.sneakoapp.usermanagementservice.dto.UserDTO;
import org.genc.sneakoapp.usermanagementservice.dto.UserDetailsDTO;

import java.util.List;

public interface UserDetailsService {
   public List<UserDetailsDTO> getAllUsers();
    public void deleteUserById(Long id);
    public  UserDetailsDTO findById(Long id);
    public  Long TotalUsers();
    public UserDTO findUserById(Long id);
    public UserDTO updateUser(Long id, UserDTO userDTO);


}
