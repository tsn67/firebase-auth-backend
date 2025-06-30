package com.dependancy.firebaseauthsystem.controller;

import com.dependancy.firebaseauthsystem.dto.ValidateResponse;
import com.dependancy.firebaseauthsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/validate")
    public ResponseEntity<?> validate() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var email = authentication.getName();
        var user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.internalServerError().build();// this is an internal server-error, saved user is not found in the db
        }

        var validateResponse = new ValidateResponse();
        validateResponse.setEmail(email);

        return ResponseEntity.ok(validateResponse);
    }
}
