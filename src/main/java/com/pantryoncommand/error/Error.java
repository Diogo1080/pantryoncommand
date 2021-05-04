package com.pantryoncommand.error;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Error class for error formating
 */
@Data
@Builder
public class Error {

    Date timestamp;
    String message;
    String method;
    String exception;
    String path;
}