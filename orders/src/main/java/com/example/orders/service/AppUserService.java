package com.example.orders.service;

import com.example.orders.model.AppUser;

public interface AppUserService {

    AppUser findAppUserByUsername(String username);

}
