package com.pantryoncommand.exeption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Recipe already exists exception trowed when recipe is already in database
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RecipeAlreadyExistsException extends RuntimeException {
    public RecipeAlreadyExistsException(String message) {
        super(message);
    }
}
