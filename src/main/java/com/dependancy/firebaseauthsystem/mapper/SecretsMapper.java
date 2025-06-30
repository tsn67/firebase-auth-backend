package com.dependancy.firebaseauthsystem.mapper;

import com.dependancy.firebaseauthsystem.dto.SecretDto;
import com.dependancy.firebaseauthsystem.entities.Secret;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecretsMapper {

    SecretDto toSecretDto(Secret secret);
}
