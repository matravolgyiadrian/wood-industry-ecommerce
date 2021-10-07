package org.thesis.woodindustryecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thesis.woodindustryecommerce.services.ProductService;

@Controller
public class HomeController {
    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/home", "/index"})
    public String home(Model model){
        model.addAttribute("products", productService.findAll());

        return "home";
    }

}