package org.example.journalapp.service;

import org.example.journalapp.dto.CreateUserRequestDto;
import org.example.journalapp.dto.CreateUserResponseDto;
import org.example.journalapp.dto.UpdateUserRequestDto;
import org.example.journalapp.dto.UpdateUserResponseDto;
import org.example.journalapp.entity.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);
    UpdateUserResponseDto updateUser(UpdateUserRequestDto updateUserRequestDto);
    Boolean deleteUser() throws AccessDeniedException;
    List<User> getAllUser();
    User findByUserName(String userName);
    void saveUser(User newUser);
    CreateUserResponseDto saveAdmin(CreateUserRequestDto createAdminRequestDto);

}
