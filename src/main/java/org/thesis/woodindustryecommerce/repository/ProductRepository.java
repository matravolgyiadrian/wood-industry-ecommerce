package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("UPDATE Product p set p.name = :name WHERE p.id = :productId")
    void setProductName(@Param("productId") Long id, @Param("name") String name);

    @Query("UPDATE Product p set p.price = :price WHERE p.id = :productId")
    void setProductPrice(@Param("productId") Long id, @Param("price") double price);
}
