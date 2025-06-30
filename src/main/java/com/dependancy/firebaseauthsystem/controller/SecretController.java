package com.dependancy.firebaseauthsystem.controller;

import com.dependancy.firebaseauthsystem.dto.PostSecret;
import com.dependancy.firebaseauthsystem.dto.SecretDto;
import com.dependancy.firebaseauthsystem.entities.Secret;
import com.dependancy.firebaseauthsystem.mapper.SecretsMapper;
import com.dependancy.firebaseauthsystem.repositories.SecretRepository;
import com.dependancy.firebaseauthsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/secrets")
@AllArgsConstructor
public class SecretController {

    private final SecretRepository secretRepository;
    private final SecretsMapper secretsMapper;
    private final UserRepository userRepository;
    //get secrets
    //post secrets
    //delete secrets

    @GetMapping
    public ResponseEntity<?> getSecrets() {

        //getName() returns the email of the user
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Secret> secrets = secretRepository.findAllByOwnedByEmail(userEmail);

        var response = new ArrayList<SecretDto>();
        secrets.forEach(secret -> {
            response.add(secretsMapper.toSecretDto(secret));
        });
        return ResponseEntity.ok(response);
    }

    //not using 'validation' @valid
    //must be handled by front-end

    @PostMapping
    public ResponseEntity<?> postSecret(@RequestBody PostSecret request) {

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(userEmail).orElse(null);

        var existingSecret = secretRepository.findAllByOwnedByEmail(userEmail).stream()
                .filter(secret -> secret.getContent().equals(request.getContent()))
                .findFirst().orElse(null);

        if (existingSecret != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var secret = new Secret();
        secret.setContent(request.getContent());
        secret.setOwnedBy(user);

        secretRepository.save(secret);
        return ResponseEntity.ok(secretsMapper.toSecretDto(secret));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSecret(@PathVariable Long id) {

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        var secret = secretRepository.findById(id).orElse(null);
        if (secret == null || !secret.getOwnedBy().getEmail().equals(userEmail)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        secretRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
