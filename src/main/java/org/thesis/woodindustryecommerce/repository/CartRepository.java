package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.Cart;
import org.thesis.woodindustryecommerce.model.CartItem;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    Cart findByUserId(Long userId);

    @Query("UPDATE Cart c set c.cartItems = :cart WHERE c.id = :cartId")
    void updateCart(@Param("cartId") Long id, @Param("cart") Cart cart);
}
