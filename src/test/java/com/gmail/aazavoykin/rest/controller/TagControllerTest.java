package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.TagDto;
import com.gmail.aazavoykin.rest.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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