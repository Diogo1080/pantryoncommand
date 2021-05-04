package com.pantryoncommand.exeption.file;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when File is not uploaded
 */
public class FileNotUploadedException extends PantryOnCommandApiException {
    public FileNotUploadedException(String message) {
        super(message);
    }
}
