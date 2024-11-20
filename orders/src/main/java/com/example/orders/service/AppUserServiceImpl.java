package com.example.orders.service;

import com.example.orders.exception.EntityNotFoundException;
import com.example.orders.exception.ErrorMessages;
import com.example.orders.model.AppUser;
import com.example.orders.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser findAppUserByUsername(String username) {
        return appUserRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.APP_USER_NOT_FOUND)
        );
    }

}
