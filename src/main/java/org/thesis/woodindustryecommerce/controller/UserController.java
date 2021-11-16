package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.services.OrderService;
import org.thesis.woodindustryecommerce.services.UserService;

import java.security.Principal;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
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
    public String register(Model model, User userForm) {
        model.addAttribute("userForm", userForm);

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User userForm, Model model) {
        if (userService.findByUsername(userForm.getUsername()) != null) {
            model.addAttribute("usernameExists", true);
            return "register";
        }

        if (userService.findByEmail(userForm.getEmail()) != null) {
            model.addAttribute("emailExists", true);
            return "register";
        }

        userService.createUser(userForm, "ROLE_USER");

        return "redirect:/login";
    }

    @GetMapping("/user-details")
    public String userDetails(Model model, Principal principal){
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("orders", orderService.findByCustomer(principal.getName()));
        return "myprofile";
    }

    @GetMapping("/user/edit")
    public String editUser(Model model, Principal principal){
        model.addAttribute("userToEdit", userService.findByUsername(principal.getName()));

        log.info("user to edit before edit: {}", userService.findByUsername(principal.getName()).toString());

        return "edit_user";
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute User userToEdit, Model model, Principal principal) {

        userToEdit.setUsername(principal.getName());
        log.info("User to edit form: {}", userToEdit.toString());

        if (userService.findByEmail(userToEdit.getEmail()) != null) {
            model.addAttribute("emailExists", true);
            return "edit_user";
        }

        userService.editUser(userToEdit);

        return "redirect:/user-details";
    }
}
