package com.example.orders.utils.mappers;

import com.example.orders.enums.AppUserRole;
import com.example.orders.model.AppUser;
import com.example.orders.security.dto.RegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AppUser toAppUser(RegistrationDTO dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(AppUserRole.ADMIN); // for now, default role
        return user;
    }

}
