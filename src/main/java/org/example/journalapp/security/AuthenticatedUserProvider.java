package org.example.journalapp.security;

import lombok.RequiredArgsConstructor;
import org.example.journalapp.entity.User;
import org.example.journalapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProvider{

    private final UserRepository userRepository;

    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userRepository.findByUserName(userName);
    }
}
