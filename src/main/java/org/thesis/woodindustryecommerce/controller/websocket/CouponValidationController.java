package org.thesis.woodindustryecommerce.controller.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.thesis.woodindustryecommerce.model.Coupon;
import org.thesis.woodindustryecommerce.model.websocket.CouponCode;
import org.thesis.woodindustryecommerce.model.websocket.CouponMessage;
import org.thesis.woodindustryecommerce.services.CouponService;

import java.util.Map;

@Controller
@Slf4j
public class CouponValidationController {

    private final CouponService couponService;

    @Autowired
    public CouponValidationController(CouponService couponService) {
        this.couponService = couponService;
    }

    @MessageMapping("/coupon")
    @SendToUser("/coupon/validation")
    public CouponMessage validate(CouponCode code){

        Coupon coupon = couponService.findByCouponCode(code.getCode());
        if (coupon == null){
            return new CouponMessage(false);
        } else{
            return coupon.toCouponMessage(true);
        }
    }
}
