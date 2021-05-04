package com.pantryoncommand.security;

import com.pantryoncommand.command.auth.PrincipalDto;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorizationValidatorService {
    public boolean hasRole(String role) {
        return role.equals(getPrincipal().getUserRole().name());
    }

    public boolean isUser(Long userId) {
        return userId.equals(getPrincipal().getUserId());
    }

    private PrincipalDto getPrincipal() {
        return (PrincipalDto) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
