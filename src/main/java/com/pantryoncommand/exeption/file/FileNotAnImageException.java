package com.pantryoncommand.exeption.file;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when file is not an image
 */
public class FileNotAnImageException extends PantryOnCommandApiException {
    public FileNotAnImageException(String message){super(message);}
}
