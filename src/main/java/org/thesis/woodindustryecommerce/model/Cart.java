package org.thesis.woodindustryecommerce.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne(mappedBy = "shoppingCart")
    private User user;

    @ElementCollection
    private List<String> cartItems;

    private double totalPrice = 0;

    public void addCartItem(CartItem item){
        this.cartItems.add("item");
        totalPrice += item.getTotalPrice();
    }
}
