package org.thesis.woodindustryecommerce.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.Cart;
import org.thesis.woodindustryecommerce.model.CartItem;
import org.thesis.woodindustryecommerce.repository.CartRepository;
import org.thesis.woodindustryecommerce.services.CartService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Cart findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void addCartItem(Long id, CartItem item) {
        Cart cartToUpdate = cartRepository.findById(id).orElseThrow(NoSuchElementException::new);
        cartToUpdate.addCartItem(item);
        cartRepository.updateCart(id, cartToUpdate);
    }


    @Override
    public void delete(Long id) {
        cartRepository.deleteById(id);
    }
}
