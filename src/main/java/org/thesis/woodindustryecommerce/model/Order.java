package org.thesis.woodindustryecommerce.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    @ManyToMany(targetEntity = CartItem.class, cascade = CascadeType.ALL)
    @JoinTable(name = "orders_cart_items",
            joinColumns = @JoinColumn(name = "orders_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "cart_items_id", referencedColumnName = "id"))
    private List<CartItem> products;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "issued_on", nullable = false)
    private LocalDateTime issuedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;


}
