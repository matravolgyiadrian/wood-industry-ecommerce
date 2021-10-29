package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.Order;
import org.thesis.woodindustryecommerce.model.Status;
import org.thesis.woodindustryecommerce.model.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByIssuedOn(User customer);
    List<Order> findByStatusOrderByIssuedOn(Status status);
    List<Order> findByCustomerAndStatusOrderByIssuedOn(User customer, Status status);
}
