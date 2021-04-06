package com.pantryoncommand.exeption.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Category already exists exception trowed when category already exists in database
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
