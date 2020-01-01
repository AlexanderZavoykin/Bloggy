package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.exception.EntityNotFoundRestException;
import com.gmail.aazavoykin.model.Publication;
import com.gmail.aazavoykin.rest.request.CreatePublicationRequest;
import com.gmail.aazavoykin.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/bloggy/publication")
@RequiredArgsConstructor
public class PublicationController {

    @Autowired
    private final PublicationService publicationService;

    @GetMapping("/")
    public ModelAndView getAll() {
        ModelAndView modelAndView = new ModelAndView("publications");
        modelAndView.addObject("publications", publicationService.getAll());
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @GetMapping("/{publicationId}")
    public ModelAndView getById(@PathVariable("publicationId") Long publicationId) {
        ModelAndView modelAndView = new ModelAndView("publication");
        Publication publication = publicationService.getById(publicationId).orElseThrow(() ->
                new EntityNotFoundRestException("Publication not found", HttpStatus.NOT_FOUND));
        modelAndView.addObject("publication", publication);
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{publicationId}")
    public ModelAndView create(@PathVariable("userId") Long userId,
                               @Valid @RequestBody CreatePublicationRequest request) {
        Publication publication = new Publication();
        publication.setTitle(request.getTitle());
        publication.setBody(request.getBody());
        publication.setTags(request.getTags());
        publication.setCreated(LocalDateTime.now());
        Publication publicated = publicationService.save(publication);

        ModelAndView modelAndView = new ModelAndView("publication");
        modelAndView.addObject("publication", publicated);
        modelAndView.setStatus(HttpStatus.CREATED);
        return modelAndView;
    }

}
