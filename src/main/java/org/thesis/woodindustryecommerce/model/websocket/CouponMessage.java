package org.thesis.woodindustryecommerce.model.websocket;

import lombok.*;
import org.thesis.woodindustryecommerce.model.Coupon;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class CouponMessage extends Coupon {
    private boolean valid;

    public CouponMessage() {
    }

    public CouponMessage(boolean valid) {
        this.valid = valid;
    }
}
