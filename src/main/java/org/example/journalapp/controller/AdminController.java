package org.example.journalapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.journalapp.dto.CreateUserRequestDto;
import org.example.journalapp.dto.CreateUserResponseDto;
import org.example.journalapp.entity.User;
import org.example.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.FOUND);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<CreateUserResponseDto> createAdmin(@RequestBody CreateUserRequestDto createAdminRequest){
        return new ResponseEntity<>(userService.saveAdmin(createAdminRequest),HttpStatus.CREATED);
    }
}
