package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.thesis.woodindustryecommerce.services.ProductService;

@Slf4j
@Controller
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
}
