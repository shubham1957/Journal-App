package org.example.journalapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.journalapp.dto.CreateUserRequestDto;
import org.example.journalapp.dto.CreateUserResponseDto;
import org.example.journalapp.dto.UserLoginDto;
import org.example.journalapp.service.UserDetailServiceImpl;
import org.example.journalapp.service.UserServiceImpl;
import org.example.journalapp.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public API", description = "Endpoints for public access")
public class PublicController {

    private final UserServiceImpl userService;
    private  final AuthenticationManager authenticationManager;
    private  final UserDetailServiceImpl userDetailService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CreateUserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        return new ResponseEntity<>(userService.createUser(createUserRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token in an HttpOnly cookie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, token stored in cookie"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, incorrect credentials")
    })
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(), userLoginDto.getPassword())
            );

            UserDetails userDetails = userDetailService.loadUserByUsername(userLoginDto.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            // Set JWT as HttpOnly Cookie
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful, token stored in cookie");
        } catch (AuthenticationException e) {
            log.error("Exception occurred while creating authentication token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logs out the user by clearing the JWT cookie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }
}
