package org.thesis.woodindustryecommerce.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Order;
import org.thesis.woodindustryecommerce.model.Status;
import org.thesis.woodindustryecommerce.model.User;
import org.thesis.woodindustryecommerce.repository.OrderRepository;
import org.thesis.woodindustryecommerce.repository.UserRepository;
import org.thesis.woodindustryecommerce.services.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findByCustomer(String username) {
        User user = userRepository.findByUsername(username);
        return orderRepository.findByCustomerOrderByIssuedOn(user);
    }

    @Override
    public List<Order> findByStatus(Status status) {
        return orderRepository.findByStatusOrderByIssuedOn(status);
    }

    @Override
    public List<Order> findByCustomerAndStatus(String username, Status status) {
        User user = userRepository.findByUsername(username);
        return orderRepository.findByCustomerAndStatusOrderByIssuedOn(user, status);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void changeStatus(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (order.getStatus().ordinal() != 2) {
            order.setStatus(Status.values()[order.getStatus().ordinal() + 1]);

            orderRepository.save(order);
        }
    }

    @Override
    public void save(Order order) {
        order.setIssuedOn(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        orderRepository.save(order);
    }
}
