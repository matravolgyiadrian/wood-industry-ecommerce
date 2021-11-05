package org.thesis.woodindustryecommerce.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class Coupon {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String couponCode;

    @Column(nullable = false)
    private int discountAmount;

    public double getDiscountMultiplier() {
        return 1D - (double) discountAmount / 100D;
    }
    public double getMultiplier() {
        return (double) discountAmount / 100D;
    }
}
