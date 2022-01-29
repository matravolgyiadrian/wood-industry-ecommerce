package org.thesis.woodindustryecommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thesis.woodindustryecommerce.model.Order;
import org.thesis.woodindustryecommerce.model.Status;
import org.thesis.woodindustryecommerce.repository.OrderRepository;
import org.thesis.woodindustryecommerce.services.implementations.OrderServiceImpl;

import java.time.*;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    private OrderServiceImpl underTest;

    private OrderRepository orderRepository;

    @BeforeEach
    public void init(){
        orderRepository = Mockito.mock(OrderRepository.class);

        underTest = new OrderServiceImpl(orderRepository);
    }

    @Test
    void testFindAllShouldCallOrderRepository(){
        //Given

        //When
        underTest.findAll();

        //Then
        Mockito.verify(orderRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindByCustomerShouldCallOrderRepositoryAndUserRepository(){
        //Given

        //When
        underTest.findByCustomer("jondoe");

        //Then
        Mockito.verify(orderRepository, Mockito.times(1)).findByCustomerOrderByIssuedOn("jondoe");
    }

    @Test
    void testFindByStatusShouldCallOrderRepository(){
        //Given

        //When
        underTest.findByStatus(Status.DELIVERED);

        //Then
        Mockito.verify(orderRepository, Mockito.times(1)).findByStatusOrderByIssuedOn(Status.DELIVERED);
    }

    @Test
    void testFindByIdShouldCallOrderRepository(){
        //Given
        Mockito.when(orderRepository.findById(0L)).thenReturn(Optional.of(new Order()));

        //When
        underTest.findById(0L);

        //Then
        Mockito.verify(orderRepository, Mockito.times(1)).findById(0L);
    }

    @Test
    void testFindByIdShouldThrowNoSuchElementException() {
        //Given
        Mockito.when(orderRepository.findById(0L)).thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(NoSuchElementException.class, () -> underTest.findById(0L));
        //Then
    }

    @Test
    void testChangeStatusShouldThrowNoSuchElementException() {
        //Given
        Mockito.when(orderRepository.findById(10L)).thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(NoSuchElementException.class, () -> underTest.changeStatus(10L));
        //Then
    }

    @Test
    void testChangeStatusShouldChangeTheStatus() {
        //Given
        Mockito.when(orderRepository.findById(10L)).thenReturn(Optional.ofNullable(
                Order.builder()
                        .id(10L)
                        .customer("jondoe")
                        .issuedOn(LocalDateTime.of(2021, 12, 1, 10, 0))
                        .shippingAddress("address")
                        .status(Status.PENDING)
                        .build()
        ));

        //When
        underTest.changeStatus(10L);

        //Then
        Mockito.verify(orderRepository).save(
                Order.builder()
                        .id(10L)
                        .customer("jondoe")
                        .issuedOn(LocalDateTime.of(2021, 12, 1, 10, 0))
                        .shippingAddress("address")
                        .status(Status.SHIPPED)
                        .build()
        );
    }

    @Test
    void testChangeStatusShouldNotChangeTheStatus() {
        //Given
        Mockito.when(orderRepository.findById(10L)).thenReturn(Optional.ofNullable(
                Order.builder()
                        .id(10L)
                        .customer("jondoe")
                        .issuedOn(LocalDateTime.of(2021, 12, 1, 10, 0))
                        .shippingAddress("address")
                        .status(Status.DELIVERED)
                        .build()
        ));

        //When
        underTest.changeStatus(10L);

        //Then
        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any(Order.class));
    }

    @Test
    void testSaveShouldSetStatusAndIssuedOn() {
        //Given
        Order order = Order.builder()
                .id(10L)
                .customer("jondoe")
                .shippingAddress("address")
                .build();

        //When
        underTest.save(order);

        //Then
        Mockito.verify(orderRepository).save(
                Order.builder()
                        .id(10L)
                        .customer("jondoe")
                        .issuedOn(order.getIssuedOn())
                        .shippingAddress("address")
                        .status(Status.PENDING)
                        .build()
        );

    }
}