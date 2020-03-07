package com.gmail.aazavoykin.rest.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class TagDto {

    @NotBlank(message = "Tag name can not be blank")
    @Length(min = 6, max = 30, message = "Tag name should have 6 .. 30 characters")
    @Pattern(message = "Tag name can contain only lowercase letters",
        regexp = "^[a-z]$")
    private String name;
}
