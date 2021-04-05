package com.pantryoncommand.command.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * UpdateRentDto mainly used to store user info when updating users
 */
@Data
@Builder
public class UpdateUserDto {

    @NotNull(message="Your userName can't be null")
    private String userName;

    @Email(message="email not valid")
    private String email;
}