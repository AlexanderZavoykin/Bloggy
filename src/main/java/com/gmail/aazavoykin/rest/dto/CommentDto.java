package com.gmail.aazavoykin.rest.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CommentDto {

    private Long id;

    private String authorNickname;

    private Long authorId;

    private String created;

    @NotBlank(message = "Comment can not be blank")
    @Length(max = 255, message = "Comment should contain up to 255 letters")
    private String body;

}
