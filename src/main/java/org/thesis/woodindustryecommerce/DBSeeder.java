package org.thesis.woodindustryecommerce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.repository.CartRepository;
import org.thesis.woodindustryecommerce.repository.ProductRepository;
import org.thesis.woodindustryecommerce.repository.UserRepository;

@Slf4j
@Component
@Profile("!test")
public class DBSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public DBSeeder(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Product tv = Product.builder().name("TV").price(50000D).build();
        productRepository.save(tv);
    }
}
