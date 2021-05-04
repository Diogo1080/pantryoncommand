package com.pantryoncommand.exeption.category;

import com.pantryoncommand.exeption.PantryOnCommandApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link PantryOnCommandApiException} thrown when category already exists
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryAlreadyExistsException extends PantryOnCommandApiException {

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
