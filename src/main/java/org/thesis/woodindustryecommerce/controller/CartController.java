package org.thesis.woodindustryecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thesis.woodindustryecommerce.model.*;
import org.thesis.woodindustryecommerce.model.binding.Billing;
import org.thesis.woodindustryecommerce.services.OrderService;
import org.thesis.woodindustryecommerce.services.ProductService;
import org.thesis.woodindustryecommerce.services.UserService;
import org.thesis.woodindustryecommerce.services.implementations.EmailSenderService;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
@SessionAttributes("shopping_cart")
public class CartController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public CartController(UserService userService, ProductService productService, OrderService orderService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.emailSenderService = emailSenderService;
    }

    @ModelAttribute("total_price")
    public double addTotalPriceAttribute(@SessionAttribute("shopping_cart") List<CartItem> cart){
        return calculateTotalPrice(cart);
    }

    @PostMapping("/cart/add")
    public ModelAndView addToCart(Long id, int quantity, @SessionAttribute("shopping_cart") List<CartItem> cart, RedirectAttributes redirectAttr) {

        Product product = productService.findById(id);
        CartItem cartItem = new CartItem(product, quantity);

        if (!addItem(cart, cartItem)) {
            redirectAttr.addFlashAttribute("notEnoughInStock", true);

            return new ModelAndView("redirect:/home");
        }

        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(Long id, @SessionAttribute("shopping_cart") List<CartItem> cart) {

        cart.removeIf(item -> item.getProduct().getId().equals(id));

        return "cart";
    }

    @GetMapping("/cart/details")
    public String cartDetails(Model model) {

        return "cart";
    }

    @PostMapping("/cart/item/quantity")
    public ResponseEntity<?> cartItemQuantityHandler(@SessionAttribute("shopping_cart") List<CartItem> cart,
                                                     boolean increment, long id){
        for(CartItem item: cart){
            if(item.getProduct().getId() == id){
                //Checks if stock is enough
                if(increment && item.getQuantity()+1 > item.getProduct().getStock()){
                    return ResponseEntity.ok("0");
                }
                item.setQuantity(increment?item.getQuantity()+1: item.getQuantity()-1);
            }
        }

        return ResponseEntity.ok(increment?"++":"--");
    }

    @GetMapping("/cart/checkout")
    public String checkout(Model model, @SessionAttribute("shopping_cart") List<CartItem> cart, Principal principal){

        if (cart.isEmpty()) {
            model.addAttribute("emptyCart", true);

            return "cart";
        }

        if(principal!= null){
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("billingForm", new Billing(user.getFullName(), user.getEmail(), user.getAddress(), null, null, null, null, null));
        } else{
            model.addAttribute("billingForm", new Billing());
        }

        return "checkout_page";
    }

    @PostMapping("/cart/checkout")
    public String checkout(@SessionAttribute("shopping_cart") List<CartItem> cart,
                           Principal principal, double discountMultiplier, Billing billingForm) {
        double totalPrice = calculateTotalPrice(cart);

        Order order = new Order();
        order.setTotalPrice(totalPrice * discountMultiplier);
        order.setProducts(cart);
        order.setEmail(billingForm.getEmail());
        order.setShippingAddress(billingForm.getAddress());
        if(principal != null){
            order.setCustomer(billingForm.getFullName());
        } else {
            order.setCustomer(billingForm.getFullName()+" /GUEST/");
        }

        for (CartItem item : cart) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            if(!product.isStopOrder() && product.getStock() <= product.getReorderThreshold()){
                product.setStock(200);
                emailSenderService.sendProductReorderEmail(product.getName(), 200);
            }
            productService.save(product);
        }

        orderService.save(order);

        emailSenderService.sendTemplateEmail(order);
        cart.clear();

        return "redirect:/home";
    }

    private boolean addItem(List<CartItem> cart, CartItem item) {
        for (CartItem cartItem : cart) {
            if (cartItem.getProduct().getId().equals(item.getProduct().getId())){
                if(cartItem.getQuantity() + item.getQuantity() <= item.getProduct().getStock()){
                    cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (item.getQuantity() > item.getProduct().getStock()) {
            return false;
        }
        cart.add(item);

        return true;
    }

    private double calculateTotalPrice(List<CartItem> cart) {
        int sum = 0;
        for (CartItem item : cart) {
            sum += item.getTotalPrice();
        }
        return sum;
    }

}
