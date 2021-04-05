package com.pantryoncommand.command.user;

import lombok.Builder;
import lombok.Data;

/**
 * CreateUserDto mainly used to store user info when creating users
 */

@Data
@Builder
public class UserDetailsDto {
    private long id;

    private String userName;

    private String email;

}
