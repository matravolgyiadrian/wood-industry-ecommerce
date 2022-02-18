package org.thesis.woodindustryecommerce.services;

import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Coupon;

import java.util.List;

@Service
public interface CouponService {
    List<Coupon> findAll();
    Coupon findById(Long id);
    Coupon findByCouponCode(String code);
    List<Coupon> findByKeyword(String keyword);
    Coupon save(Coupon coupon);
    void delete(Long id);
}
