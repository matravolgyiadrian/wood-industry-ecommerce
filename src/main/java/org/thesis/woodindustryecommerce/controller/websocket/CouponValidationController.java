package org.thesis.woodindustryecommerce.controller.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    @SendTo("/coupon/validation")
    public CouponMessage validate(CouponCode code, SimpMessageHeaderAccessor headerAccessor){
        Map<String, Object> attributes = headerAccessor.getSessionAttributes();
        assert attributes != null;
        log.info("Session attributes has shopping_cart? : {}", attributes.containsKey("shopping_cart"));

        Coupon coupon = couponService.findByCouponCode(code.getCode());
        if (coupon == null){
            return new CouponMessage(false);
        } else{
            return coupon.toCouponMessage(true);
        }
    }
}
