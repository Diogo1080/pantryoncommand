package com.pantryoncommand.exeption.ingredient;

/**
 * Ingredient not found exception trowed when ingredient is not found in database
 */
public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(String message) {
        super(message);
    }
}