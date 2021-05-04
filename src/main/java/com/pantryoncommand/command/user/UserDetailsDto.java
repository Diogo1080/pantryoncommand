package com.pantryoncommand.command.user;

import lombok.Builder;
import lombok.Data;

/**
 * UserDetailsDto mainly used to store user info
 */

@Data
@Builder
public class UserDetailsDto {
    private long id;

    private String userName;

    private String email;

}
