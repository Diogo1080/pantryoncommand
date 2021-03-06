package com.pantryoncommand.service;


import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.user.CreateUserDto;
import com.pantryoncommand.command.user.UpdateUserDto;
import com.pantryoncommand.command.user.UserDetailsDto;
import com.pantryoncommand.converters.UserConverter;
import com.pantryoncommand.enumerators.UserRole;
import com.pantryoncommand.error.ErrorMessages;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.user.UserAlreadyExistsException;
import com.pantryoncommand.exeption.user.UserNotFoundException;
import com.pantryoncommand.persistence.entity.UserEntity;
import com.pantryoncommand.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link UserService} implementation
 */
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImp.class);

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @see UserService#addNewUser(CreateUserDto, UserRole)
     */
    public UserDetailsDto addNewUser(CreateUserDto userRegistrationDto, UserRole userRole) throws UserAlreadyExistsException, DatabaseCommunicationException {

        // Build UserEntity
        UserEntity userEntity = UserConverter.fromCreateUserDtoToUserEntity(userRegistrationDto);
        userEntity.setRole(userRole);

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        // Set encrypted password
        userEntity.setEncryptedPassword(encryptedPassword);

        // Persist user into database
        try {
            LOGGER.debug("Saving user into database");
            userRepository.save(userEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error(ErrorMessages.USER_ALREADY_EXISTS);
            throw new UserAlreadyExistsException(ErrorMessages.USER_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving user into database - {} - with exception - {}", userEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }

        // Build UserDetailsDto to return to the client
        return UserConverter.fromUserEntityToUserDetailsDto(userEntity);
    }

    /**
     * @see UserService#getUserById(long)
     */
    public UserDetailsDto getUserById(long userId) throws UserNotFoundException {

        // Get user details from database
        LOGGER.debug("Getting user with id {}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.USER_NOT_FOUND);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // Build UserDetailsDto to return to the client
        return UserConverter.fromUserEntityToUserDetailsDto(userEntity);
    }

    /**
     * @see UserService#getUsersList(int, int , UserRole)
     */
    public Paginated<UserDetailsDto> getUsersList(int page, int size, UserRole userRole) {

        // Get all users from database
        LOGGER.debug("Getting all users from database");
        Page<UserEntity> usersList;
        try {
            usersList = userRepository.findAllByRole(
                    userRole.name(),
                    PageRequest.of(page, size, Sort.by("first_name"))
            );
        } catch (Exception e) {
            LOGGER.error("Failed while getting all users from database", e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }


        // Convert list items from UserEntity to UserDetailsDto
        List<UserDetailsDto> usersListResponse = new ArrayList<>();
        for (UserEntity userEntity : usersList.getContent()) {
            usersListResponse.add(UserConverter.fromUserEntityToUserDetailsDto(userEntity));
        }

        //Build paginated
        Paginated<UserDetailsDto> paginated = new Paginated<>(
                usersListResponse,
                usersListResponse.size(),
                page,
                usersList.getTotalPages(),
                usersList.getTotalElements()
        );

        //Return paginated
        return paginated;
    }

    /**
     * @see UserService#deleteUser(long)
     */
    public void deleteUser(long userId) throws UserNotFoundException, DatabaseCommunicationException {

        // Verify if the user exists
        LOGGER.debug("Getting user with id {}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.USER_NOT_FOUND);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // Delete user
        try {
            LOGGER.debug("Deleting user from database");
            userRepository.delete(userEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while deleting user from database - {} - with exception - {}", userEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }
    }

    /**
     * @see UserService#updateUser(long, UpdateUserDto)
     */
    public UserDetailsDto updateUser(long userId, UpdateUserDto updateUserDto) throws UserNotFoundException, DatabaseCommunicationException {

        // Verify if the user exists
        LOGGER.debug("Getting user with id {}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.USER_NOT_FOUND);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // Update data with userDetails received
        userEntity.setUserName(updateUserDto.getUserName());
        userEntity.setEmail(updateUserDto.getEmail());

        // Save changes
        try {
            LOGGER.debug("Saving updated user into database");
            userRepository.save(userEntity);
        } catch (Exception e) {
            LOGGER.error("Failed while updating user into database - {} - with exception - {}", userEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert to UserDetailsDto and return updated user
        return UserConverter.fromUserEntityToUserDetailsDto(userEntity);
    }
}

