package com.pantryoncommand.exeption.ingredient;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when ingredient already exists exception
 */
public class IngredientAlreadyExistsException extends PantryOnCommandApiException {
    public IngredientAlreadyExistsException(String message) {
        super(message);
    }
}
