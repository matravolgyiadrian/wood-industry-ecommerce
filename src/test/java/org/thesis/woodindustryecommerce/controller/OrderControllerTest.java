package org.thesis.woodindustryecommerce.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.thesis.woodindustryecommerce.model.*;
import org.thesis.woodindustryecommerce.services.OrderService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
@OverrideAutoConfiguration(enabled = true)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testGetAllOrderShouldAddAttributeOrders() throws Exception{
        //Given
        Order order = Order.builder()
                .customer("jondoe")
                .status(Status.PENDING)
                .shippingAddress("address")
                .products(List.of(new CartItem(Product.builder().name("Chair").price(100).stock(100).reorderThreshold(10).stopOrder(false).build(), 1)))
                .totalPrice(100)
                .build();
        Mockito.when(orderService.findAll()).thenReturn(List.of(order));

        //When
        mockMvc.perform(get("/order/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("orders", List.of(order)));

        //Then
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void changeStatus() throws Exception{
        //Given

        //When
        mockMvc.perform(get("/order/status/1"))
                .andExpect(redirectedUrl("/order/all"))
                .andExpect(view().name("redirect:/order/all"));

        //Then
        Mockito.verify(orderService, Mockito.times(1)).changeStatus(1L);
    }
}