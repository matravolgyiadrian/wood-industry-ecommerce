package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thesis.woodindustryecommerce.model.CartItem;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.services.ProductService;

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

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
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
    public String home(Model model){
        return "home";
    }

    @GetMapping("/about")
    public String about(){
        return "about_us";
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