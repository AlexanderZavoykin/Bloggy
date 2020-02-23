package com.gmail.aazavoykin.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StoryDto {

    private Long id;

    private String title;

    private String authorNickname;

    private Long authorId;

    private String created;

    private List<String> tags = new ArrayList<>();

    private String body;

}
