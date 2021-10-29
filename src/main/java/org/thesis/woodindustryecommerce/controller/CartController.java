package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thesis.woodindustryecommerce.model.*;
import org.thesis.woodindustryecommerce.services.CouponService;
import org.thesis.woodindustryecommerce.services.OrderService;
import org.thesis.woodindustryecommerce.services.ProductService;
import org.thesis.woodindustryecommerce.services.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

@Controller
@Slf4j
public class CartController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CouponService couponService;

    @Autowired
    public CartController(UserService userService, ProductService productService, OrderService orderService, CouponService couponService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.couponService = couponService;
    }

    @PostMapping("/cart/add")
    public String addToCart(Long id, int quantity, HttpSession session, Model model) {

        Product product = productService.findById(id);
        CartItem cartItem = new CartItem(product, quantity);
        List<CartItem> cart = getCart(session);

        if (!addItem(cart, cartItem)) {
            model.addAttribute("notEnoughInStock", true);
        }

        return "redirect:/home";
    }

    @PostMapping("/cart/remove/")
    public String removeFromCart(Long id, HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(id));


        return "cart";
    }

    @GetMapping("/cart/details")
    public String cartDetails(Model model, HttpSession session) {
        model.addAttribute("total_price", calculateTotalPrice(getCart(session)));


        return "cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(Model model, HttpSession session, Principal principal, String couponCode) {
        List<CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            model.addAttribute("emptyCart", true);
            return "cart";
        }
        User customer = userService.findByUsername(principal.getName());
        Order order = Order.builder()
                .customer(customer)
                .products(cart)
                .totalPrice(calculateTotalPrice(cart))
                //TODO may be a custom address
                .shippingAddress(customer.getAddress())
                .build();

        for (CartItem item : cart) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productService.save(product);
        }

        log.info("coupon code: {}", couponCode);
        Coupon coupon = couponService.findByCouponCode(couponCode.toUpperCase());
        if (coupon != null) {
            order.setTotalPrice(order.getTotalPrice() * coupon.getMultiplier());
        }

        orderService.save(order);

        session.setAttribute("shopping_cart", new LinkedList<>());

        return "redirect:/home";
    }

    private List<CartItem> getCart(HttpSession session) {
        if (session.getAttribute("shopping_cart") == null) {
            session.setAttribute("shopping_cart", new LinkedList<>());
        }

        return (List<CartItem>) session.getAttribute("shopping_cart");
    }

    private boolean addItem(List<CartItem> cart, CartItem item) {
        for (CartItem cartItem : cart) {
            if (cartItem.getProduct().getId().equals(item.getProduct().getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return cartItem.getQuantity() <= cartItem.getProduct().getStock();
            }
        }
        if (item.getQuantity() > item.getProduct().getStock()) {
            return false;
        }
        cart.add(item);
        return true;
    }

    private long calculateTotalPrice(List<CartItem> cart) {
        int sum = 0;
        for (CartItem item : cart) {
            sum += item.getTotalPrice();
        }
        return sum;
    }

}
