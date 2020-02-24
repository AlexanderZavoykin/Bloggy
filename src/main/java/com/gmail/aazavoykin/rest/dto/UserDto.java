package com.gmail.aazavoykin.rest.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class UserDto {

    @NotBlank(message = "Nickname cannot be blank")
    @Pattern(message = "Bad formed nickname",
            regexp = "^[a-zA-Z0-9_-]{8,20}$")
    @Size(min = 8, max = 20, message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    private String nickname;

    @Email(message = "Email address has invalid format",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Size(max = 120, message = "Password should contain up to 80 symbols")
    private String email;

    @Length(max = 255, message = "User info should contain up to 255 symbols")
    private String info;

}
