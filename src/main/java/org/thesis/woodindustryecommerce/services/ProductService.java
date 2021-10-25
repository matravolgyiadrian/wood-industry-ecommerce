package org.thesis.woodindustryecommerce.services;

import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Product;

import java.util.List;

@Service
public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void updateName(Long id, String name);
    void updatePrice(Long id, double price);
    void delete(Long id);
}
