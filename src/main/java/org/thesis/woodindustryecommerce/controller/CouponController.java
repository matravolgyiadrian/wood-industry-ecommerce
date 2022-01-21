package org.thesis.woodindustryecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thesis.woodindustryecommerce.model.Coupon;
import org.thesis.woodindustryecommerce.services.CouponService;

@Controller
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupon/all")
    public String getCoupons(Model model) {
        model.addAttribute("coupons", couponService.findAll());

        return "coupon";
    }

    @GetMapping("/coupon/new")
    public String renderNewCoupon(Model model) {
        model.addAttribute("coupons", couponService.findAll());
        model.addAttribute("form", true);
        model.addAttribute("isNew", true);
        model.addAttribute("couponToEdit", new Coupon());

        return "coupon";
    }

    @PostMapping("/coupon/new")
    public String newCoupon(Model model, Coupon coupon) {
        coupon.setCouponCode(coupon.getCouponCode().toUpperCase());
        for (Coupon temp : couponService.findAll()) {
            if (temp.getCouponCode().equals(coupon.getCouponCode())) {
                //TODO codeAlreadyExists attribute isn't used yet in the html file
                model.addAttribute("codeAlreadyExists", true);
                model.addAttribute("coupons", couponService.findAll());
                model.addAttribute("form", true);
                model.addAttribute("isNew", true);
                model.addAttribute("couponToEdit", coupon);
                return "coupon";
            }
        }
        couponService.save(coupon);

        return "redirect:/coupon/all";
    }

    @GetMapping("/coupon/edit/{id}")
    public String renderEditCoupon(@PathVariable Long id, Model model) {
        model.addAttribute("coupons", couponService.findAll());
        model.addAttribute("form", true);
        model.addAttribute("isEdit", true);
        model.addAttribute("couponToEdit", couponService.findById(id));

        return "coupon";
    }

    @PostMapping("/coupon/edit/{id}")
    public String editCoupon(@PathVariable Long id, @ModelAttribute Coupon couponForm) {
        Coupon coupon = new Coupon(id, couponForm.getCouponCode().toUpperCase(), couponForm.getDiscountAmount());

        couponService.save(coupon);

        return "redirect:/coupon/all";
    }

    @PostMapping("/coupon/delete/{id}")
    public String deleteCoupon(@PathVariable Long id, Model model) {
        couponService.delete(id);

        return "redirect:/coupon/all";
    }
}
