package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<Order> findByStatusOrderByIssuedOn(Status status);
    List<Order> findByCustomerAndStatusOrderByIssuedOn(String customer, Status status);
    @EntityGraph(value = "Order.detail")
    @Query("from Order o where lower(o.customer) like lower(concat('%', :keyword, '%')) or lower(o.email) like lower(concat('%', :keyword, '%'))")
    List<Order> findByKeyword(@Param("keyword") String keyword);
}
