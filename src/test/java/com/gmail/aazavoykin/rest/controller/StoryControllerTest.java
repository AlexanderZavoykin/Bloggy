package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.CommentDto;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.request.CommentStoryRequest;
import com.gmail.aazavoykin.rest.response.Response;
import com.gmail.aazavoykin.service.StoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

class StoryControllerTest extends AbstractControllerTest {

    @Autowired
    private StoryService storyService;

    @Test
    public void shouldGetOnlyNotRoughStories_byUnauthenticated() {
        final ResponseEntity<Response<List<StoryDto>>> response = restTemplate.exchange("/stories/all",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<List<StoryDto>>>() {
            });
        final Response<List<StoryDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final List<StoryDto> storyDtos = body.getBody();
        assertNotNull(storyDtos);
        assertEquals(1, storyDtos.size());
    }

    @Test
    public void shouldGetLast10Stories_byUnauthenticated() {
        final ResponseEntity<Response<List<StoryDto>>> response = restTemplate.exchange("/stories/last10",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<List<StoryDto>>>() {
            });
        final Response<List<StoryDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final List<StoryDto> storyDtos = body.getBody();
        assertNotNull(storyDtos);
        assertEquals(1, storyDtos.size());
    }

    @Test
    public void shouldGetNotRoughStory_byUnauthenticated() {
        final ResponseEntity<Response<StoryDto>> response = restTemplate.exchange("/stories/1000002",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<StoryDto>>() {
            });
        final Response<StoryDto> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final StoryDto storyDto = body.getBody();
        assertNotNull(storyDto);
        assertEquals("Just another story", storyDto.getBody());
        assertEquals("How I spend summer", storyDto.getTitle());
        assertEquals("CharlieSheen", storyDto.getAuthorNickname());
    }

    @Test
    public void shouldNotGetRoughStory_byNotAuthor() {
        final ResponseEntity<Response<StoryDto>> response = restTemplate.exchange("/stories/1000003",
            HttpMethod.GET,
            prepareHttpEntity(null, getAdminAccessToken()),
            new ParameterizedTypeReference<Response<StoryDto>>() {
            });
        final Response<StoryDto> body = response.getBody();
        assertNotNull(body);
        Assertions.assertThrows(InternalException.class, () -> storyService.getById(1000003L));
        assertNotNull(body);
        assertEquals("Story not found", body.getMessage());
        assertEquals("E002", body.getCode());
    }

    @Test
    public void shouldGetRoughStory_byAuthor() {
        final ResponseEntity<Response<StoryDto>> response = restTemplate.exchange("/stories/1000003",
            HttpMethod.GET,
            prepareHttpEntity(null, getSingleUserAccessToken()),
            new ParameterizedTypeReference<Response<StoryDto>>() {
            });
        final Response<StoryDto> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final StoryDto storyDto = body.getBody();
        assertNotNull(storyDto);
        assertEquals("Rough story - not for publishing yet", storyDto.getBody());
        assertEquals("What about ice-cream?", storyDto.getTitle());
        assertEquals("CharlieSheen", storyDto.getAuthorNickname());
    }

    @Test
    public void shouldLeaveComment_bySingleUser() {
        final String comment = "What a story, Mark!";
        final CommentStoryRequest request = new CommentStoryRequest();
        request.setBody(comment);
        final ResponseEntity<Response<StoryDto>> response = restTemplate.exchange("/stories/1000002/comment",
            HttpMethod.POST,
            prepareHttpEntity(request, getSingleUserAccessToken()),
            new ParameterizedTypeReference<Response<StoryDto>>() {
            });
        final Response<StoryDto> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final StoryDto storyDto = body.getBody();
        assertNotNull(storyDto);
        assertEquals(1000002L, (long) storyDto.getId());
        assertTrue(storyDto.getComments().stream().map(CommentDto::getBody).anyMatch(comment::equalsIgnoreCase));
    }


}