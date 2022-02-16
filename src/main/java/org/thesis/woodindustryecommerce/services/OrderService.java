package org.thesis.woodindustryecommerce.services;

import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Order;
import org.thesis.woodindustryecommerce.model.Status;

import java.util.List;

@Service
public interface OrderService {
    List<Order> findAll();
    List<Order> findByCustomer(String username);
    List<Order> findByStatus(Status status);
    List<Order> findByCustomerAndStatus(String username, Status status);
    List<Order> findByKeyword(String keyword);
    Order findById(Long id);
    void changeStatus(Long id);
    void save(Order order);

}
