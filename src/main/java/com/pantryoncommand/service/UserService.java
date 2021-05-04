package com.pantryoncommand.service;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.user.CreateUserDto;
import com.pantryoncommand.command.user.UpdateUserDto;
import com.pantryoncommand.command.user.UserDetailsDto;
import com.pantryoncommand.enumerators.UserRole;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.user.UserAlreadyExistsException;
import com.pantryoncommand.exeption.user.UserNotFoundException;
import org.springframework.data.domain.Pageable;

/**
 * Common interface for User service, provides methods to manage Users
 */
public interface UserService {
    /**
     * Create new user
     * @param userRegistrationDto {@link CreateUserDto}
     * @param userRole {@link UserRole}
     * @return {@link UserDetailsDto}
     * @throws UserAlreadyExistsException when the user already exists
     * @throws DatabaseCommunicationException when database connection isn't established
     */
    UserDetailsDto addNewUser(CreateUserDto userRegistrationDto, UserRole userRole) throws UserAlreadyExistsException, DatabaseCommunicationException;

    /**
     * Get given user with id
     * @param userId Receives user id
     * @return {@link UserDetailsDto}
     * @throws UserNotFoundException when user is not found
     */
    UserDetailsDto getUserById(long userId) throws UserNotFoundException;


    /**
     * Gets list of users from database with pagination
     * @param page the page
     * @param size number of elements per page
     * @param userRole the filter by role
     * @return {@link Paginated<UserDetailsDto>}
     */
    Paginated<UserDetailsDto> getUsersList(int page, int size, UserRole userRole);

    /**
     * Deletes certain user with id
     * @param userId Receives user id
     * @throws UserNotFoundException when user is not found
     * @throws DatabaseCommunicationException when database connection isn't established
     */
    void deleteUser(long userId) throws UserNotFoundException, DatabaseCommunicationException;

    /**
     * Update user
     * @param userId Receives user is
     * @param updateUserDto Receives user info
     * @return {@link UserDetailsDto}
     * @throws UserNotFoundException when user is not found
     * @throws DatabaseCommunicationException when database connection isn't established
     */
    UserDetailsDto updateUser(long userId, UpdateUserDto updateUserDto) throws UserNotFoundException,DatabaseCommunicationException;
}
