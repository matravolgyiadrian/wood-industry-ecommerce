package org.thesis.woodindustryecommerce.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Coupon;
import org.thesis.woodindustryecommerce.repository.CouponRepository;
import org.thesis.woodindustryecommerce.services.CouponService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Coupon findByCouponCode(String code) {
        return couponRepository.findByCouponCode(code);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public void delete(Long id) {
        couponRepository.deleteById(id);
    }
}
