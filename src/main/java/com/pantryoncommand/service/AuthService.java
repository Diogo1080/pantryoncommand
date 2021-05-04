package com.pantryoncommand.service;

import com.pantryoncommand.command.auth.CredentialsDto;
import com.pantryoncommand.command.auth.LoggedInDto;
import com.pantryoncommand.command.auth.PrincipalDto;

/**
 * Common interface for authorization operations
 */
public interface AuthService {

    /**
     * Login user
     * @param credentialsDto
     * @return {@link LoggedInDto} logged in user details
     */
    LoggedInDto loginUser(CredentialsDto credentialsDto);

    /**
     * Validate token
     * @param token
     * @return {@link PrincipalDto} principal authenticated
     */
    PrincipalDto validateToken(String token);
}