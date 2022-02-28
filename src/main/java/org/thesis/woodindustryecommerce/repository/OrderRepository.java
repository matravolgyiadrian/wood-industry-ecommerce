package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.thesis.woodindustryecommerce.model.Order;
import org.thesis.woodindustryecommerce.model.Status;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Transactional(readOnly = true)
    @EntityGraph(value = "Order.detail")
    List<Order> findAll();
    @EntityGraph(value = "Order.detail")
    List<Order> findByCustomerOrderByIssuedOn(String customer);
}
