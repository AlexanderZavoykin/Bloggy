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
    private String title;
    private String authorNickname;
    private Long authorId;
    private String created;
    private String updated;
    private List<TagDto> tags;
    private List<CommentDto> comments;
    private String body;
    private boolean rough;
}
