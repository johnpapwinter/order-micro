package com.example.orders.security;

import com.example.orders.model.AppUser;
import com.example.orders.service.AppUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserService appUserService;

    public UserDetailsServiceImpl(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public AppPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserService.findAppUserByUsername(username);

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(appUser.getRole().name());
        List<GrantedAuthority> authorities = new ArrayList<>(Collections.singleton(simpleGrantedAuthority));

        return AppPrincipal.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(authorities)
                .build();
    }

}
