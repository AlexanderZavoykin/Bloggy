package com.gmail.aazavoykin.rest.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Accessors(chain = true)
public class StoryDto {

    private Long id;
    @NotBlank(message = "Title can not be blank")
    @Length(min = 6, max = 255, message = "Title should contain from 6 up to 255 symbols")
    private String title;
    private String authorNickname;
    private Long authorId;
    private String created;
    private List<String> tags;
    @NotBlank(message = "Body can not be blank")
    private String body;
}
