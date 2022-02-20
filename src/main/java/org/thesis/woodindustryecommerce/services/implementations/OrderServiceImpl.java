package org.thesis.woodindustryecommerce.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thesis.woodindustryecommerce.model.Order;
import org.thesis.woodindustryecommerce.model.Status;
import org.thesis.woodindustryecommerce.repository.OrderRepository;
import org.thesis.woodindustryecommerce.services.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, EmailSenderService emailSenderService) {
        this.orderRepository = orderRepository;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findByCustomer(String username) {
        return orderRepository.findByCustomerOrderByIssuedOn(username);
    }

    @Override
    public List<Order> findByStatus(Status status) {
        return orderRepository.findByStatusOrderByIssuedOn(status);
    }

    @Override
    public List<Order> findByCustomerAndStatus(String username, Status status) {
        return orderRepository.findByCustomerAndStatusOrderByIssuedOn(username, status);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public void changeStatus(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (order.getStatus().ordinal() != 2) {
            order.setStatus(Status.values()[order.getStatus().ordinal() + 1]);

            orderRepository.save(order);
            emailSenderService.sendOrderStatusChangedEmail(order);
        }
    }

    @Override
    public void save(Order order) {
        order.setIssuedOn(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        orderRepository.save(order);
    }
}
