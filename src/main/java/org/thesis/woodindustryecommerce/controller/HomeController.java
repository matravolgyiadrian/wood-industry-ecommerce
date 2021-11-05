package org.thesis.woodindustryecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thesis.woodindustryecommerce.model.CartItem;
import org.thesis.woodindustryecommerce.services.ProductService;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
public class HomeController {
    private final ProductService productService;


    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/home", "/index"})
    public String home(Model model, HttpSession session){
        model.addAttribute("products", productService.findAll());
        getCart(session);
        return "home";
    }

    @GetMapping("/about")
    public String about(){
        return "about_us";
    }

    private List<CartItem> getCart(HttpSession session) {
        if (session.getAttribute("shopping_cart") == null) {
            session.setAttribute("shopping_cart", new LinkedList<>());
        }

        return (List<CartItem>) session.getAttribute("shopping_cart");
    }

}