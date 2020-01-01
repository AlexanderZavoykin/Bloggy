package com.gmail.aazavoykin.rest.request;

import com.gmail.aazavoykin.model.Publication;
import com.gmail.aazavoykin.model.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreatePublicationRequest {

    private String title;

    private List<Tag> tags = new ArrayList<>();

    private String body;

    public Publication publication() {
        Publication publication = new Publication();
        publication.setTitle(title);
        publication.setBody(body);
        publication.setTags(tags);
        return publication;
    }

}
