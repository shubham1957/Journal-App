package org.example.journalapp.service;

import lombok.RequiredArgsConstructor;
import org.example.journalapp.dto.CreateUserRequestDto;
import org.example.journalapp.dto.CreateUserResponseDto;
import org.example.journalapp.dto.UpdateUserRequestDto;
import org.example.journalapp.dto.UpdateUserResponseDto;
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

    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto){

        User user = User.builder()
                .userName(createUserRequestDto.getUserName())
                .password(passwordEncoder.encode(createUserRequestDto.getPassword()))
                .roles(List.of("USER"))
                .build();

        User newUser = userRepository.save(user);

        return CreateUserResponseDto.builder()
                .id(newUser.getId())
                .userName(newUser.getUserName())
                .password(newUser.getPassword())
                .journals(newUser.getJournals())
                .roles(newUser.getRoles())
                .build();
    }

    public UpdateUserResponseDto updateUser(UpdateUserRequestDto updateUserRequestDto){
        User existingUser = authenticatedUserProvider.getAuthenticatedUser(); // Always authenticated

        if (updateUserRequestDto.getUserName() != null && !updateUserRequestDto.getUserName().isBlank()) {
            existingUser.setUserName(updateUserRequestDto.getUserName());
        }

        if (updateUserRequestDto.getPassword() != null && !updateUserRequestDto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updateUserRequestDto.getPassword()));
        }

        userRepository.save(existingUser);

        return UpdateUserResponseDto.builder()
                .userName(existingUser.getUserName())
                .password(existingUser.getPassword())
                .build();
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

    public void saveUser(User newUser){
        userRepository.save(newUser);
    }

    public CreateUserResponseDto saveAdmin(CreateUserRequestDto createAdminRequestDto){
        User user = User.builder()
                .userName(createAdminRequestDto.getUserName())
                .password(passwordEncoder.encode(createAdminRequestDto.getPassword()))
                .roles(List.of("USER", "ADMIN"))
                .build();

        User newAdmin = userRepository.save(user);

        return CreateUserResponseDto.builder()
                .id(newAdmin.getId())
                .userName(newAdmin.getUserName())
                .password(newAdmin.getPassword())
                .build();

    }
}
