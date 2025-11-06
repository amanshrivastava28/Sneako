package org.genc.sneakoapp.usermanagementservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.genc.sneakoapp.usermanagementservice.dto.AuthRequestDTO;
import org.genc.sneakoapp.usermanagementservice.dto.AuthResponseDTO;
import org.genc.sneakoapp.usermanagementservice.dto.CustomUserDetails;
import org.genc.sneakoapp.usermanagementservice.dto.ErrorResponse;
import org.genc.sneakoapp.usermanagementservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/user-service"})
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;
    @Value("${spring.application.name}")
    private String appName;
    @Value("${server.port}")
    private String serverPort;

   @PostMapping("/login")
    @Operation(security = {@SecurityRequirement(name = "")})
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request , HttpServletRequest servletRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

           /*  --- OPTIMIZATION START: Access authenticated principal ---
             Retrieve the authenticated object from the SecurityContextHolder.
             The Principal object contains the UserDetails that was just loaded by the DaoAuthenticationProvider.
            CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            --- OPTIMIZATION END ---
            */
            //If  your business requires  additional  Details
           CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getUsername());
            if (!user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(auth -> auth.equalsIgnoreCase(request.getRole()))) {
                log.warn("Role mismatch for user {}: expected {}, got {}", user.getUsername(), user.getAuthorities(), request.getRole());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.of(
                                HttpStatus.UNAUTHORIZED,
                                "Role mismatch: unauthorized access",
                                servletRequest.getRequestURI()
                        ));
            }
            String token = jwtUtil.generateToken(user);

            AuthResponseDTO response = new AuthResponseDTO(token,user.getId(), user.getUsername(), user.getAddress(),
                  request.getRole(),  user.getEmail(), user.getPhone(),instanceId + appName);

            log.info("Access from {} on port {} (instance: {})", appName, serverPort, instanceId);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid username or password",
                            servletRequest.getRequestURI()
                    ));
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of(
                            HttpStatus.UNAUTHORIZED,
                            "Authentication error: " + e.getMessage(),
                            servletRequest.getRequestURI()
                    ));
        }
    }
}
