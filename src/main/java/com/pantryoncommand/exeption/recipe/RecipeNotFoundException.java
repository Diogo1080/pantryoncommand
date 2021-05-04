package com.pantryoncommand.exeption.recipe;
import com.pantryoncommand.exeption.PantryOnCommandApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link PantryOnCommandApiException} thrown when recipe is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends PantryOnCommandApiException {

    public RecipeNotFoundException(String message) {
        super(message);
    }
}
