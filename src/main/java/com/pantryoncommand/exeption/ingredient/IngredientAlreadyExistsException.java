package com.pantryoncommand.exeption.ingredient;

/**
 * Ingredient already exists exception trowed when ingredient already exists in database
 */
public class IngredientAlreadyExistsException extends RuntimeException{
    public IngredientAlreadyExistsException(String message) {
        super(message);
    }
}
