package org.example.journalapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.journalapp.dto.UpdateUserRequestDto;
import org.example.journalapp.dto.UpdateUserResponseDto;
import org.example.journalapp.entity.User;
import org.example.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser(),HttpStatus.OK);
    }

    @GetMapping("{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName){
        return new ResponseEntity<>(userService.findByUserName(userName),HttpStatus.FOUND);
    }

    @PutMapping
    public ResponseEntity<UpdateUserResponseDto> updateUser(@RequestBody UpdateUserRequestDto updateUserRequestDto){
        return new ResponseEntity<>(userService.updateUser(updateUserRequestDto),HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser() throws AccessDeniedException {
        return new ResponseEntity<>(userService.deleteUser(),HttpStatus.OK);
    }
}
