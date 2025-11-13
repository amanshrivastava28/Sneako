package org.genc.sneakoapp.usermanagementservice.controller;


import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.usermanagementservice.dto.UserDTO;
import org.genc.sneakoapp.usermanagementservice.dto.UserDetailsDTO;
import org.genc.sneakoapp.usermanagementservice.service.api.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-service/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsService userDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsersById(@PathVariable Long id) {
        UserDTO user = userDetailsService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id ,@RequestBody UserDTO userdto) {
        UserDTO user = userDetailsService.updateUser(id,userdto);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userDetailsService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totalusers")
    public ResponseEntity<Long> totalUsers() {
        Long totalUsers = userDetailsService.totalUsers();
        return new ResponseEntity<>(totalUsers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        List<UserDetailsDTO> users = userDetailsService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/admin/{id}")
    public ResponseEntity<UserDetailsDTO> findUserById(@PathVariable Long id) {
        UserDetailsDTO user = userDetailsService.findUserById(id);
        return ResponseEntity.ok(user);
    }



}
