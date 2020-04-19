package com.gmail.aazavoykin.rest.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils.EMAIL_REGEXP;
import static com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils.NICKNAME_REGEXP;
import static com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils.PASSWORD_REGEXP;

@Data
public class UserSignupRequest {

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = PASSWORD_REGEXP,
        message = "Bad formed password. Password should contain at least 1 capital letter, 1 number and 1 special character")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    private String password;
    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = PASSWORD_REGEXP,
        message = "Bad formed password. Password should contain at least 1 capital letter, 1 number and 1 special character")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    private String matchingPassword;
    @NotBlank(message = "Nickname cannot be blank")
    @Pattern(regexp = NICKNAME_REGEXP,
        message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    @Size(min = 8, max = 20, message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    private String nickname;
    @Email(regexp = EMAIL_REGEXP, message = "Email address has invalid format")
    @Size(min = 8, max = 255, message = "Email should contain from 8 up to 255 symbols")
    private String email;
}
