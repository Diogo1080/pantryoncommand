package com.pantryoncommand.exeption.user;

import com.pantryoncommand.exeption.PantryOnCommandApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link PantryOnCommandApiException} thrown when User already exists
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends PantryOnCommandApiException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
