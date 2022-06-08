package org.thesis.woodindustryecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.thesis.woodindustryecommerce.model.Coupon;
import org.thesis.woodindustryecommerce.model.Product;
import org.thesis.woodindustryecommerce.model.Role;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.repository.CouponRepository;
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
    private final CouponRepository couponRepository;

    @Autowired
    public DBSeeder(ProductRepository productRepository, UserRepository userRepository, RoleRepository roleRepository, CouponRepository couponRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.couponRepository = couponRepository;
        this.encoder = new BCryptPasswordEncoder();
    }


    @PostConstruct
    public void run() {
        Product chair = Product.builder()
                .name("Wooden chair")
                .price(50000D)
                .stock(200)
                .stopOrder(false)
                .reorderThreshold(10)
                .imageUrl("https://res.cloudinary.com/hlfoeju87/image/upload/v1637008986/izooujnou3npkswl5n0l.jpg")
                .build();
        this.productRepository.save(chair);

        if (roleRepository.count() == 0) {
            Role admin = new Role();
            admin.setAuthority("ROLE_ADMIN");

            Role user = new Role();
            user.setAuthority("ROLE_USER");

            this.roleRepository.saveAndFlush(admin);
            this.roleRepository.saveAndFlush(user);
        }

        couponRepository.save(Coupon.builder().couponCode("CODE20").discountAmount(20).build());

        userRepository.deleteAll();
        User adminUser = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                .fullName("admin")
                .email("admin@admin")
                .address("35045 Alabama, Clanton 2273 Brookside Drive street")
                .authorities(Set.of(roleRepository.findByAuthority("ROLE_ADMIN")))
                .build();
        userRepository.saveAndFlush(adminUser);
    }
}
