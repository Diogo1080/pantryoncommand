package com.pantryoncommand.command.auth;

import com.pantryoncommand.enumerators.UserRole;
import lombok.Builder;
import lombok.Data;

/**
 * Principal information
 * principal definition - entity who can authenticate (user, other service, third-parties...)
 */
@Data
@Builder
public class PrincipalDto {
    private Long userId;
    private String username;
    private UserRole userRole;
}
