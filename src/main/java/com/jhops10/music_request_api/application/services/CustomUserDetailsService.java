package com.jhops10.music_request_api.application.services;

import com.jhops10.music_request_api.domain.model.User;
import com.jhops10.music_request_api.domain.ports.outgoing.UserRepositoryPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    public CustomUserDetailsService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPassword(),
                new ArrayList<>()
        );

    }
}
