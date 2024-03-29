package org.thesis.woodindustryecommerce.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
public class CartItem implements Serializable {
    @OneToOne
    private Product product;
    private int quantity;
    private double totalPrice;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
    }

    public void setQuantity(int qty) {
        this.quantity = qty;
        this.totalPrice = product != null ? product.getPrice() * qty : 0;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.totalPrice = product.getPrice() * quantity;
    }
}
