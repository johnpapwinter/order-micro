package com.example.orders.service;

import com.example.orders.model.AppUser;
import com.example.orders.security.dto.RegistrationDTO;

public interface AppUserService {

    AppUser findAppUserByUsername(String username);

    Long registerAppUser(RegistrationDTO dto);

}
