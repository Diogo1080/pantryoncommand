package com.pantryoncommand.exeption.user;

import com.pantryoncommand.exeption.PantryOnCommandApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * A {@link PantryOnCommandApiException} thrown when user is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends PantryOnCommandApiException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
