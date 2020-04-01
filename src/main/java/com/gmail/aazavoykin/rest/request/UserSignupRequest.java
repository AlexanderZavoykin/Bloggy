package com.gmail.aazavoykin.rest.request;

import com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserSignupRequest {

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = RegularExpressionUtils.PASSWORD_REGEXP,
        message = "Bad formed password. Password should contain at least 1 capital letter, 1 number and 1 special character")
    private String password;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    @Pattern(regexp = RegularExpressionUtils.PASSWORD_REGEXP,
        message = "Bad formed password. Password should contain at least 1 capital letter, 1 number and 1 special character")
    private String matchingPassword;
    @NotBlank(message = "Nickname cannot be blank")
    @Pattern(regexp = RegularExpressionUtils.NICKNAME_REGEXP,
        message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    @Size(min = 8, max = 20, message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    private String nickname;
    @NotBlank(message = "Nickname cannot be blank")
    @Email(message = "Email address has invalid format",
        regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(min = 8, max = 20, message = "Email should contain from 8 up to 255 symbols")
    private String email;
}
