package com.dependancy.firebaseauthsystem.repositories;

import com.dependancy.firebaseauthsystem.entities.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretRepository extends JpaRepository<Secret, Long> {

    List<Secret> findAllByOwnedByEmail(String email);
}