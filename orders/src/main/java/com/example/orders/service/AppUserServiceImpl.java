package com.example.orders.service;

import com.example.orders.exception.EntityNotFoundException;
import com.example.orders.exception.ErrorMessages;
import com.example.orders.model.AppUser;
import com.example.orders.repository.AppUserRepository;
import com.example.orders.security.dto.RegistrationDTO;
import com.example.orders.utils.mappers.AppUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    public AppUserServiceImpl(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser findAppUserByUsername(String username) {
        return appUserRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.APP_USER_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public Long registerAppUser(RegistrationDTO dto) {
        AppUser appUser = appUserMapper.toAppUser(dto);

        return appUserRepository.save(appUser).getId();
    }

}
