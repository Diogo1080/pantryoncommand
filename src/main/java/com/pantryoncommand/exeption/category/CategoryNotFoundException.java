package com.pantryoncommand.exeption.category;

import com.pantryoncommand.exeption.PantryOnCommandApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link PantryOnCommandApiException} thrown when category is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends PantryOnCommandApiException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
