package org.genc.sneakoapp.usermanagementservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.genc.sneakoapp.usermanagementservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Long id;
    private final Set<GrantedAuthority> authorities;
    private final String email;
    private final String address;
    private String phone;

    public CustomUserDetails(User user) {
        this.authorities =  user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                .collect(Collectors.toSet());
        this.id= user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phone = user.getPhone();
    }
    public Set<String> getRoles() {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
