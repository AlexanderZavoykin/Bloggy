package com.gmail.aazavoykin.rest.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils.EMAIL_REGEXP;

@Data
public class UserLoginRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(regexp = EMAIL_REGEXP, message = "Email address has invalid format")
    @Size(min = 8, max = 255, message = "Email should contain from 8 up to 255 symbols")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
