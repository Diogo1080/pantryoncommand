package com.pantryoncommand.converters;

import com.pantryoncommand.command.auth.PrincipalDto;
import com.pantryoncommand.command.user.CreateUserDto;
import com.pantryoncommand.command.user.UserDetailsDto;
import com.pantryoncommand.persistence.entity.UserEntity;

/**
 * Rent converter
 * Transforms from one data model to another
 */
public class UserConverter {

    /**
     * From {@link CreateUserDto} to {@link UserEntity}
     * @param createUserDto {@link CreateUserDto}
     * @return {@link UserEntity}
     */
    public static UserEntity fromCreateUserDtoToUserEntity(CreateUserDto createUserDto) {
        return UserEntity.builder()
                .userName(createUserDto.getUserName())
                .email(createUserDto.getEmail())
                .encryptedPassword(createUserDto.getPassword())
                .build();
    }

    /**
     * From {@link UserEntity} to {@link UserDetailsDto}
     * @param userEntity {@link UserEntity}
     * @return {@link UserDetailsDto}
     */
    public static UserDetailsDto fromUserEntityToUserDetailsDto(UserEntity userEntity) {
        return UserDetailsDto.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUserName())
                .email(userEntity.getEmail())
                .build();
    }

    /**
     * Convert from {@link UserEntity} to {@link PrincipalDto}
     * @param userEntity
     * @return {@link PrincipalDto}
     */
    public static PrincipalDto fromUserEntityToPrincipalDto(UserEntity userEntity) {
        return PrincipalDto.builder()
                .userId(userEntity.getId())
                .firstName(userEntity.getUserName())
                .userRole(userEntity.getRole())
                .build();
    }
}
