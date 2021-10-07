package org.thesis.woodindustryecommerce.services.implementations;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.repository.ProductRepository;
import org.thesis.woodindustryecommerce.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateName(Long id, String name) {
        productRepository.setProductName(id, name);
    }

    @Override
    public void updatePrice(Long id, double price) {
        productRepository.setProductPrice(id, price);
    }

}
