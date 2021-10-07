package org.thesis.woodindustryecommerce.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartId", referencedColumnName = "id")
    private Cart shoppingCart;
}
