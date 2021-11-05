package org.thesis.woodindustryecommerce.model.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billing {
    private String fullName;
    private String email;
    private String address;
    private String paymentType;
    private String nameOnCard;
    private String creditCardNumber;
    private String exp;
    private String cvv;
}
