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

    @GetMapping("/product/all")
    public String getAll(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("productForm", new Product());

        return "product";
    }

    @GetMapping("/product/new")
    public String newProduct(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("productForm", Product.builder().reorderThreshold(10).build());
        model.addAttribute("form", true);
        model.addAttribute("method", "new");

        return "product";
    }

    @PostMapping("/product/new")
    public String newProduct(@ModelAttribute Product productForm) {

        log.debug("Product with name: {} has been successfully created", productForm.getName());
        productForm.setReorderThreshold(10);
        productForm.setStopOrder(false);
        Product savedProduct = productService.save(productForm);

        return "redirect:/product/all";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);

        model.addAttribute("products", productService.findAll());
        model.addAttribute("productForm", product);
        model.addAttribute("form", true);
        model.addAttribute("method", "edit");

        log.debug("Product with id: {} is being edited", id);

        return "product";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product productForm) {
        log.debug("imageUrl: {}", productForm.getImage());
        Product product = productService.findById(id);
        if(productForm.getImage() != null){
            product.setImage(productForm.getImage());
        }
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setStock(productForm.getStock());
        product.setReorderThreshold(productForm.getReorderThreshold());
        productService.save(product);
        return "redirect:/product/all";
    }

    @PostMapping("/product/stop-order/{id}")
    public String stopOrderProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        product.setStopOrder(true);

        productService.save(product);
        log.debug("Product with id: {} is reported to stop order", id);

        return "redirect:/product/all";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);

        log.debug("Product with id: {} has been successfully deleted", id);

        return "redirect:/product/all";
    }
}
