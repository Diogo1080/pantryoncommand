package com.pantryoncommand.command.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * CreateUserDto mainly used to store user info when creating users
 */

@Data
@Builder
public class CreateUserDto {
    @NotBlank(message = "Must have a user name")
    private String userName;

    @Email(message = "Your email doesn't fit")
    private String email;

    @NotBlank(message = "Must have a password")
    private String password;


    /**
     * toString method
     * @return Returns a overwrite of toString in order to not log sensible data
     */
    @Override
    public String toString() {
        return "CreateUserDto{" +
                "firstName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='***'"+'\'' +
                '}';
    }
}
