package com.gmail.aazavoykin.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils.PASSWORD_REGEXP;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = PASSWORD_REGEXP,
        message = "Bad formed password. Password should contain at least 1 capital letter, 1 number and 1 special character")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    private String password;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    @Pattern(regexp = PASSWORD_REGEXP,
        message = "Bad formed password. Password should contain at least 1 capital letter, 1 number and 1 special character")
    private String matchingPassword;
}
