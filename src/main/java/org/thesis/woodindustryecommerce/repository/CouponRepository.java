package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.Coupon;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCouponCode(String couponCode);
    @Query("from Coupon c where lower(c.couponCode) like lower(concat('%', :keyword, '%'))")
    List<Coupon> findByKeyword(@Param("keyword")String keyword);
}
