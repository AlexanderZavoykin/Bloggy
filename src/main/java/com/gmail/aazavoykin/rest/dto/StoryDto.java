package com.gmail.aazavoykin.rest.dto;

import com.gmail.aazavoykin.model.Story;
import com.gmail.aazavoykin.model.Tag;
import com.gmail.aazavoykin.service.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryDto {

    private Long id;

    private String title;

    private String authorNickname;

    private Long authorId;

    private String created;

    private List<Tag> tags = new ArrayList<>();

    private String body;

    public StoryDto(Story story, int charsLimit) {
        id = story.getId();
        title = story.getTitle();
        authorNickname = story.getUser().getNickname();
        authorId = story.getUser().getId();
        created = story.getCreated().format(ServiceUtils.DATE_TIME_FORMATTER);
        tags = story.getTags();
        body = ServiceUtils.cutStoryBody(story.getBody(), charsLimit);
    }

}
