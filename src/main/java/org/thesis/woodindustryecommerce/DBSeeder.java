package org.thesis.woodindustryecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.model.Role;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.repository.ProductRepository;
import org.thesis.woodindustryecommerce.repository.RoleRepository;
import org.thesis.woodindustryecommerce.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@Profile("!test")
public class DBSeeder {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public DBSeeder(ProductRepository productRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = new BCryptPasswordEncoder();
    }


    @PostConstruct
    public void run() {
        Product tv = Product.builder().name("TV").price(50000D).stock(200).build();
        this.productRepository.save(tv);

        if (roleRepository.count() == 0) {
            Role admin = new Role();
            admin.setAuthority("ROLE_ADMIN");

            Role user = new Role();
            user.setAuthority("ROLE_USER");

            this.roleRepository.saveAndFlush(admin);
            this.roleRepository.saveAndFlush(user);
        }

        userRepository.deleteAll();
        User adminUser = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                .email("admin@admin.com")
                .address("35045 Alabama, Clanton 2273 Brookside Drive street")
                .authorities(Set.of(roleRepository.findByAuthority("ROLE_ADMIN")))
                .build();
        userRepository.saveAndFlush(adminUser);
    }
}
