package org.thesis.woodindustryecommerce.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;
}
