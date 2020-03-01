package com.gmail.aazavoykin.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserLoginRequest {

    @NotBlank(message = "Nickname cannot be blank")
    @Pattern(message = "Bad formed nickname",
            regexp = "^[a-zA-Z0-9_-]{8,20}$")
    @Size(min = 8, max = 20, message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    private String email;

    private String password;

}
