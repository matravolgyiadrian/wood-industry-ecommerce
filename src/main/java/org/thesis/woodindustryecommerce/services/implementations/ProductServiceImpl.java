package org.thesis.woodindustryecommerce.services.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.repository.ProductRepository;
import org.thesis.woodindustryecommerce.services.ProductService;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final CloudinaryService cloudinaryService;

    private final AtomicInteger counter = new AtomicInteger();
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(CloudinaryService cloudinaryService, ProductRepository productRepository){
        this.cloudinaryService = cloudinaryService;
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
        String url = cloudinaryService.uploadFile(product.getImage());
        product.setImageUrl(url);
        log.info("saved product image: {}", url);

        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
