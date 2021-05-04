package com.pantryoncommand.exeption.file;

import com.pantryoncommand.exeption.PantryOnCommandApiException;

/**
 * A {@link PantryOnCommandApiException} thrown when reading file is not successful
 */
public class ReadingFileException extends PantryOnCommandApiException {

    public ReadingFileException(String message, Exception e){super(message, e);}
}
