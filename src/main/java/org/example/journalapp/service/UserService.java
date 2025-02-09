package org.example.journalapp.service;

import lombok.RequiredArgsConstructor;
import org.example.journalapp.entity.User;
import org.example.journalapp.repository.UserRepository;
import org.example.journalapp.security.AuthenticatedUserProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(User newUser){
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(List.of("USER"));
        return userRepository.save(newUser);
    }

    public User updateUser(User updateUserRequestBody){
        User existingUser = authenticatedUserProvider.getAuthenticatedUser();
        if(existingUser!=null){
            existingUser.setUserName(updateUserRequestBody.getUserName());
            existingUser.setPassword(passwordEncoder.encode(updateUserRequestBody.getPassword()));
            userRepository.save(existingUser);
        }
        return existingUser;
    }

    public Boolean deleteUser() throws AccessDeniedException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        String userName = authentication.getName();
        return userRepository.deleteByUserName(userName);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
