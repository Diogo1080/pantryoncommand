package com.pantryoncommand.exeption.ingredient;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when ingredient not found
 */
public class IngredientNotFoundException extends PantryOnCommandApiException {
    public IngredientNotFoundException(String message) {
        super(message);
    }
}