package com.pantryoncommand.exeption;

/**
 * Database communication Exception trowed when communication with database isn't successfully established
 */
public class DatabaseCommunicationException extends PantryOnCommandApiException {
    public DatabaseCommunicationException(String message){
        super(message);
    }
    public DatabaseCommunicationException(String message, Throwable e){
        super(message, e);
    }
}
