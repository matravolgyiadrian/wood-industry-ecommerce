package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
