package org.thesis.woodindustryecommerce.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Builder
@Embeddable
public class CartItem {
    private Product product;
    private int quantity;
    private double totalPrice;

    public void setQuantity(int qty){
        this.quantity = qty;
        this.totalPrice = product != null? product.getPrice() * qty : 0;
    }

    public void setProduct(Product product){
        this.product = product;
        this.totalPrice = product.getPrice() * quantity;
    }
}
