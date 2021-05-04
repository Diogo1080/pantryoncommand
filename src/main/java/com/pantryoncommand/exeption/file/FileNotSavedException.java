package com.pantryoncommand.exeption.file;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when file is not saved
 */
public class FileNotSavedException extends PantryOnCommandApiException {
    public FileNotSavedException(String message) {
        super(message);
    }
    public FileNotSavedException(String message, Exception e) {
        super(message, e);
    }
}
