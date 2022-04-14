package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thesis.woodindustryecommerce.model.CartItem;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.services.ProductService;
import org.thesis.woodindustryecommerce.services.implementations.EmailSenderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Controller
@SessionAttributes("shopping_cart")
public class HomeController {
    private final ProductService productService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public HomeController(ProductService productService, EmailSenderService emailSenderService) {
        this.productService = productService;
        this.emailSenderService = emailSenderService;
    }

    @ModelAttribute("products")
    public List<Product> addProductsAttribute(){
        return productService.findAll();
    }

    @ModelAttribute("shopping_cart")
    public List<CartItem> addShoppingCartAttribute(){
        return new LinkedList<>();
    }

    @GetMapping({"/", "/home", "/index"})
    public String home(){
        return "home";
    }

    @GetMapping("/about")
    public String about(){
        return "about_us";
    }

    @PostMapping("/contact")
    public ModelAndView contact(String name, String email, String message, RedirectAttributes redirectAttr){
        emailSenderService.sendSimpleEmail(email, name, message);
        redirectAttr.addFlashAttribute("successfulEmail", true);
        return new ModelAndView("redirect:/about");
    }

    @RequestMapping(value={"/robots.txt", "/robot.txt"})
    public void robots(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().write("User-agent: *\nDisallow: /\n");
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }


}