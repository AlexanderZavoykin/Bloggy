package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.db.model.Story;
import com.gmail.aazavoykin.db.repository.StoryRepository;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.CommentDto;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.dto.TagDto;
import com.gmail.aazavoykin.rest.request.CommentStoryRequest;
import com.gmail.aazavoykin.rest.request.StorySaveRequest;
import com.gmail.aazavoykin.rest.request.StoryUpdateRequest;
import com.gmail.aazavoykin.rest.response.Response;
import com.gmail.aazavoykin.service.StoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

class TagControllerTest extends AbstractControllerTest {

    @Test
    public void shouldGetAllTags_byUnauthenticated() {
        final ResponseEntity<Response<List<TagDto>>> response = restTemplate.exchange("/tags/all",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<List<TagDto>>>() {
            });
        final Response<List<TagDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final List<TagDto> tagDtos = body.getBody();
        assertNotNull(tagDtos);
        assertEquals(3, tagDtos.size());
    }
}