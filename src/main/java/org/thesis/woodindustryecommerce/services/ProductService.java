package org.thesis.woodindustryecommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thesis.woodindustryecommerce.model.Product;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    List<Product> findAll();
    List<Product> findByKeyword(String keyword);
    Product findById(Long id);
    Product save(Product product);
    void delete(Long id);
}
