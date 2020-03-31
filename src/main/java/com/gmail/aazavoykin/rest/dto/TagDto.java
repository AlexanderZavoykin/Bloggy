package com.gmail.aazavoykin.rest.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class TagDto {

    private Long id;
    private LocalDateTime created;
    private String name;
}
