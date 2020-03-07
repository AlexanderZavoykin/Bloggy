package com.gmail.aazavoykin.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequest {

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
}
