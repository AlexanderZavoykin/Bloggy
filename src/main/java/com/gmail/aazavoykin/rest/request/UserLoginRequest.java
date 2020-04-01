package com.gmail.aazavoykin.rest.request;

import com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserLoginRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(regexp = RegularExpressionUtils.NICKNAME_REGEXP,
        message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    @Size(min = 8, max = 20, message = "Email should contain from 8 up to 255 symbols")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
