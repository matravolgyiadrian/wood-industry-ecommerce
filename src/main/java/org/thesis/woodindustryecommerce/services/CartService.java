package org.thesis.woodindustryecommerce.services;

import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Cart;
import org.thesis.woodindustryecommerce.model.CartItem;

import java.util.List;

@Service
public interface CartService {
    List<Cart> findAll();
    Cart findById(Long id);
    Cart findByUserId(Long userId);
    Cart save(Cart cart);
    void addCartItem(Long id, CartItem item);
    void delete(Long id);
}
