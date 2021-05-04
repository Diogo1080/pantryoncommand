package com.pantryoncommand.controller;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.user.CreateUserDto;
import com.pantryoncommand.command.user.UpdateUserDto;
import com.pantryoncommand.command.user.UserDetailsDto;
import com.pantryoncommand.enumerators.UserRole;
import com.pantryoncommand.error.ErrorMessages;
import com.pantryoncommand.exeption.PantryOnCommandApiException;
import com.pantryoncommand.service.UserServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

/**
 * User Controller provides end points for CRUD operations on users
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    private final UserServiceImp userService;

    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    /**
     * Create new user
     * @param createUserDto {@link CreateUserDto}
     * @return {@link UserDetailsDto}
     */
    @PostMapping
    public ResponseEntity<UserDetailsDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {

        LOGGER.info("Request to create user - {} and role {}.", createUserDto, UserRole.USER);
        UserDetailsDto userDetailsDto =  userService.addNewUser(createUserDto, UserRole.USER);

        LOGGER.info("Retrieving created user {}", userDetailsDto);
        return new ResponseEntity<>(userDetailsDto, HttpStatus.CREATED);
    }

    /**
     * Get user with certain id
     * @param userId Receives user id
     * @return {@link UserDetailsDto}
     */
    @GetMapping("/{userId}")
    @PreAuthorize("@authorized.isUser(#userId) ||" +
            "@authorized.hasRole(\"MOD\") ||" +
            "@authorized.hasRole(\"ADMIN\")")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable long userId) {
        LOGGER.info("Request to get user of id - {}", userId);
        UserDetailsDto userDetailsDto = userService.getUserById(userId);

        LOGGER.info("Retrieving user with info - {}",userDetailsDto);
        return new ResponseEntity<>(userDetailsDto, HttpStatus.OK);
    }


    /**
     * Gets list of users from database with pagination
     * @param page the page number
     * @param size the number of elements per page
     * @Param userRole {@link UserRole}
     * @return {@link Paginated<UserDetailsDto>}
     */
    @GetMapping
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") ||" +
            "(@authorized.hasRole(\"MOD\") && #userRole.name() == \"USER\")")
    public ResponseEntity<Paginated<UserDetailsDto>> getUsersList(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(name = "type", defaultValue = "CUSTOMER") UserRole userRole) {

        LOGGER.info("Request to get user list with page and size - {} {}", page, size);

        Paginated<UserDetailsDto> usersList;
        try {
            usersList = userService.getUsersList(page,size,userRole);
        } catch (PantryOnCommandApiException e){
            throw e;
        } catch (Exception e){
            LOGGER.error("Failed to get all users", e);
            throw new PantryOnCommandApiException(ErrorMessages.OPERATION_FAILED);
        }

        LOGGER.info("Retrieving List of users with info - {}", usersList);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    /**
     * Update user with certain id
     * @param userId Receives user id
     * @param updateUserDto {@link UpdateUserDto}
     * @return {@link UserDetailsDto}
     */
    @PutMapping("/{userId}")
    @PreAuthorize("@authorized.isUser(#userId) ||" +
            "@authorized.hasRole(\"ADMIN\")")
    public ResponseEntity<UserDetailsDto> updateUser(@PathVariable long userId,
                                                     @Valid @RequestBody UpdateUserDto updateUserDto) {
        LOGGER.info("Request to update user of id - {} - with info - {}", userId,updateUserDto);
        UserDetailsDto userDetailsDto = userService.updateUser(userId, updateUserDto);

        LOGGER.info("Retrieving updated of user with info - {}",userDetailsDto);
        return new ResponseEntity<>(userDetailsDto, HttpStatus.OK);
    }

    /**
     * Delete user with certain id
     * @param userId Receives user id
     * @return Ok if deleted
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\")")
    public ResponseEntity deleteUser(@PathVariable long userId) {
        LOGGER.info("Request to delete user of id - {}", userId);
        userService.deleteUser(userId);

        LOGGER.info("Responding on successful delete");
        return new ResponseEntity(HttpStatus.OK);
    }
}
