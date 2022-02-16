package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thesis.woodindustryecommerce.services.OrderService;

import java.security.Principal;

@Controller
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllOrder(Model model, String keyword) {
        if(keyword != null){
            model.addAttribute("orders", orderService.findByKeyword(keyword));
        } else {
            model.addAttribute("orders", orderService.findAll());
        }

        return "order";
    }

    @GetMapping("/order/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeStatus(@PathVariable Long id) {
        orderService.changeStatus(id);

        return "redirect:/order/all";
    }
}
