package com.pantryoncommand.exeption.recipe;


import com.pantryoncommand.exeption.PantryOnCommandApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link PantryOnCommandApiException} thrown when recipe already exists
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RecipeAlreadyExistsException extends PantryOnCommandApiException {
    public RecipeAlreadyExistsException(String message) {
        super(message);
    }
}
