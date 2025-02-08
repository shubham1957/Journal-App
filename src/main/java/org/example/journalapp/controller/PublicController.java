package org.example.journalapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.journalapp.entity.User;
import org.example.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser){
        return new ResponseEntity<>(userService.createUser(newUser), HttpStatus.CREATED);
    }

}
