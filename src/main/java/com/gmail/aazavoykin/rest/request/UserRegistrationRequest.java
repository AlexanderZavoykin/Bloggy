package com.gmail.aazavoykin.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationRequest {

    @JsonIgnore
    @NotBlank(message = "Password cannot be blank")
    @Pattern(message = "Bad formed password. Password should contain at least one capital letter, " +
            "at least one number and at least one special symbol of these: '@', '#', '$', '%', '^', '&', '+', '='.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)$")
    private final String password;

    @JsonIgnore
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    @Pattern(message = "Bad formed password. Password should contain at least one capital letter, " +
            "at least one number and at least one special symbol of these: '@', '#', '$', '%', '^', '&', '+', '='.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)$")
    private final String matchingPassword;

    @NotBlank(message = "Nickname cannot be blank")
    @Pattern(message = "Bad formed nickname",
            regexp = "^[a-zA-Z0-9_-]{8,20}$")
    @Size(min = 8, max = 20, message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    private final String nickname;

    @Email(message = "Email address has invalid format",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Size(max = 120, message = "Password should contain up to 80 symbols")
    private final String email;

    @Length(max = 255, message = "User info should contain up to 255 symbols")
    private final String info;

}
