package com.pantryoncommand.exeption.file;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when file is not found
 */
public class FileNotFoundException extends PantryOnCommandApiException {
    public FileNotFoundException(String message){super(message);}
    public FileNotFoundException(String message, Exception e){super(message, e);}
}
