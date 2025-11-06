package org.genc.sneakoapp.usermanagementservice.service.api;


import org.genc.sneakoapp.usermanagementservice.dto.UserRegistrationRequestDTO;
import org.genc.sneakoapp.usermanagementservice.dto.UserRegistrationResponseDTO;

public interface UserMgmtService {

    public UserRegistrationResponseDTO registerNewUser(UserRegistrationRequestDTO userReqDTO);

    public  boolean isNewUser(String userName);
}
