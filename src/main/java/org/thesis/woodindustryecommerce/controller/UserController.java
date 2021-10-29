package org.thesis.woodindustryecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thesis.woodindustryecommerce.model.binding.RegistrationForm;
import org.thesis.woodindustryecommerce.services.UserService;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Error");
        }
        model.addAttribute("method", "login");

        return "login";
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String register(Model model, RegistrationForm userForm) {
        model.addAttribute("userForm", userForm);
        model.addAttribute("method", "register");

        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userForm") RegistrationForm userForm, BindingResult bindingResult, Model model) {
        if (userService.findByUsername(userForm.getUsername()) != null) {
            model.addAttribute("usernameExists", true);
            model.addAttribute("method", "register");
            return "login";
        }

        if (userService.findByEmail(userForm.getEmail()) != null) {
            model.addAttribute("emailExists", true);
            model.addAttribute("method", "register");
            return "login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("method", "register");
            return "login";
        }

        userService.createUser(userForm.toUser(), "ROLE_USER");

        model.addAttribute("method", "login");
        return "login";
    }
}
