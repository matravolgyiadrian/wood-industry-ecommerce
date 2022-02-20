package org.thesis.woodindustryecommerce;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thesis.woodindustryecommerce.controller.*;
import org.thesis.woodindustryecommerce.controller.websocket.CouponValidationController;

@SpringBootTest
@ActiveProfiles("test")
public class SmokeTest {

    @Autowired private HomeController controller;
    @Autowired private CartController cartController;
    @Autowired private CouponController couponController;
    @Autowired private OrderController orderController;
    @Autowired private ProductController productController;
    @Autowired private UserController userController;
    @Autowired private CouponValidationController couponValidationController;

    @Test
    public void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
        Assertions.assertThat(cartController).isNotNull();
        Assertions.assertThat(couponController).isNotNull();
        Assertions.assertThat(orderController).isNotNull();
        Assertions.assertThat(productController).isNotNull();
        Assertions.assertThat(userController).isNotNull();
        Assertions.assertThat(couponValidationController).isNotNull();
    }
}
