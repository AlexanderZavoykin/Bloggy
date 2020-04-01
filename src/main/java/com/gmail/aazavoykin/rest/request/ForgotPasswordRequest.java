package com.gmail.aazavoykin.rest.request;

import com.gmail.aazavoykin.rest.request.util.RegularExpressionUtils;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(regexp = RegularExpressionUtils.EMAIL_REGEXP, message = "Email address has invalid format")
    @Size(min = 8, max = 255, message = "Email should contain from 8 up to 255 symbols")
    private String email;
}
