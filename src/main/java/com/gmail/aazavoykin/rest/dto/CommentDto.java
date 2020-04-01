package com.gmail.aazavoykin.rest.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentDto {

    private Long id;
    private String authorNickname;
    private Long authorId;
    private String created;
    private String body;
}
