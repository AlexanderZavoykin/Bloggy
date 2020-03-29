package com.gmail.aazavoykin.rest.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StoryUpdateRequest {

    @NotNull
    private Long id;
    @NotBlank(message = "Title can not be blank")
    @Length(min = 6, max = 255, message = "Title should contain from 6 up to 255 symbols")
    private String title;
    private List<String> tagNames;
    @NotBlank(message = "Body can not be blank")
    private String body;
}
