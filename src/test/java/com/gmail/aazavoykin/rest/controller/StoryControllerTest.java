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

class StoryControllerTest extends AbstractControllerTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private StoryRepository storyRepository;

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
    public void shouldGetOnlyNotRoughStories_byTag_byUnauthenticated() {
        final ResponseEntity<Response<List<StoryDto>>> response = restTemplate.exchange("/stories/all?tag=travel",
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

    @Test
    @DirtiesContext
    public void shouldAddNewStory_bySingleUser() {
        assertEquals(1, storyRepository.getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc().size());
        final List<String> tags = Arrays.asList("Amazing", "Wonderful", "Interesting");
        final StorySaveRequest request = new StorySaveRequest();
        request.setTitle("Another amazing story");
        request.setBody("When I was young...");
        request.setTagNames(tags);
        final ResponseEntity<Response<StoryDto>> response = restTemplate.exchange("/stories/add",
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
        assertEquals(request.getTitle(), storyDto.getTitle());
        assertEquals(request.getBody(), storyDto.getBody());
        assertEquals(tags, storyDto.getTags().stream().map(TagDto::getName).collect(Collectors.toList()));
        assertEquals(2, storyRepository.getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc().size());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateStory_bySingleUser() {
        final Story story = storyRepository.getById(1000002L);
        assertNotNull(story);
        assertEquals(1, storyRepository.getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc().size());

        /*
         ** Deleting all tags, marking story as rough (not to be published after update),
         ** changing its body and title
         */
        final StoryUpdateRequest request = new StoryUpdateRequest();
        request.setRough(true);
        request.setTitle("Changed title");
        request.setBody("Changed body");
        request.setTagNames(Collections.emptyList());
        final ResponseEntity<Response<StoryDto>> response = restTemplate.exchange("/stories/1000002/update",
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
        assertEquals(request.getTitle(), storyDto.getTitle());
        assertEquals(request.getBody(), storyDto.getBody());
        assertTrue(storyDto.getTags().isEmpty());
        assertTrue(storyDto.isRough());
        assertEquals(0, storyRepository.getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc().size());
    }

    @Test
    @DirtiesContext
    public void shouldDeleteStory_bySingleUser() {
        final Story story = storyRepository.getById(1000002L);
        assertNotNull(story);
        assertEquals(1, storyRepository.getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc().size());
        final ResponseEntity<Response<StoryDto>> response = restTemplate.exchange("/stories/1000002/delete",
            HttpMethod.POST,
            prepareHttpEntity(null, getSingleUserAccessToken()),
            new ParameterizedTypeReference<Response<StoryDto>>() {
            });
        final Response<StoryDto> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        assertEquals(0, storyRepository.getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc().size());
    }
}