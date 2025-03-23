package org.example.journalapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.journalapp.dto.UpdateUserRequestDto;
import org.example.journalapp.dto.UpdateUserResponseDto;
import org.example.journalapp.entity.User;
import org.example.journalapp.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "APIs for managing users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/{userName}")
    @Operation(summary = "Get user by username", description = "Fetches user details by their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName) {
        Optional<User> user = Optional.ofNullable(userService.findByUserName(userName));

        return user.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PutMapping
    @Operation(summary = "Update user details", description = "Updates user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<UpdateUserResponseDto> updateUser(@RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(updateUserRequestDto));
    }

    @DeleteMapping
    @Operation(summary = "Delete current user", description = "Deletes the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Boolean> deleteUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        boolean deleted = userService.deleteUser();
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok(true);
    }
}
