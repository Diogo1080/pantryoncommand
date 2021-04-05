package com.pantryoncommand.exeption;

import com.pantryoncommand.error.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
/**
 * Pantryoncommand exception handler for handling exceptions from Response Entity
 */
@ControllerAdvice
public class PantryOnCommandExeptionHandler extends ResponseEntityExceptionHandler {

    /**
     * handle "not found" exceptions
     * @param ex The exception
     * @param request The request for that exception
     * @return {@link Error}
     */
    @ExceptionHandler(value = {
            UserNotFoundException.class,
            RecipeNotFoundException.class})
    public ResponseEntity<Error> handlerNotFoundException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    /**
     * handle "already exists" exceptions
     * @param ex The exception
     * @param request The request for that exception
     * @return {@link Error}
     */
    @ExceptionHandler(value = {
            UserAlreadyExistsException.class,
            RecipeAlreadyExistsException.class})
    public ResponseEntity<Error> handlerConflictException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT);
    }

    /**
     * Handle exception (default)
     * @param ex The exception
     * @param request The request for that exception
     * @return {@link Error}
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Error> handlerAnyOtherException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle method argument not valid
     * @param ex the parameter
     * @param headers httpHeaders
     * @param httpStatus httpStatus
     * @param request the request
     * @return {@link Error}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {
        Error error = Error.builder()
                .timestamp(new Date())
                .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error, httpStatus);
    }

    /**
     * Build the error response
     * @param ex the exception
     * @param request the request
     * @param httpStatus the httpStatus
     * @return {@link Error}
     */
    public ResponseEntity<Error> buildErrorResponse(Exception ex, HttpServletRequest request, HttpStatus httpStatus) {
        Error error = Error.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, httpStatus);
    }
}