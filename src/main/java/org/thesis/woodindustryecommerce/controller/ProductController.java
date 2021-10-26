package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.services.ProductService;

@Slf4j
@Controller

public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/new")
    public String newProduct(Model model){
        model.addAttribute("productForm", new Product());
        model.addAttribute("method", "new");

        return "product";
    }

    @PostMapping("/product/new")
    public String newProduct(@ModelAttribute  Product productForm, Model model){
        productService.save(productForm);
        log.debug("Product with name: {} has been successfully created", productForm.getName());

        return "redirect:/home";
    }

    @GetMapping("product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){
        Product product = productService.findById(id);

        model.addAttribute("productForm", product);
        model.addAttribute("method", "edit");

        log.debug("Product with id: {} is being edited", id);

        return "product";
    }

    @PostMapping("product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model, @ModelAttribute Product productForm){
        Product newProduct = new Product(id, productForm.getName(), productForm.getPrice(), productForm.getStock());
        productService.save(newProduct);
        return "redirect:/home";
    }

    @PostMapping("product/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.delete(id);

        log.debug("Product with id: {} has been successfully deleted", id);

        return "redirect:/home";
    }

}
