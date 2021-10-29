package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCouponCode(String couponCode);
}
