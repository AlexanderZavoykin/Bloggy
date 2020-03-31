package com.gmail.aazavoykin.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Password cannot be blank")
//    @Pattern(message = "Bad formed password. Password should contain at least one capital letter, " +
//        "at least one number and at least one special symbol of these: '@', '#', '$', '%', '^', '&', '+', '='.",
//        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)$")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    private String password;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
//    @Pattern(message = "Bad formed password. Password should contain at least one capital letter, " +
//        "at least one number and at least one special symbol of these: '@', '#', '$', '%', '^', '&', '+', '='.",
//        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)$")
    private String matchingPassword;
}
