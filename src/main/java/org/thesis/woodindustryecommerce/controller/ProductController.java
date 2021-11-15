package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.services.ProductService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller

public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/all")
    public String getAll(Model model){
        model.addAttribute("products", productService.findAll());
        model.addAttribute("productForm", new Product());

        return "product";
    }

    @GetMapping("/product/new")
    public String newProduct(Model model){
        model.addAttribute("products", productService.findAll());
        model.addAttribute("productForm", new Product());
        model.addAttribute("form", true);
        model.addAttribute("method", "new");

        return "product";
    }

    @PostMapping("/product/new")
    public String newProduct(@ModelAttribute  Product productForm){

        log.debug("Product with name: {} has been successfully created", productForm.getName());
        Product savedProduct = productService.save(productForm);

        return "redirect:/product/all";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){
        Product product = productService.findById(id);

        model.addAttribute("products", productService.findAll());
        model.addAttribute("productForm", product);
        model.addAttribute("form", true);
        model.addAttribute("method", "edit");

        log.debug("Product with id: {} is being edited", id);

        return "product";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product productForm){
        log.info("imageUrl: {}", productForm.getImage());
        Product newProduct = new Product(id, productForm.getName(), productForm.getPrice(), productForm.getStock(), productForm.getImage(), "");
        productService.save(newProduct);
        return "redirect:/product/all";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.delete(id);

        log.debug("Product with id: {} has been successfully deleted", id);

        return "redirect:/product/all";
    }
}
