package com.pantryoncommand.exeption;

public class PantryOnCommandApiException extends RuntimeException{
    public PantryOnCommandApiException() {
    }

    public PantryOnCommandApiException(String message) {
        super(message);
    }

    public PantryOnCommandApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public PantryOnCommandApiException(Throwable cause) {
        super(cause);
    }

    public PantryOnCommandApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
