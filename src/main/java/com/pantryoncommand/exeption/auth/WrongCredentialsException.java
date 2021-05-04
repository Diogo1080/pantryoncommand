package com.pantryoncommand.exeption.auth;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when the provided credentials are invalid
 */
public class WrongCredentialsException extends PantryOnCommandApiException {
    public WrongCredentialsException(String message) {
        super(message);
    }
}

