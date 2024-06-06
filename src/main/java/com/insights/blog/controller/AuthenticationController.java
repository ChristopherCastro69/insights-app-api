package com.insights.blog.controller;

import com.insights.blog.exception.UserAlreadyExistsException;
import com.insights.blog.model.User;
import com.insights.blog.payload.LoginRequestDTO;
import com.insights.blog.payload.AuthenticationResponseDTO;
import com.insights.blog.payload.RegisterRequestDTO;
import com.insights.blog.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            return ResponseEntity.ok(authenticationService.register(registerRequestDTO));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Return 409 Conflict with empty body
        } catch (Exception e) {
            // Handle other unexpected exceptions (log, return 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticateRequest(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDTO));
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authenticationService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/all")
    public ResponseEntity<Void> deleteAllUsers() {
        authenticationService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        try {
            authenticationService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
