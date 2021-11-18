package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thesis.woodindustryecommerce.model.*;
import org.thesis.woodindustryecommerce.model.binding.Billing;
import org.thesis.woodindustryecommerce.services.CouponService;
import org.thesis.woodindustryecommerce.services.OrderService;
import org.thesis.woodindustryecommerce.services.ProductService;
import org.thesis.woodindustryecommerce.services.UserService;

import javax.servlet.http.HttpServletRequest;
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
    public String addToCart(Long id, int quantity, HttpSession session, Model model, HttpServletRequest request) {

        Product product = productService.findById(id);
        CartItem cartItem = new CartItem(product, quantity);
        List<CartItem> cart = getCart(request.getSession());

        if (!addItem(cart, cartItem)) {
            model.addAttribute("notEnoughInStock", true);
        }

        return "redirect:/home";
    }

    @PostMapping("/cart/remove/")
    public String removeFromCart(Long id, HttpSession session, HttpServletRequest request) {
        List<CartItem> cart = getCart(request.getSession());
        cart.removeIf(item -> item.getProduct().getId().equals(id));


        return "cart";
    }

    @GetMapping("/cart/details")
    public String cartDetails(Model model, HttpSession session) {
        model.addAttribute("total_price", calculateTotalPrice(getCart(session)));
        model.addAttribute("discountPercentage", 0);
        model.addAttribute("discountMultiplier", 1);
        model.addAttribute("shopping_cart", (List<CartItem>) session.getAttribute("shopping_cart"));

        return "cart";
    }


    @PostMapping("/cart/validate-coupon")
    public String validate(Model model, String couponCode, HttpSession session, Principal principal, HttpServletRequest request){
        Coupon coupon = couponService.findByCouponCode(couponCode);

        if(coupon == null){
            model.addAttribute("discountPercentage", 0);
            model.addAttribute("discountMultiplier", 1);
        } else{
            model.addAttribute("discountPercentage", coupon.getDiscountAmount() != 0 ? coupon.getDiscountAmount() : 0);
            model.addAttribute("discountMultiplier", coupon.getMultiplier() != 1 ? coupon.getDiscountMultiplier() : 1);
            model.addAttribute("coupon_code", coupon.getCouponCode());

        }

        model.addAttribute("total_price", calculateTotalPrice(getCart(request.getSession())));
        model.addAttribute("billingForm", new Billing());
        if(principal!= null){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
        } else{
            model.addAttribute("user", new User());
        }
        return "checkout";
    }

    @GetMapping("/cart/checkout")
    public String checkout(Model model, HttpSession session, Principal principal){
        List<CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            model.addAttribute("emptyCart", true);
            model.addAttribute("total_price", calculateTotalPrice(getCart(session)));
            model.addAttribute("discountPercentage", 0);
            model.addAttribute("discountMultiplier", 1);

            return "cart";
        }
        model.addAttribute("total_price", calculateTotalPrice(getCart(session)));
        model.addAttribute("discountPercentage", 0);
        model.addAttribute("discountMultiplier", 1);
        model.addAttribute("billingForm", new Billing());

        if(principal != null){
            model.addAttribute("user", userService.findByUsername(principal.getName()));
        } else{
            model.addAttribute("user", new User());
        }

        return "checkout";
    }

    @PostMapping("/cart/checkout")
    public String checkout(Model model, HttpSession session, Principal principal, double discountMultiplier, Billing billingForm, HttpServletRequest request) {
        //TODO send email about the order
        List<CartItem> cart = getCart(request.getSession());

        Order order = new Order();
        order.setTotalPrice(calculateTotalPrice(cart));
        order.setProducts(cart);
        order.setShippingAddress(billingForm.getAddress());
        User customer;
        if(principal != null){
            customer = userService.findByUsername(principal.getName());
        } else {
            customer = userService.createGuestUser(billingForm.getFullName(), billingForm.getEmail(), billingForm.getAddress());
        }
        order.setCustomer(customer);

        for (CartItem item : cart) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            //Reordered from the product TODO notice admin about the reorder
            if(product.getStock() <= 10){
                product.setStock(200);
            }
            productService.save(product);
        }

        order.setTotalPrice(order.getTotalPrice() * discountMultiplier);

        orderService.save(order);

        request.getSession().setAttribute("shopping_cart", new LinkedList<>());

        return "redirect:/home";
    }

    private List<CartItem> getCart(HttpSession session) {
        if (session.getAttribute("shopping_cart") == null) {
            session.setAttribute("shopping_cart", new LinkedList<>());
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("shopping_cart");
        log.info("getCart: cart has");
        return cart;
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
