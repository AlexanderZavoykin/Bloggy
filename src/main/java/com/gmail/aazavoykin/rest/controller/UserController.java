package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.exception.EntityNotFoundRestException;
import com.gmail.aazavoykin.model.Publication;
import com.gmail.aazavoykin.model.User;
import com.gmail.aazavoykin.service.PublicationService;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/bloggy/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final PublicationService publicationService;

    @Autowired
    private final UserService userService;

    @GetMapping("/{userID}")
    public ModelAndView userInfo(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId).orElseThrow(() ->
                new EntityNotFoundRestException("User with id='" + userId + "' is not found", HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("user");
        modelAndView.addObject("user", user);
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @GetMapping("/{userID}/publications")
    public ModelAndView getAllForUser(@PathVariable("userId") Long userId) {
        List<Publication> publications = publicationService.getAllByUserId(userId);
        ModelAndView modelAndView = new ModelAndView("publications");
        modelAndView.addObject("publications", publications);
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }
    
}
