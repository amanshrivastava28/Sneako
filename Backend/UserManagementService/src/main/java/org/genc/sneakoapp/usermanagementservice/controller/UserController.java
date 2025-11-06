package org.genc.sneakoapp.usermanagementservice.controller;


import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.usermanagementservice.dto.UserDTO;
import org.genc.sneakoapp.usermanagementservice.service.api.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-service/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsService userDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsersById(@PathVariable Long id) {
        UserDTO user = userDetailsService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id ,@RequestBody UserDTO userdto) {
        UserDTO user = userDetailsService.updateUser(id,userdto);
        return ResponseEntity.ok(user);
    }




}
