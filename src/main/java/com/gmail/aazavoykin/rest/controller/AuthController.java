package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.model.User;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(name = "error", required = false) Boolean error, Model model) {
        final ModelAndView modelAndView = new ModelAndView("auth/login");
        if (Boolean.TRUE.equals(error)) {
            modelAndView.addObject("error", true);
        }
        return new ModelAndView("auth/login");
    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationForm() {
        final ModelAndView modelAndView = new ModelAndView("auth/registration");
        modelAndView.addObject("user", new UserDto());
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView registerUser(@ModelAttribute("user") @Valid UserDto userDto) {
        final User registered = createUserAccount(userDto);
        if (registered == null) {
            return new ModelAndView("auth/registration", "user", userDto);
        }
        return new ModelAndView("auth/successful_registration", "user", userDto);
    }

    private User createUserAccount(UserDto userDto) {
        final User registered;
        try {
            registered = userService.save(userDto);
        } catch (InternalException e) {
            return null;
        }
        return registered;
    }

}
