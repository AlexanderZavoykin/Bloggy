package com.gmail.aazavoykin.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentStoryRequest {

    @NotBlank(message = "Comment can not be blank")
    private String body;
}
